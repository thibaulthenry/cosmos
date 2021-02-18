package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Unload extends AbstractCommand {

    public Unload() {
        super(CosmosParameters.WORLD_ONLINE.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.one(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.online"));

        super.serviceProvider.world().unload(src, worldKey, false);

        super.serviceProvider.message()
                .getMessage(src, "success.root.unload")
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
