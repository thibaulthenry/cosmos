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

    CommandException getError(Audience src, String key);

    CommandException getError(Audience src, String key, String replaceKey, Object replacement);

    Locale getLocale(Audience src);

    Message getMessage(Audience src, String key);

    Message getMessage(CommandContext context, String key);

    Message getMessage(Locale locale, String key);

    Message getMessage(Template template);

    TextComponent getText(Audience src, String key);

    Supplier<CommandException> supplyError(Audience src, String key);

    Supplier<CommandException> supplyError(Audience src, String key, String replaceKey, Object replacement);

}
