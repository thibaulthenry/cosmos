package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.models.parameters.CosmosKeys;
import cosmos.models.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.Flag;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.world.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.ServerWorldProperties;
import org.spongepowered.math.vector.Vector3d;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class Move extends AbstractCommand {

    public Move() {
        super(
                CosmosParameters.ENTITIES,
                Parameter.worldProperties().setKey(CosmosKeys.WORLD).optional().build(),
                Parameter.vector3d().setKey(CosmosKeys.XYZ).optional().build(),
                Parameter.vector3d().setKey(CosmosKeys.PITCH_YAW_ROLL).optional().build()
        );
    }

    @Override
    protected Flag[] flags() {
        return new Flag[]{Flag.of(CosmosKeys.FLAG_SAFE_ONLY)};
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ServerWorldProperties properties = context.getOne(CosmosKeys.WORLD)
                .orElse(this.serviceProvider.world().getProperties(context));

        if (!properties.getWorld().isPresent()) {
            this.serviceProvider.world().load(src, properties, false);
        }

        if (!properties.getWorld().isPresent()) {
            throw this.serviceProvider.message()
                    .getMessage(src, "error.root.move.unloaded")
                    .replace("world", properties)
                    .asException();
        }

        final ServerWorld world = properties.getWorld().get();
        final Optional<Vector3d> optionalPosition = context.getOne(CosmosKeys.XYZ);
        final ServerLocation location = optionalPosition.map(world::getLocation).orElse(world.getLocation(world.getProperties().getSpawnPosition()));
        final Vector3d position = location.getPosition();
        final Vector3d rotation = context.getOne(CosmosKeys.PITCH_YAW_ROLL).orElse(null);
        final boolean safeOnly = context.hasFlag(CosmosKeys.FLAG_SAFE_ONLY);

        final Collection<Component> contents = context.getOne(CosmosParameters.ENTITIES).orElse(Collections.emptyList())
                .stream()
                .map(target -> {
                    final boolean notify = this.serviceProvider.transportation().mustNotify(src, target);
                    final boolean other = !this.serviceProvider.transportation().isSelf(src, target);
                    final String targetName = target instanceof Tamer ? ((Tamer) target).getName() : target.getUniqueId().toString();

                    if (!this.serviceProvider.transportation().teleport(target, location, rotation, safeOnly)) {
                        return this.serviceProvider.message()
                                .getMessage(src, "error.root.move")
                                .replace("target", targetName)
                                .replace("world", properties)
                                .replace("position", position)
                                .condition("target", other)
                                .condition("verb", other)
                                .condition("safe", safeOnly)
                                .errorColor()
                                .asText();
                    }

                    if (notify && target instanceof Audience) {
                        final Audience targetAudience = (Audience) target;

                        this.serviceProvider.message()
                                .getMessage(targetAudience, "success.root.move")
                                .replace("world", properties)
                                .replace("position", position)
                                .condition("target", false)
                                .condition("verb", false)
                                .condition("safe", safeOnly)
                                .successColor()
                                .sendTo(targetAudience);
                    }

                    return this.serviceProvider.message()
                            .getMessage(src, "success.root.move")
                            .replace("target", targetName)
                            .replace("world", properties)
                            .replace("position", position)
                            .condition("target", other)
                            .condition("verb", other)
                            .condition("safe", safeOnly)
                            .successColor()
                            .asText();
                })
                .collect(Collectors.toList());

        final TextComponent title = this.serviceProvider.message()
                .getMessage(src, "success.root.move.list")
                .replace("number", contents.size())
                .successColor()
                .asText();

        this.serviceProvider.pagination().sendPagination(src, title, contents, true);
    }

}
