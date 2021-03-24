package cosmos.executors.commands.perworld;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.registries.listener.impl.perworld.AbstractPerWorldListener;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Tuple;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class Group extends AbstractPerWorldCommand {

    public Group() {
        super(CosmosParameters.WORLD_DISTINCT.get().consumeAllRemaining().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final AbstractPerWorldListener listener) throws CommandException {
        final Set<ResourceKey> group = new HashSet<>(context.all(CosmosKeys.WORLD));

        if (group.isEmpty()) {
            throw super.serviceProvider.message().getError(src, "error.invalid.value", "param", CosmosKeys.WORLD);
        }

        final String featureName = super.serviceProvider.listener().format(listener.getClass());

        if (group.size() == 1) {
            super.serviceProvider.registry().group().unregister(Tuple.of(featureName, group.iterator().next()));
        } else {
            group.forEach(key -> super.serviceProvider.registry().group().register(Tuple.of(featureName, key), group));
        }

        // todo message
    }

}
