package cosmos.registries.serializer.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.serializer.Serializer;
import cosmos.services.io.FinderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.registry.RegistryTypes;

import java.nio.file.Path;
import java.util.Optional;

@Singleton
public class PortalSerializer implements Serializer<CosmosPortal> {

    private final FinderService finderService;

    @Inject
    public PortalSerializer(final Injector injector) {
        this.finderService = injector.getInstance(FinderService.class);
    }

    @Override
    public Optional<CosmosPortal> deserialize(final Path path) {
        final DataContainer dataContainer = this.finderService.readFromFile(path).orElse(DataContainer.createNew());

        if (dataContainer.isEmpty()) {
            return Optional.empty();
        }

        return dataContainer.getRegistryValue(Queries.Portal.TYPE, RegistryTypes.PORTAL_TYPE)
                .filter(portalType -> portalType instanceof CosmosPortalType)
                .map(portalType -> (CosmosPortalType) portalType)
                .flatMap(portalType -> Sponge.dataManager().deserialize(portalType.portalClass(), dataContainer));
    }

    @Override
    public void serialize(final Path path, final CosmosPortal data) {
        this.finderService.writeToFile(data, path);
    }

}
