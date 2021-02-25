package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosSignPortal;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class CosmosFramePortalBuilderImpl extends AbstractCosmosPortalBuilder<CosmosFramePortal> implements CosmosFramePortal.Builder {

    @Inject
    public CosmosFramePortalBuilderImpl() {
        this(CosmosFramePortal.class, 1);
    }

    protected CosmosFramePortalBuilderImpl(final Class<CosmosFramePortal> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    public @NonNull CosmosFramePortal build() {
        Preconditions.checkNotNull(super.key, "CosmosPortal cannot be null!");
        Preconditions.checkNotNull(super.trigger, "CosmosPortal key cannot be null");
        Preconditions.checkNotNull(super.origins, "CosmosPortal trigger cannot be null");
        Preconditions.checkArgument(!super.origins.isEmpty(), "CosmosPortal origins cannot be null");

        return new CosmosFramePortalImpl(
                super.key, super.trigger, super.origins,
                super.delay, super.destination,
                super.nausea, super.particles,
                super.particlesInterval, super.soundAmbiance,
                super.soundTravel, super.soundTrigger
        );
    }

    @Override
    protected Optional<CosmosFramePortal> buildContent(final DataView container) throws InvalidDataException {
        return Optional.empty(); // todo
    }

    @Override
    protected boolean isAnyOfTriggers(final BlockType trigger) {
        return CosmosFramePortal.isAnyOfTriggers(trigger);
    }

}
