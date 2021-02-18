package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Import extends AbstractCommand {

    public Import() {
        super(CosmosParameters.WORLD_EXPORTED.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        Sponge.server().worldManager().loadTemplate(ResourceKey.of(Cosmos.NAMESPACE, "test"));
    }

}
