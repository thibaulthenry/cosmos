package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.CollectorSerializable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.CollisionRule;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibility;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TeamData implements CollectorSerializable<Team> {

    private final boolean allowFriendlyFire;
    private final CollisionRule collisionRule;
    private final String color;
    private final Visibility deathMessageVisibility;
    private final String displayName;
    private final String name;
    private final Visibility nameTagVisibility;
    private final List<String> players;
    private final String prefix;
    private final boolean seeFriendlyInvisibles;
    private final String suffix;

    public TeamData(final Team team) {
        final GsonComponentSerializer jsonSerializer = GsonComponentSerializer.gson();

        this.allowFriendlyFire = team.allowFriendlyFire();
        this.collisionRule = team.collisionRule();
        this.color = team.color().toString();
        this.deathMessageVisibility = team.deathMessageVisibility();
        this.displayName = jsonSerializer.serialize(team.displayName());
        this.name = team.name();
        this.nameTagVisibility = team.nameTagVisibility();

        this.players = team.members()
                .stream()
                .map(jsonSerializer::serialize)
                .collect(Collectors.toList());

        this.prefix = jsonSerializer.serialize(team.prefix());
        this.seeFriendlyInvisibles = team.canSeeFriendlyInvisibles();
        this.suffix = jsonSerializer.serialize(team.suffix());
    }

    public TeamData(final boolean allowFriendlyFire, final CollisionRule collisionRule, final String color,
                    final Visibility deathMessageVisibility, final String displayName, final String name,
                    final Visibility nameTagVisibility, final List<String> players, final String prefix,
                    final boolean seeFriendlyInvisibles, final String suffix) {
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
    public Optional<Team> collect() {
        if (this.name == null) {
            return Optional.empty();
        }

        final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();

        final NamedTextColor color = Optional.ofNullable(this.color)
                .map(NamedTextColor.NAMES::value)
                .orElse(NamedTextColor.WHITE);

        final Set<Component> members = this.players
                .stream()
                .map(gsonComponentSerializer::deserialize)
                .collect(Collectors.toSet());

        final Team.Builder teamBuilder = Team.builder()
                .allowFriendlyFire(this.allowFriendlyFire)
                .collisionRule(this.collisionRule)
                .color(color)
                .deathTextVisibility(this.deathMessageVisibility)
                .members(members)
                .name(this.name)
                .nameTagVisibility(this.nameTagVisibility)
                .canSeeFriendlyInvisibles(this.seeFriendlyInvisibles);

        Optional.ofNullable(this.displayName)
                .map(gsonComponentSerializer::deserialize)
                .ifPresent(teamBuilder::displayName);

        Optional.ofNullable(this.prefix)
                .map(gsonComponentSerializer::deserialize)
                .ifPresent(teamBuilder::prefix);

        Optional.ofNullable(this.suffix)
                .map(gsonComponentSerializer::deserialize)
                .ifPresent(teamBuilder::suffix);

        return Optional.of(teamBuilder.build());
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboard.Team.ALLOW_FRIENDLY_FIRE, this.allowFriendlyFire)
                .set(Queries.Scoreboard.Team.COLLISION_RULE, this.collisionRule.key(RegistryTypes.COLLISION_RULE))
                .set(Queries.Scoreboard.Team.COLOR, this.color)
                .set(Queries.Scoreboard.Team.DEATH_MESSAGE_VISIBILITY, this.deathMessageVisibility.key(RegistryTypes.VISIBILITY))
                .set(Queries.Scoreboard.Team.DISPLAY_NAME, this.displayName)
                .set(Queries.Scoreboard.Team.NAME, this.name)
                .set(Queries.Scoreboard.Team.NAME_TAG_VISIBILITY, this.nameTagVisibility.key(RegistryTypes.VISIBILITY))
                .set(Queries.Scoreboard.Team.PLAYERS, this.players)
                .set(Queries.Scoreboard.Team.PREFIX, this.prefix)
                .set(Queries.Scoreboard.Team.SEE_FRIENDLY_INVISIBLES, this.seeFriendlyInvisibles)
                .set(Queries.Scoreboard.Team.SUFFIX, this.suffix);
    }

}
