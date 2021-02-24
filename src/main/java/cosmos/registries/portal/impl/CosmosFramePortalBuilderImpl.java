package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import cosmos.registries.portal.CosmosFramePortal;
import org.checkerframework.checker.nullness.qual.NonNull;
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
    public CosmosFramePortal.Builder from(final CosmosFramePortal value) {
        Preconditions.checkNotNull(value, "CosmosPortal cannot be null!");
        Preconditions.checkNotNull(value.key(), "CosmosPortal key cannot be null");
        Preconditions.checkNotNull(value.trigger(), "CosmosPortal trigger cannot be null");
        Preconditions.checkNotNull(value.getOrigin(), "CosmosPortal origins cannot be null");

        super.delay = value.delay().orElse(null);
        super.destination = value.getDestination().orElse(null);
        super.key = value.key();
        super.nausea = value.nausea();
        super.origins = value.origins();
        super.particles = value.particles().orElse(null);
        super.particlesInterval = value.particlesInterval();
        super.soundAmbiance = value.soundAmbiance().orElse(null);
        super.soundTravel = value.soundTravel().orElse(null);
        super.soundTrigger = value.soundTrigger().orElse(null);
        super.trigger = value.trigger();

        return this;
    }

}
