package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.InventoryData;
import cosmos.registries.data.serializable.impl.InventorySlotData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class InventoryDataBuilder extends AbstractDataBuilder<InventoryData> {

    @Inject
    public InventoryDataBuilder() {
        this(InventoryData.class, 1);
    }

    protected InventoryDataBuilder(final Class<InventoryData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<InventoryData> buildContent(final DataView container) throws InvalidDataException {
        final Optional<List<DataView>> optionalViewList = container.getViewList(Queries.Inventories.INVENTORY);

        if (!optionalViewList.isPresent()) {
            return Optional.empty();
        }

        final List<InventorySlotData> slots = optionalViewList.get()
                .stream()
                .map(view -> Sponge.dataManager().deserialize(InventorySlotData.class, view))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return Optional.of(new InventoryData(slots));
    }

}
