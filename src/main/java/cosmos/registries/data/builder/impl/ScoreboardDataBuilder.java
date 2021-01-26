package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.DisplaySlotData;
import cosmos.registries.data.serializable.impl.ObjectiveData;
import cosmos.registries.data.serializable.impl.ScoreData;
import cosmos.registries.data.serializable.impl.ScoreboardData;
import cosmos.registries.data.serializable.impl.TeamData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ScoreboardDataBuilder extends AbstractDataBuilder<ScoreboardData> {

    @Inject
    public ScoreboardDataBuilder() {
        this(ScoreboardData.class, 1);
    }

    protected ScoreboardDataBuilder(final Class<ScoreboardData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<ScoreboardData> buildContent(final DataView container) throws InvalidDataException {
        final List<DisplaySlotData> displaySlotsData = container.getViewList(Queries.Scoreboards.DISPLAY_SLOTS)
                .map(viewList -> viewList
                        .stream()
                        .map(view -> Sponge.getDataManager().deserialize(DisplaySlotData.class, view))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());

        final List<ObjectiveData> objectivesData = container.getViewList(Queries.Scoreboards.OBJECTIVES)
                .map(viewList -> viewList
                        .stream()
                        .map(view -> Sponge.getDataManager().deserialize(ObjectiveData.class, view))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());

        final List<ScoreData> scoresData = container.getViewList(Queries.Scoreboards.SCORES)
                .map(viewList -> viewList
                        .stream()
                        .map(view -> Sponge.getDataManager().deserialize(ScoreData.class, view))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());

        final List<TeamData> teamsData = container.getViewList(Queries.Scoreboards.TEAMS)
                .map(viewList -> viewList
                        .stream()
                        .map(view -> Sponge.getDataManager().deserialize(TeamData.class, view))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());

        return Optional.of(new ScoreboardData(displaySlotsData, objectivesData, scoresData, teamsData));
    }

}
