package cosmos.executors.commands.portal.selection;

import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

abstract class AbstractPortalSelectionCommand extends AbstractCommand {

    AbstractPortalSelectionCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        if (!(src instanceof Identifiable)) {
            throw super.serviceProvider.message().getError(src, "error.missing.portal.selection.any");
        }

        final UUID key = ((Identifiable) src).uniqueId();
        final Optional<Set<ServerLocation>> optionalSelection = super.serviceProvider.registry().portalSelection().find(key);

        if (!optionalSelection.isPresent() || optionalSelection.get().isEmpty()) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.missing.portal.selection")
                    .clickEvent("tools", ClickEvent.suggestCommand("/cm portal tools"))
                    .hoverEvent("tools", HoverEvent.showText(super.serviceProvider.message().getText(src, "error.missing.portal.selection.hover")))
                    .asError();
        }

        this.run(src, context, key, optionalSelection.get());
    }

    protected abstract void run(Audience src, CommandContext context, UUID key, Set<ServerLocation> selection) throws CommandException;

}
