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
public class Load extends AbstractCommand {

    // TODO https://github.com/SpongePowered/Sponge/issues/3256

    public Load() {
        super(CosmosParameters.WORLD_OFFLINE.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.one(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.offline"));

        super.serviceProvider.world().load(src, worldKey, false);

        super.serviceProvider.message()
                .getMessage(src, "success.root.load")
                .replace("world", worldKey)
                .green()
                .sendTo(src);
    }

}
