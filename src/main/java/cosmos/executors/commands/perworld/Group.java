package cosmos.executors.commands.perworld;

import com.google.inject.Singleton;
import cosmos.constants.ConfigurationNodes;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.PerWorldFeatures;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Tuple;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class Group extends AbstractPerWorldCommand {

    public Group() {
        super(CosmosParameters.WORLD_DISTINCT.get().consumeAllRemaining().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final PerWorldFeatures feature) throws CommandException {
        final Set<ResourceKey> group = new HashSet<>(context.all(CosmosKeys.WORLD));

        if (group.isEmpty()) {
            throw super.serviceProvider.message().getError(src, "error.invalid.value", "param", CosmosKeys.WORLD);
        }

        if (group.size() == 1) {
            super.serviceProvider.registry().group().unregister(Tuple.of(feature, group.iterator().next()));
        } else {
            group.forEach(key -> super.serviceProvider.registry().group().register(Tuple.of(feature, key), group));
        }

        final Set<ResourceKey> collectedWorlds = new HashSet<>();

        final Collection<Set<String>> featureGroups = super.serviceProvider.registry().group()
                .streamEntries()
                .filter(entry -> entry.key().first().equals(feature))
                .filter(entry -> !collectedWorlds.contains(entry.key().second()))
                .map(entry -> {
                    collectedWorlds.add(entry.key().second());
                    return entry.value().stream().map(ResourceKey::formatted).collect(Collectors.toSet());
                })
                .collect(Collectors.toSet());

        super.serviceProvider.configuration().saveValue(
                featureGroups,
                ConfigurationNodes.PER_WORLD,
                feature.formatted(),
                ConfigurationNodes.PER_WORLD_GROUPS
        );

        // todo message
    }

}
