package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.CollectorSerializable;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScoreboardData implements CollectorSerializable<Scoreboard> {

    private final List<DisplaySlotData> displaySlotsData;
    private final List<ObjectiveData> objectivesData;
    private final List<ScoreData> scoresData;
    private final List<TeamData> teamsData;

    public ScoreboardData(final Scoreboard scoreboard) {
        this.displaySlotsData = RegistryTypes.DISPLAY_SLOT.get()
                .stream()
                .map(displaySlot -> scoreboard.objective(displaySlot)
                        .map(objective -> new DisplaySlotData(displaySlot, objective))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        this.objectivesData = scoreboard.objectives()
                .stream()
                .map(ObjectiveData::new)
                .collect(Collectors.toList());

        this.scoresData = scoreboard.scores()
                .stream()
                .map(score -> score.objectives()
                        .stream()
                        .map(objective -> new ScoreData(score, objective))
                        .collect(Collectors.toList())
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        this.teamsData = scoreboard.teams()
                .stream()
                .map(TeamData::new)
                .collect(Collectors.toList());
    }

    public ScoreboardData(final List<DisplaySlotData> displaySlotsData, final List<ObjectiveData> objectivesData, final List<ScoreData> scoresData, final List<TeamData> teamsData) {
        this.displaySlotsData = displaySlotsData;
        this.objectivesData = objectivesData;
        this.scoresData = scoresData;
        this.teamsData = teamsData;
    }

    @Override
    public Optional<Scoreboard> collect() {
        final List<Objective> objectives = this.objectivesData
                .stream()
                .map(ObjectiveData::collect)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        final List<Team> teams = this.teamsData
                .stream()
                .map(TeamData::collect)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        final Scoreboard scoreboard = Scoreboard.builder().objectives(objectives).teams(teams).build();

        this.displaySlotsData.forEach(data -> data.share(scoreboard));
        this.scoresData.forEach(data -> data.share(scoreboard));

        return Optional.of(scoreboard);
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboards.DISPLAY_SLOTS, this.displaySlotsData)
                .set(Queries.Scoreboards.OBJECTIVES, this.objectivesData)
                .set(Queries.Scoreboards.SCORES, this.scoresData)
                .set(Queries.Scoreboards.TEAMS, this.teamsData);
    }

}
