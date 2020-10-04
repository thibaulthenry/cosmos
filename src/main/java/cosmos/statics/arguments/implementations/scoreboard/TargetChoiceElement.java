package cosmos.statics.arguments.implementations.scoreboard;

import cosmos.statics.finders.FinderScoreboard;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.storage.WorldProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TargetChoiceElement extends AbstractScoreboardChoiceElement<Text> {

    public TargetChoiceElement(@Nullable Text key, @Nonnull Text worldKey) {
        super(key, worldKey);
    }

    @Override
    void setKeySupplier(WorldProperties worldProperties) {
        keySupplier = () -> FinderScoreboard.getTrackedPlayers(worldProperties.getUniqueId())
                .stream()
                .map(text -> {
                    if (hasAction(text)) {
                        return "\"[" + TextSerializers.JSON.serialize(text).replaceAll("\"", "\\\\\"") + "]\"";
                    }

                    String formattedName = TextSerializers.FORMATTING_CODE.serialize(text);

                    return formattedName.contains(" ") ? "\"" + formattedName + "\"" : formattedName;
                })
                .collect(Collectors.toList());
    }

    @Override
    void setValueSupplier(WorldProperties worldProperties) {
        valueSupplier = (key) -> FinderScoreboard.getTrackedPlayers(worldProperties.getUniqueId())
                .stream()
                .filter(text -> key.equals(text.toPlain()))
                .findFirst()
                .orElse(null);
    }

    private static boolean hasAction(Text text) {
        if (text == null) {
            return false;
        }

        return StreamSupport
                .stream(text.withChildren().spliterator(), true)
                .anyMatch(item -> item.getHoverAction().isPresent() || item.getClickAction().isPresent() || item.getShiftClickAction().isPresent());
    }
}
