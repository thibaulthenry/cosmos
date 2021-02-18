package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.registries.data.serializable.impl.AdvancementProgressData;
import cosmos.registries.data.serializable.impl.AdvancementTreeData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class AdvancementTreeDataBuilder extends AbstractDataBuilder<AdvancementTreeData> {

    @Inject
    public AdvancementTreeDataBuilder() {
        this(AdvancementTreeData.class, 1);
    }

    protected AdvancementTreeDataBuilder(final Class<AdvancementTreeData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<AdvancementTreeData> buildContent(final DataView container) throws InvalidDataException {
        final List<AdvancementProgressData> advancementProgressesData = container.keys(false)
                .stream()
                .map(query -> container.getView(query)
                        .flatMap(view -> Sponge.dataManager().deserialize(AdvancementProgressData.class, view))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return Optional.of(new AdvancementTreeData(advancementProgressesData));
    }

}
