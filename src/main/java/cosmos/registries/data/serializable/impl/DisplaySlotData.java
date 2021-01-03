package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.objective.Objective;

public class DisplaySlotData implements DataSerializable {

    private final String displaySlot;
    private final String objective;

    public DisplaySlotData(final org.spongepowered.api.scoreboard.displayslot.DisplaySlot displaySlot, final Objective objective) {
        this.displaySlot = displaySlot.key(RegistryTypes.DISPLAY_SLOT).getFormatted(); // todo findKey instead of key
        this.objective = objective.getName();
    }

    public DisplaySlotData(final String displaySlot, final String objective) {
        this.displaySlot = displaySlot;
        this.objective = objective;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboards.DisplaySlot.DISPLAY_SLOT, this.displaySlot)
                .set(Queries.Scoreboards.DisplaySlot.OBJECTIVE, this.objective);
    }
}
