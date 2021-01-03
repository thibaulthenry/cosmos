package cosmos.executors.commands.root;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.world.WorldOnline;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.Flag;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3d;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class Move extends AbstractCommand {

    @Inject
    public Move(final Injector injector) {
        super(
                CosmosParameters.ENTITY_TARGETS_OPTIONAL,
                injector.getInstance(WorldOnline.class).builder().optional().build(),
                Parameter.vector3d().setKey(CosmosKeys.XYZ).optional().build(),
                Parameter.vector3d().setKey(CosmosKeys.PITCH_YAW_ROLL).optional().build()
        );
    }

    @Override
    protected List<String> aliases() {
        return Arrays.asList("mv", "tp");
    }

    @Override
    protected Flag[] flags() {
        return new Flag[]{Flag.of(CosmosKeys.FLAG_SAFE_ONLY)};
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ServerWorld world = Sponge.getServer().getWorldManager().world(this.serviceProvider.world().getKeyOrSource(context))
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.world"));

        final Optional<List<Entity>> optionalEntities = context.getOne(CosmosParameters.ENTITY_TARGETS_OPTIONAL);

        if (optionalEntities.isPresent() && optionalEntities.get().isEmpty()) {
            throw this.serviceProvider.message().getError(src, "error.invalid.entity");
        }

        if (!(optionalEntities.isPresent() || src instanceof Entity)) {
            throw this.serviceProvider.message().getError(src, "error.missing.entities");
        }

        final Optional<Vector3d> optionalPosition = context.getOne(CosmosKeys.XYZ);
        final ServerLocation location = optionalPosition.map(world::getLocation).orElse(world.getLocation(world.getProperties().spawnPosition()));
        final Vector3d position = location.getPosition();
        final Vector3d rotation = context.getOne(CosmosKeys.PITCH_YAW_ROLL).orElse(null);
        final boolean safeOnly = context.hasFlag(CosmosKeys.FLAG_SAFE_ONLY);

        final Collection<Component> contents = context.getOne(CosmosParameters.ENTITY_TARGETS_OPTIONAL)
                .orElse(Collections.singletonList((Entity) src))
                .stream()
                .map(target -> {
                    final boolean other = !this.serviceProvider.transportation().isSelf(src, target);
                    final String targetName = target instanceof Tamer ? ((Tamer) target).getName() : target.getUniqueId().toString();

                    if (!this.serviceProvider.transportation().teleport(target, location, rotation, safeOnly)) {
                        return this.serviceProvider.message()
                                .getMessage(src, "error.root.move")
                                .replace("target", targetName)
                                .replace("world", world)
                                .replace("position", position)
                                .condition("target", other)
                                .condition("verb", other)
                                .condition("safe", safeOnly)
                                .red()
                                .asText();
                    }

                    if (this.serviceProvider.transportation().mustNotify(src, target)) {
                        final Audience targetAudience = (Audience) target;

                        this.serviceProvider.message()
                                .getMessage(targetAudience, "success.root.move")
                                .replace("world", world)
                                .replace("position", position)
                                .condition("target", false)
                                .condition("verb", false)
                                .condition("safe", safeOnly)
                                .green()
                                .sendTo(targetAudience);
                    }

                    return this.serviceProvider.message()
                            .getMessage(src, "success.root.move")
                            .replace("target", targetName)
                            .replace("world", world)
                            .replace("position", position)
                            .condition("target", other)
                            .condition("verb", other)
                            .condition("safe", safeOnly)
                            .green()
                            .asText();
                })
                .collect(Collectors.toList());

        final TextComponent title = this.serviceProvider.message()
                .getMessage(src, "success.root.move.list")
                .replace("number", contents.size())
                .green()
                .asText();

        this.serviceProvider.pagination().send(src, title, contents, true);
    }

}
