package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.Flag;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class MoveTo extends AbstractCommand {

    public MoveTo() {
        super(
                Parameter.entity().key(CosmosKeys.ENTITY_DESTINATION).build(),
                CosmosParameters.ENTITIES.get().key(CosmosKeys.ENTITIES).optional().build()
        );
    }

    @Override
    protected List<String> additionalAliases() {
        return Arrays.asList("mvto", "tpto");
    }

    @Override
    protected Flag[] flags() {
        return new Flag[]{Flag.of(CosmosKeys.Flag.SAFE_ONLY)};
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final Entity destination = context.one(CosmosKeys.ENTITY_DESTINATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.ENTITY_DESTINATION));

        final Optional<List<Entity>> optionalEntities = context.one(CosmosKeys.ENTITIES);

        if (!(optionalEntities.isPresent() || src instanceof Entity)) {
            throw super.serviceProvider.message().getError(src, "error.missing.entities.any");
        }

        if (optionalEntities.isPresent() && optionalEntities.get().isEmpty()) {
            throw super.serviceProvider.message().getError(src, "error.empty-output");
        }

        final ServerLocation location = destination.serverLocation();
        final Vector3d rotation = destination.rotation();
        final boolean safeOnly = context.hasFlag(CosmosKeys.Flag.SAFE_ONLY);

        final Collection<Component> contents = context.one(CosmosKeys.ENTITIES)
                .orElse(Collections.singletonList((Entity) src))
                .stream()
                .map(target -> {
                    final boolean sourceIsTarget = super.serviceProvider.validation().isSelf(src, target);
                    final boolean sourceIsDestination = super.serviceProvider.validation().isSelf(src, destination);
                    final boolean targetIsDestination = super.serviceProvider.validation().isSelf(target, destination);
                    final String targetName = target instanceof Tamer ? ((Tamer) target).name() : target.uniqueId().toString();
                    final String destinationName = destination instanceof Tamer ? ((Tamer) destination).name() : destination.uniqueId().toString();

                    if (!super.serviceProvider.transportation().teleport(target, location, rotation, safeOnly)) {
                        return super.serviceProvider.message()
                                .getMessage(src, "error.root.move-to")
                                .replace("dest1", destinationName)
                                .replace("target", targetName)
                                .condition("dest1", !sourceIsDestination && !targetIsDestination)
                                .condition("dest2", sourceIsDestination)
                                .condition("dest3", !sourceIsDestination && targetIsDestination)
                                .condition("safe", safeOnly)
                                .condition("target", !sourceIsTarget)
                                .condition("verb", !sourceIsTarget)
                                .red()
                                .asText();
                    }

                    if (super.serviceProvider.transportation().mustNotify(src, target)) {
                        final Audience targetAudience = (Audience) target;

                        super.serviceProvider.message()
                                .getMessage(targetAudience, "success.root.move-to")
                                .replace("dest1", destinationName)
                                .condition("dest1", !targetIsDestination)
                                .condition("dest2", targetIsDestination)
                                .condition("dest3", false)
                                .condition("safe", safeOnly)
                                .condition("target", false)
                                .condition("verb", false)
                                .green()
                                .sendTo(targetAudience);
                    }

                    return super.serviceProvider.message()
                            .getMessage(src, "success.root.move-to")
                            .replace("dest1", destinationName)
                            .replace("target", targetName)
                            .condition("dest1", !sourceIsDestination && !targetIsDestination)
                            .condition("dest2", sourceIsDestination)
                            .condition("dest3", !sourceIsDestination && targetIsDestination)
                            .condition("safe", safeOnly)
                            .condition("target", !sourceIsTarget)
                            .condition("verb", !sourceIsTarget)
                            .green()
                            .asText();
                })
                .collect(Collectors.toList());


        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.root.move.list")
                .replace("number", contents.size())
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, true);
    }

}
