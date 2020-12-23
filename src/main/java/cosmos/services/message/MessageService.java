package cosmos.services.message;

import com.google.inject.ImplementedBy;
import cosmos.registries.message.Message;
import cosmos.registries.template.Template;
import cosmos.services.CosmosService;
import cosmos.services.message.impl.MessageServiceImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Locale;
import java.util.function.Supplier;

@ImplementedBy(MessageServiceImpl.class)
public interface MessageService extends CosmosService {

    Supplier<CommandException> getError(Audience src, String key);

    Locale getLocale(Audience src);

    Message getMessage(Template template);

    Message getMessage(Locale locale, String key);

    Message getMessage(Audience src, String key);

    Message getMessage(CommandContext context, String key);

    TextComponent getText(Audience src, String key);

}
