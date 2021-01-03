package cosmos.executors.parameters.impl.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.services.ServiceProvider;
import net.kyori.adventure.text.Component;
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

@Singleton
public class TrackedNames implements ValueParameter<Component> {

    private final ServiceProvider serviceProvider;

    @Inject
    public TrackedNames(final ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return this.getWorldScoreboardTrackedNames(context)
                .stream()
                .filter(name -> name.startsWith(currentInput))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends Component> getValue(final Parameter.Key<? super Component> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final String input = reader.peekString();

        return this.getWorldScoreboardTrackedNames(context)
                .stream()
                .filter(input::equals)
                .findFirst()
                .map(Component::text);
    }

    private Collection<String> getWorldScoreboardTrackedNames(final CommandContext context) {
        return this.serviceProvider.world().findKeyOrSource(context)
                .map(worldKey -> this.serviceProvider.perWorld().scoreboards().getTracked(worldKey))
                .orElse(Collections.emptyList())
                .stream()
                .map(displayName -> {
//                    if (hasAction(displayName)) {
//                        return "\"[" + TextSerializers.JSON.serialize(displayName).replaceAll("\"", "\\\\\"") + "]\"";
//                    }

                    final String formattedName = LegacyComponentSerializer.legacyAmpersand().serialize(displayName);

                    return formattedName.contains(" ") ? "\"" + formattedName + "\"" : formattedName;
                })
                .collect(Collectors.toList());
    }

//    private static boolean hasAction(Text text) {
//        if (text == null) {
//            return false;
//        }
//
//        return StreamSupport
//                .stream(text.withChildren().spliterator(), true)
//                .anyMatch(item -> item.getHoverAction().isPresent() || item.getClickAction().isPresent() || item.getShiftClickAction().isPresent());
//    }
}
