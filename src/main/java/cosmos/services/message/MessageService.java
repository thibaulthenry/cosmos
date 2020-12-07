package cosmos.services.message;

import com.google.inject.ImplementedBy;
import cosmos.registries.message.Message;
import cosmos.registries.template.Template;
import cosmos.services.CosmosService;
import cosmos.services.message.impl.MessageServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Locale;

@ImplementedBy(MessageServiceImpl.class)
public interface MessageService extends CosmosService {

    Message getMessage(Template template);

    Message getMessage(Locale locale, String key);

    Message getMessage(Audience audience, String key);

    Message getMessage(CommandContext context, String key);

    Locale getLocale(Audience audience);

}
