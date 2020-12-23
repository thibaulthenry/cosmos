package cosmos.services.message.impl;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.registries.message.Message;
import cosmos.registries.template.Template;
import cosmos.services.message.MessageService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Locale;
import java.util.function.Supplier;

@Singleton
public class MessageServiceImpl implements MessageService {

    public Supplier<CommandException> getError(final Audience src, final String key) {
        return this.getMessage(src, key).asSupplier();
    }

    public Locale getLocale(final Audience src) {
        if (src instanceof ServerPlayer) {
            return ((ServerPlayer) src).getLocale();
        }

        return Locale.ROOT;
    }

    @Override
    public Message getMessage(final Template template) {
        return new Message(template);
    }

    @Override
    public Message getMessage(final Locale locale, final String key) {
        return this.getMessage(Cosmos.getServices().template().getTemplateRegistry(locale).get(key));
    }

    @Override
    public Message getMessage(final Audience src, final String key) {
        return this.getMessage(getLocale(src), key);
    }

    @Override
    public Message getMessage(final CommandContext context, final String key) {
        return this.getMessage(context.getCause().getAudience(), key);
    }

    @Override
    public TextComponent getText(final Audience src, final String key) {
        return this.getMessage(src, key).asText();
    }

}
