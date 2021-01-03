package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.objective.Objective;

public class ObjectiveData implements DataSerializable {

    private final String criterion;
    private final String displayName;
    private final String name;
    private final String renderType;

    public ObjectiveData(final Objective objective) {
        this.criterion = objective.getCriterion().key(RegistryTypes.CRITERION).getFormatted(); // todo findKey instead of key
        this.displayName = GsonComponentSerializer.gson().serialize(objective.getDisplayName());
        this.name = objective.getName();
        this.renderType = objective.getDisplayMode().key(RegistryTypes.OBJECTIVE_DISPLAY_MODE).getFormatted();
    }

    public ObjectiveData(final String criterion, final String displayName, final String name, final String renderType) {
        this.criterion = criterion;
        this.displayName = displayName;
        this.name = name;
        this.renderType = renderType;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboards.Objective.CRITERION, this.criterion)
                .set(Queries.Scoreboards.Objective.DISPLAY_NAME, this.displayName)
                .set(Queries.Scoreboards.Objective.NAME, this.name)
                .set(Queries.Scoreboards.Objective.RENDER_TYPE, this.renderType);
    }
}
