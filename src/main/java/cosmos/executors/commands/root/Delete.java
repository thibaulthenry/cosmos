package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Collections;
import java.util.List;

@Singleton
public class Delete extends AbstractCommand {

    public Delete() {
        super(CosmosParameters.WORLD_OFFLINE.get().build());
    }

    @Override
    protected List<String> additionalAliases() {
        return Collections.singletonList("remove");
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.one(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.offline"));

        super.serviceProvider.world().delete(src, worldKey, false);

        super.serviceProvider.message()
                .getMessage(src, "success.root.delete")
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
