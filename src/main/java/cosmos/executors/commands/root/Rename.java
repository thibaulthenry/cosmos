package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Rename extends AbstractCommand {

    public Rename() {
        super(
                CosmosParameters.WORLD_ALL.get().build(),
                Parameter.string().key(CosmosKeys.NAME).build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.one(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.WORLD));

        final ResourceKey renameKey = context.one(CosmosKeys.NAME)
                .map(name -> ResourceKey.of(Cosmos.NAMESPACE, name))
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.NAME));

        super.serviceProvider.world().rename(src, worldKey, renameKey, true);

        super.serviceProvider.message()
                .getMessage(src, "success.root.rename")
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
