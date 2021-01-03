package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Team;

import java.util.List;
import java.util.stream.Collectors;

public class TeamData implements DataSerializable {

    private final boolean allowFriendlyFire;
    private final String collisionRule;
    private final String color;
    private final String deathMessageVisibility;
    private final String displayName;
    private final String name;
    private final String nameTagVisibility;
    private final List<String> players;
    private final String prefix;
    private final boolean seeFriendlyInvisibles;
    private final String suffix;

    public TeamData(final Team team) {
        final GsonComponentSerializer jsonSerializer = GsonComponentSerializer.gson();

        this.allowFriendlyFire = team.allowFriendlyFire();
        this.collisionRule = team.getCollisionRule().key(RegistryTypes.COLLISION_RULE).getFormatted();
        this.color = team.getColor().toString();
        this.deathMessageVisibility = team.getDeathMessageVisibility().key(RegistryTypes.VISIBILITY).getFormatted();
        this.displayName = jsonSerializer.serialize(team.getDisplayName());
        this.name = team.getName();
        this.nameTagVisibility = team.getNameTagVisibility().key(RegistryTypes.VISIBILITY).getFormatted();
        this.players = team.getMembers()
                .stream()
                .map(jsonSerializer::serialize)
                .collect(Collectors.toList());
        this.prefix = jsonSerializer.serialize(team.getPrefix());
        this.seeFriendlyInvisibles = team.canSeeFriendlyInvisibles();
        this.suffix = jsonSerializer.serialize(team.getSuffix());
    }

    public TeamData(final boolean allowFriendlyFire, final String collisionRule, final String color, final String deathMessageVisibility,
                    final String displayName, final String name, final String nameTagVisibility, final List<String> players,
                    final String prefix, final boolean seeFriendlyInvisibles, final String suffix) {
        this.allowFriendlyFire = allowFriendlyFire;
        this.collisionRule = collisionRule;
        this.color = color;
        this.deathMessageVisibility = deathMessageVisibility;
        this.displayName = displayName;
        this.name = name;
        this.nameTagVisibility = nameTagVisibility;
        this.players = players;
        this.prefix = prefix;
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.suffix = suffix;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboards.Team.ALLOW_FRIENDLY_FIRE, this.allowFriendlyFire)
                .set(Queries.Scoreboards.Team.COLLISION_RULE, this.collisionRule)
                .set(Queries.Scoreboards.Team.COLOR, this.color)
                .set(Queries.Scoreboards.Team.DEATH_MESSAGE_VISIBILITY, this.deathMessageVisibility)
                .set(Queries.Scoreboards.Team.DISPLAY_NAME, this.displayName)
                .set(Queries.Scoreboards.Team.NAME, this.name)
                .set(Queries.Scoreboards.Team.NAME_TAG_VISIBILITY, this.nameTagVisibility)
                .set(Queries.Scoreboards.Team.PLAYERS, this.players)
                .set(Queries.Scoreboards.Team.PREFIX, this.prefix)
                .set(Queries.Scoreboards.Team.SEE_FRIENDLY_INVISIBLES, this.seeFriendlyInvisibles)
                .set(Queries.Scoreboards.Team.SUFFIX, this.suffix);
    }
}
