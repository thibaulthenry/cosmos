package cosmos.executors.commands.portal.selection;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Set;
import java.util.UUID;

@Singleton
public class Clear extends AbstractPortalSelectionCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final UUID key, final Set<ServerLocation> selection) throws CommandException {
        if (!super.serviceProvider.registry().portalSelection().unregister(key).isPresent()) {
            throw super.serviceProvider.message().getError(src, "error.portal.selection.clear");
        }

        super.serviceProvider.message()
                .getMessage(src, "success.portal.selection.clear")
                .green()
                .sendTo(src);
    }

}
