package cosmos.constants;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.scoreboard.CollisionRule;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.text.format.TextColor;

import java.util.Optional;

public enum TeamOptions {

    COLLISION_RULE("collisionRule", CollisionRule.class),
    COLOR("color", TextColor.class),
    DEATH_MESSAGE_VISIBILITY("deathMessageVisibility", Visibility.class),
    FRIENDLYFIRE("friendlyfire", null),
    NAMETAG_VISIBILITY("nametagVisibility", Visibility.class),
    SEE_FRIENDLY_INVISIBLES("seeFriendlyInvisibles", null);

    private final String key;
    private final Class<? extends CatalogType> catalogClass;

    TeamOptions(String key, Class<? extends CatalogType> catalogClass) {
        this.catalogClass = catalogClass;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Optional<Class<? extends CatalogType>> getCatalogClass() {
        return Optional.ofNullable(catalogClass);
    }

    public String getText() {
        return name().toLowerCase().replace("_", " ");
    }
}
