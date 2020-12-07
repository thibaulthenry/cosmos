package cosmos.services.message.impl;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.message.Message;
import cosmos.registries.template.Template;
import cosmos.services.message.MessageService;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Locale;

@Singleton
public class MessageServiceImpl implements MessageService {

    @Override
    public Message getMessage(final Template template) {
        return new Message(template);
    }

    @Override
    public Message getMessage(final Locale locale, final String key) {
        return this.getMessage(Cosmos.getServices().template().getTemplateRegistry(locale).get(key));
    }

    @Override
    public Message getMessage(final Audience audience, final String key) {
        return this.getMessage(getLocale(audience), key);
    }

    @Override
    public Message getMessage(final CommandContext context, final String key) {
        return this.getMessage(context.getCause().getAudience(), key);
    }

    public Locale getLocale(final Audience audience) {
        if (audience instanceof ServerPlayer) {
            return ((ServerPlayer) audience).getLocale();
        }

        return Locale.ROOT;
    }
}
