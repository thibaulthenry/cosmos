package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScoreboardData implements DataSerializable {

    private final List<DisplaySlotData> displaySlotsData;
    private final List<ObjectiveData> objectivesData;
    private final List<ScoreData> scoresData;
    private final List<TeamData> teamsData;

    public ScoreboardData(final Scoreboard scoreboard) {
        this.displaySlotsData = RegistryTypes.DISPLAY_SLOT.get()
                .stream()
                .map(displaySlot -> scoreboard.getObjective(displaySlot)
                        .map(objective -> new DisplaySlotData(displaySlot, objective))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        this.objectivesData = scoreboard.getObjectives()
                .stream()
                .map(ObjectiveData::new)
                .collect(Collectors.toList());

        this.scoresData = scoreboard.getScores()
                .stream()
                .map(score -> score.getObjectives()
                        .stream()
                        .map(objective -> new ScoreData(score, objective))
                        .collect(Collectors.toList())
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        this.teamsData = scoreboard.getTeams()
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
    public int getContentVersion() {
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
