package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.CollectorSerializable;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.criteria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;

import java.util.Optional;

public class ObjectiveData implements CollectorSerializable<Objective> {

    private final Criterion criterion;
    private final String displayName;
    private final String name;
    private final ObjectiveDisplayMode displayMode;

    public ObjectiveData(final Objective objective) {
        this.criterion = objective.getCriterion();
        this.displayName = GsonComponentSerializer.gson().serialize(objective.getDisplayName());
        this.name = objective.getName();
        this.displayMode = objective.getDisplayMode();
    }

    public ObjectiveData(final Criterion criterion, final ObjectiveDisplayMode displayMode, final String displayName, final String name) {
        this.criterion = criterion;
        this.displayMode = displayMode;
        this.displayName = displayName;
        this.name = name;
    }

    @Override
    public Optional<Objective> collect() {
        if (this.name == null || this.criterion == null) {
            return Optional.empty();
        }

        return Optional.of(
                Objective.builder()
                        .criterion(this.criterion)
                        .objectiveDisplayMode(this.displayMode)
                        .displayName(GsonComponentSerializer.gson().deserialize(this.displayName))
                        .name(this.name)
                        .build()
        );
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboards.Objective.CRITERION, this.criterion.key(RegistryTypes.CRITERION))
                .set(Queries.Scoreboards.Objective.DISPLAY_NAME, this.displayName)
                .set(Queries.Scoreboards.Objective.NAME, this.name)
                .set(Queries.Scoreboards.Objective.DISPLAY_MODE, this.displayMode.key(RegistryTypes.OBJECTIVE_DISPLAY_MODE));
    }

}
