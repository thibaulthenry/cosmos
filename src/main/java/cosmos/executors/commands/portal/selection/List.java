package cosmos.executors.commands.portal.selection;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class List extends AbstractPortalSelectionCommand {

    @Override
    protected void run(final Audience src, final CommandContext context, final UUID key, final Set<ServerLocation> selection) throws CommandException {
        final Locale locale = super.serviceProvider.message().getLocale(src);

        final Collection<Component> contents = selection
                .stream()
                .map(location -> super.serviceProvider.format().asText(location, locale))
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.portal.selection.list.header")
                .replace("number", contents.size())
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, false);
    }

}
