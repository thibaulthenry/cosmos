package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
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

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class Move extends AbstractCommand {

    public Move() {
        super(
                CosmosParameters.ENTITIES.get().key(CosmosKeys.ENTITIES).optional().build(),
                CosmosParameters.WORLD_ONLINE.get().build(),
                Parameter.vector3d().key(CosmosKeys.X_Y_Z).optional().build(),
                Parameter.vector3d().key(CosmosKeys.PITCH_YAW_ROLL).optional().build()
        );
    }

    @Override
    protected List<String> additionalAliases() {
        return Arrays.asList("mv", "tp");
    }

    @Override
    protected Flag[] flags() {
        return new Flag[]{Flag.of(CosmosKeys.Flag.SAFE_ONLY)};
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = super.serviceProvider.world().keyOrSource(context);

        final ServerWorld world = Sponge.server().worldManager().world(worldKey)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.missing.world", "world", worldKey));

        final Optional<List<Entity>> optionalEntities = context.one(CosmosKeys.ENTITIES);

        if (optionalEntities.isPresent() && optionalEntities.get().isEmpty()) {
            throw super.serviceProvider.message().getError(src, "error.invalid.value", "param", CosmosKeys.ENTITIES);
        }

        if (!(optionalEntities.isPresent() || src instanceof Entity)) {
            throw super.serviceProvider.message().getError(src, "error.missing.entities.any");
        }

        final Optional<Vector3d> optionalPosition = context.one(CosmosKeys.X_Y_Z);
        final ServerLocation location = optionalPosition.map(world::location).orElse(world.location(world.properties().spawnPosition()));
        final Vector3d position = location.position();
        final Vector3d rotation = context.one(CosmosKeys.PITCH_YAW_ROLL).orElse(null);
        final boolean safeOnly = context.hasFlag(CosmosKeys.Flag.SAFE_ONLY);

        final Collection<Component> contents = context.one(CosmosKeys.ENTITIES)
                .orElse(Collections.singletonList((Entity) src))
                .stream()
                .map(target -> {
                    final boolean other = !super.serviceProvider.validation().isSelf(src, target);
                    final String targetName = target instanceof Tamer ? ((Tamer) target).name() : target.uniqueId().toString();

                    if (!super.serviceProvider.transportation().teleport(target, location, rotation, safeOnly)) {
                        return super.serviceProvider.message()
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

                    if (super.serviceProvider.transportation().mustNotify(src, target)) {
                        final Audience targetAudience = (Audience) target;

                        super.serviceProvider.message()
                                .getMessage(targetAudience, "success.root.move")
                                .replace("world", world)
                                .replace("position", position)
                                .condition("target", false)
                                .condition("verb", false)
                                .condition("safe", safeOnly)
                                .green()
                                .sendTo(targetAudience);
                    }

                    return super.serviceProvider.message()
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

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.root.move.header")
                .replace("number", contents.size())
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, true);
    }

}
