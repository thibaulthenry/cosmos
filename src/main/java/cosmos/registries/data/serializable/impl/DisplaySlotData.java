package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.Objective;

public class DisplaySlotData implements ShareableSerializable<org.spongepowered.api.scoreboard.Scoreboard> {

    private final DisplaySlot displaySlot;
    private final String objective;

    public DisplaySlotData(final DisplaySlot displaySlot, final Objective objective) {
        this.displaySlot = displaySlot;
        this.objective = objective.name();
    }

    public DisplaySlotData(final DisplaySlot displaySlot, final String objective) {
        this.displaySlot = displaySlot;
        this.objective = objective;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public void share(final org.spongepowered.api.scoreboard.Scoreboard data) {
        data.objective(this.objective).ifPresent(objective -> data.updateDisplaySlot(objective, this.displaySlot));
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboard.DisplaySlot.DISPLAY_SLOT, this.displaySlot.key(RegistryTypes.DISPLAY_SLOT))
                .set(Queries.Scoreboard.DisplaySlot.OBJECTIVE, this.objective);
    }

}
