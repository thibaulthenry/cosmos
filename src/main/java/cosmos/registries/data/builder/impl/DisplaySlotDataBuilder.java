package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.DisplaySlotData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;

import java.util.Optional;

@Singleton
public class DisplaySlotDataBuilder extends AbstractDataBuilder<DisplaySlotData> {

    @Inject
    public DisplaySlotDataBuilder() {
        this(DisplaySlotData.class, 1);
    }

    protected DisplaySlotDataBuilder(final Class<DisplaySlotData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<DisplaySlotData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Scoreboards.DisplaySlot.DISPLAY_SLOT, Queries.Scoreboards.DisplaySlot.OBJECTIVE)) {
            return Optional.empty();
        }

        final DisplaySlot displaySlot = container.getRegistryValue(Queries.Scoreboards.DisplaySlot.DISPLAY_SLOT, RegistryTypes.DISPLAY_SLOT)
                .orElseThrow(() -> new InvalidDataException("Missing display slot while building ObjectiveData"));
        final String objective = container.getString(Queries.Scoreboards.DisplaySlot.OBJECTIVE)
                .orElseThrow(() -> new InvalidDataException("Missing objective while building ObjectiveData"));

        return Optional.of(new DisplaySlotData(displaySlot, objective));
    }

}