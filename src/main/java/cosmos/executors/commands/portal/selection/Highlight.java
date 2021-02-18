package cosmos.executors.commands.portal.selection;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Set;
import java.util.UUID;

@Singleton
public class Highlight extends AbstractPortalSelectionCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final UUID key, final Set<ServerLocation> selection) throws CommandException {
        final String tag = "cosmos-portal-selection-" + key;
        super.serviceProvider.portal().highlight(BlockTypes.GLASS.get(), Ticks.of(599), tag, selection);

        super.serviceProvider.message()
                .getMessage(src, "success.portal.selection.highlight")
                .green()
                .sendTo(src);
    }

}
