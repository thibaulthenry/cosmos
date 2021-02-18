package cosmos.services.message.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.message.Message;
import cosmos.registries.template.Template;
import cosmos.services.message.MessageService;
import cosmos.services.template.TemplateService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Locale;
import java.util.function.Supplier;

@Singleton
public class MessageServiceImpl implements MessageService {

    private final TemplateService templateService;

    @Inject
    public MessageServiceImpl(final Injector injector) {
        this.templateService = injector.getInstance(TemplateService.class);
    }

    @Override
    public CommandException getError(final Audience src, final String key) {
        return this.getMessage(src, key).asError();
    }

    @Override
    public CommandException getError(final Audience src, final String key, final String replaceKey, final Object replacement) {
        return this.getMessage(src, key).replace(replaceKey, replacement).asError();
    }

    @Override
    public Locale getLocale(final Audience src) {
        if (src instanceof ServerPlayer) {
            return ((ServerPlayer) src).get(Keys.LOCALE).orElse(Locale.ROOT);
        }

        return Locale.ROOT;
    }

    @Override
    public Message getMessage(final Audience src, final String key) {
        return this.getMessage(this.getLocale(src), key);
    }

    @Override
    public Message getMessage(final CommandContext context, final String key) {
        return this.getMessage(context.cause().audience(), key);
    }

    @Override
    public Message getMessage(final Locale locale, final String key) {
        return this.getMessage(this.templateService.templateRegistry(locale).value(key));
    }

    @Override
    public Message getMessage(final Template template) {
        return new Message(template);
    }

    @Override
    public TextComponent getText(final Audience src, final String key) {
        return this.getMessage(src, key).asText();
    }

    @Override
    public Supplier<CommandException> supplyError(final Audience src, final String key) {
        return this.getMessage(src, key).asSupplier();
    }

    @Override
    public Supplier<CommandException> supplyError(final Audience src, final String key, final String replaceKey, final Object replacement) {
        return this.getMessage(src, key).replace(replaceKey, replacement).asSupplier();
    }

}
