package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Import extends AbstractCommand {

    public Import() {
        super(CosmosParameters.Builder.WORLD_EXPORTED.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        Sponge.getServer().getWorldManager().loadTemplate(ResourceKey.of(Cosmos.NAMESPACE, "test"));
    }

}
