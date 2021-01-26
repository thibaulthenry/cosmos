package cosmos.executors.parameters.impl.scoreboard;

import cosmos.services.ServiceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScoreHolders implements ValueParameter<List<Component>> {

    private final ServiceProvider serviceProvider;

    public ScoreHolders(final ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        final List<String> completions = this.getWorldScoreHolders(context)
                .stream()
                .filter(name -> name.startsWith(currentInput))
                .collect(Collectors.toList());

        if (currentInput != null && currentInput.isEmpty()) {
            completions.add(0, "*");
        }

        return completions;
    }

    @Override
    public Optional<? extends List<Component>> getValue(final Parameter.Key<? super List<Component>> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final String input = reader.parseString();
        final boolean isAsterisk = "*".equals(input);

        final List<Component> scoreHolders = this.getWorldScoreHolders(context)
                .stream()
                .filter(holder -> isAsterisk || holder.equals(input))
                .map(Component::text)
                .collect(Collectors.toList());

        if (!isAsterisk && scoreHolders.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(scoreHolders);
    }

    private Collection<String> getWorldScoreHolders(final CommandContext context) {
        return this.serviceProvider.world().findKeyOrSource(context)
                .map(worldKey -> this.serviceProvider.perWorld().scoreboards().getScoreHolders(worldKey))
                .orElse(Collections.emptyList())
                .stream()
                .map(displayName -> {
                    if (hasAction(displayName)) {
                        return GsonComponentSerializer.gson().serialize(displayName).replaceAll("\"", "\\\\\"");
                    }

                    final String formattedName = LegacyComponentSerializer.legacyAmpersand().serialize(displayName);

                    return formattedName.contains(" ") ? "\"" + formattedName + "\"" : formattedName;
                })
                .collect(Collectors.toList());
    }

    private boolean hasAction(final Component text) {
        if (text == null) {
            return false;
        }

        return text.children().parallelStream()
                .anyMatch(item -> item.hoverEvent() != null || item.clickEvent() != null);
    }

}
