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
    private final ObjectiveDisplayMode displayMode;
    private final String displayName;
    private final String name;

    public ObjectiveData(final Objective objective) {
        this.criterion = objective.criterion();
        this.displayMode = objective.displayMode();
        this.displayName = GsonComponentSerializer.gson().serialize(objective.displayName());
        this.name = objective.name();
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
                        .displayName(GsonComponentSerializer.gson().deserialize(this.displayName))
                        .name(this.name)
                        .objectiveDisplayMode(this.displayMode)
                        .build()
        );
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboard.Objective.CRITERION, this.criterion.key(RegistryTypes.CRITERION))
                .set(Queries.Scoreboard.Objective.DISPLAY_MODE, this.displayMode.key(RegistryTypes.OBJECTIVE_DISPLAY_MODE))
                .set(Queries.Scoreboard.Objective.DISPLAY_NAME, this.displayName)
                .set(Queries.Scoreboard.Objective.NAME, this.name);
    }

}
