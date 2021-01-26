package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.Objective;

public class DisplaySlotData implements ShareableSerializable<Scoreboard> {

    private final DisplaySlot displaySlot;
    private final String objective;

    public DisplaySlotData(final DisplaySlot displaySlot, final Objective objective) {
        this.displaySlot = displaySlot;
        this.objective = objective.getName();
    }

    public DisplaySlotData(final DisplaySlot displaySlot, final String objective) {
        this.displaySlot = displaySlot;
        this.objective = objective;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public void share(final Scoreboard data) {
        data.getObjective(this.objective).ifPresent(objective -> data.updateDisplaySlot(objective, this.displaySlot));
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Scoreboards.DisplaySlot.DISPLAY_SLOT, this.displaySlot.key(RegistryTypes.DISPLAY_SLOT))
                .set(Queries.Scoreboards.DisplaySlot.OBJECTIVE, this.objective);
    }

}
