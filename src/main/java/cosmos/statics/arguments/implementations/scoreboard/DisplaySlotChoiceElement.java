package cosmos.statics.arguments.implementations.scoreboard;

import com.google.common.base.CaseFormat;
import cosmos.statics.arguments.implementations.EnhancedCatalogedElement;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DisplaySlotChoiceElement extends EnhancedCatalogedElement<DisplaySlot> {

    private static final String SIDEBAR_TEAM_PREFIX = "sidebar.team.";

    private static final Map<String, String> coloredSidebar = Sponge.getRegistry().getAllOf(TextColor.class)
            .stream()
            .map(CatalogType::getName)
            .collect(Collectors.toMap(Function.identity(), colorName -> SIDEBAR_TEAM_PREFIX + colorName));

    public DisplaySlotChoiceElement(Text key) {
        super(key, DisplaySlot.class);
    }

    @Override
    protected Iterable<String> getChoices() {
        return StreamSupport.stream(super.getChoices().spliterator(), false)
                .map(DisplaySlotChoiceElement::toVanilla)
                .collect(Collectors.toList());
    }

    @Override
    protected Iterable<String> getOnCompleteChoices() {
        return StreamSupport.stream(super.getOnCompleteChoices().spliterator(), false)
                .map(DisplaySlotChoiceElement::toVanilla)
                .collect(Collectors.toList());
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        return super.getValue(toSponge(choice));
    }

    public static String toVanilla(String displaySlotName) {
        return coloredSidebar.getOrDefault(displaySlotName, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, displaySlotName));
    }

    private static String toSponge(String displaySlotName) {
        if (displaySlotName.startsWith(SIDEBAR_TEAM_PREFIX)) {
            return displaySlotName.substring(SIDEBAR_TEAM_PREFIX.length());
        }

        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, displaySlotName);
    }
}
