package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.TeamData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;
import java.util.Optional;

@Singleton
public class TeamDataBuilder extends AbstractDataBuilder<TeamData> {

    @Inject
    public TeamDataBuilder() {
        this(TeamData.class, 1);
    }

    protected TeamDataBuilder(final Class<TeamData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<TeamData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Scoreboards.Team.ALLOW_FRIENDLY_FIRE, Queries.Scoreboards.Team.COLLISION_RULE,
                Queries.Scoreboards.Team.COLOR, Queries.Scoreboards.Team.DEATH_MESSAGE_VISIBILITY,
                Queries.Scoreboards.Team.DISPLAY_NAME, Queries.Scoreboards.Team.NAME, Queries.Scoreboards.Team.NAME_TAG_VISIBILITY,
                Queries.Scoreboards.Team.PLAYERS, Queries.Scoreboards.Team.PREFIX,
                Queries.Scoreboards.Team.SEE_FRIENDLY_INVISIBLES, Queries.Scoreboards.Team.SUFFIX)) {
            return Optional.empty();
        }

        final boolean allowFriendlyFire = container.getBoolean(Queries.Scoreboards.Team.ALLOW_FRIENDLY_FIRE)
                .orElseThrow(() -> new InvalidDataException("Missing allow friendly fire while building TeamData"));
        final String collisionRule = container.getString(Queries.Scoreboards.Team.COLLISION_RULE)
                .orElseThrow(() -> new InvalidDataException("Missing collision rule while building TeamData"));
        final String color = container.getString(Queries.Scoreboards.Team.COLOR)
                .orElseThrow(() -> new InvalidDataException("Missing color while building TeamData"));
        final String deathMessageVisibility = container.getString(Queries.Scoreboards.Team.DEATH_MESSAGE_VISIBILITY)
                .orElseThrow(() -> new InvalidDataException("Missing death message visibility while building TeamData"));
        final String displayName = container.getString(Queries.Scoreboards.Team.DISPLAY_NAME)
                .orElseThrow(() -> new InvalidDataException("Missing display name while building TeamData"));
        final String name = container.getString(Queries.Scoreboards.Team.NAME)
                .orElseThrow(() -> new InvalidDataException("Missing name while building TeamData"));
        final String nameTagVisibility = container.getString(Queries.Scoreboards.Team.NAME_TAG_VISIBILITY)
                .orElseThrow(() -> new InvalidDataException("Missing name tag visibility while building TeamData"));
        final List<String> players = container.getStringList(Queries.Scoreboards.Team.PLAYERS)
                .orElseThrow(() -> new InvalidDataException("Missing players while building TeamData"));
        final String prefix = container.getString(Queries.Scoreboards.Team.PREFIX)
                .orElseThrow(() -> new InvalidDataException("Missing prefix while building TeamData"));
        final boolean seeFriendlyInvisibles = container.getBoolean(Queries.Scoreboards.Team.SEE_FRIENDLY_INVISIBLES)
                .orElseThrow(() -> new InvalidDataException("Missing see friendly invisibles while building TeamData"));
        final String suffix = container.getString(Queries.Scoreboards.Team.SUFFIX)
                .orElseThrow(() -> new InvalidDataException("Missing suffix while building TeamData"));

        return Optional.of(new TeamData(allowFriendlyFire, collisionRule, color, deathMessageVisibility, displayName,
                name, nameTagVisibility, players, prefix, seeFriendlyInvisibles, suffix));
    }
}
