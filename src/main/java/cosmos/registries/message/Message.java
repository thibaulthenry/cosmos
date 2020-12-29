package cosmos.registries.message;

import cosmos.Cosmos;
import cosmos.registries.template.Template;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.spongepowered.api.command.exception.CommandException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class Message {

    private final Template template;
    private final Map<String, Component> replacementMap = new HashMap<>();
    private final Map<String, Boolean> conditionMap = new HashMap<>();
    private final Map<String, HoverEvent<?>> hoverEventMap = new HashMap<>();
    private final Map<String, ClickEvent> clickEventMap = new HashMap<>();
    private TextColor defaultColor = NamedTextColor.WHITE;

    public Message(final Template template) {
        this.template = template;
    }

    public Message replace(final String key, final Object replacement) {
        this.replacementMap.put(key, Cosmos.getServices().format().asText(replacement));
        return this;
    }

    public Message condition(final String key, final boolean condition) {
        this.conditionMap.put(key, condition);
        return this;
    }

    public Message hoverEvent(final String key, final HoverEvent<?> hoverEvent) {
        this.hoverEventMap.put(key, hoverEvent);
        return this;
    }

    public Message clickEvent(final String key, final ClickEvent clickEvent) {
        this.clickEventMap.put(key, clickEvent);
        return this;
    }

    public Message defaultColor(final TextColor defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }

    public Message successColor() {
        return this.defaultColor(NamedTextColor.GREEN);
    }

    public Message errorColor() {
        return this.defaultColor(NamedTextColor.RED);
    }

    public TextComponent asText() {
        return Optional.ofNullable(this.template)
                .map(t -> t.toText(this.replacementMap, this.conditionMap, this.hoverEventMap, this.clickEventMap, this.defaultColor).color(this.defaultColor))
                .orElseGet(() -> {
                    Cosmos.getLogger().info("No template found for message result");
                    return Component.empty();
                });
    }

    public CommandException asException() {
        return new CommandException(this.errorColor().asText());
    }

    public Supplier<CommandException> asSupplier() {
        return this::asException;
    }

    public void sendTo(final Audience src) {
        src.sendMessage(this.asText());
    }

}
