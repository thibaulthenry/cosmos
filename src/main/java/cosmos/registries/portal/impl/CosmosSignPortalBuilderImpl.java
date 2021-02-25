package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import cosmos.registries.portal.CosmosPressurePlatePortal;
import cosmos.registries.portal.CosmosSignPortal;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class CosmosSignPortalBuilderImpl extends AbstractCosmosPortalBuilder<CosmosSignPortal> implements CosmosSignPortal.Builder {

    @Inject
    public CosmosSignPortalBuilderImpl() {
        this(CosmosSignPortal.class, 1);
    }

    protected CosmosSignPortalBuilderImpl(final Class<CosmosSignPortal> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    public @NonNull CosmosSignPortal build() {
        Preconditions.checkNotNull(super.key, "CosmosSignPortal cannot be null!");
        Preconditions.checkNotNull(super.trigger, "CosmosSignPortal key cannot be null");
        Preconditions.checkNotNull(super.origins, "CosmosSignPortal trigger cannot be null");
        Preconditions.checkArgument(!super.origins.isEmpty(), "CosmosSignPortal origins cannot be null");

        return new CosmosSignPortalImpl(
                super.key, super.trigger, super.origins,
                super.delay, super.destination,
                super.nausea, super.particles,
                super.particlesInterval, super.soundAmbiance,
                super.soundTravel, super.soundTrigger
        );
    }

    @Override
    protected Optional<CosmosSignPortal> buildContent(final DataView container) throws InvalidDataException {
        return Optional.empty(); // todo
    }

    @Override
    protected boolean isAnyOfTriggers(final BlockType trigger) {
        return CosmosSignPortal.isAnyOfTriggers(trigger);
    }

}
