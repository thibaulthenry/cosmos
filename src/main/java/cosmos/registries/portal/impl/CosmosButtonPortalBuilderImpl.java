package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import cosmos.registries.portal.CosmosButtonPortal;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class CosmosButtonPortalBuilderImpl extends AbstractCosmosPortalBuilder<CosmosButtonPortal> implements CosmosButtonPortal.Builder {

    @Inject
    public CosmosButtonPortalBuilderImpl() {
        this(CosmosButtonPortal.class, 1);
    }

    protected CosmosButtonPortalBuilderImpl(final Class<CosmosButtonPortal> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    public @NonNull CosmosButtonPortal build() {
        Preconditions.checkNotNull(super.key, "CosmosButtonPortal cannot be null!");
        Preconditions.checkNotNull(super.origins, "CosmosButtonPortal trigger cannot be null");
        Preconditions.checkArgument(!super.origins.isEmpty(), "CosmosButtonPortal origins cannot be null");
        Preconditions.checkNotNull(super.trigger, "CosmosButtonPortal key cannot be null");

        return new CosmosButtonPortalImpl(
                super.key, super.origins, super.trigger,
                super.delay, super.delayFormat, super.delayGradientColors, super.delayShown,
                super.destination, super.nausea,
                super.particles, super.particlesFluctuation, super.particlesSpawnInterval, super.particlesViewDistance,
                super.soundAmbient, super.soundDelay, super.soundDelayInterval, super.soundTravel, super.soundTrigger
        );
    }

    @Override
    protected Optional<CosmosButtonPortal> buildContent(final DataView container) throws InvalidDataException {
        return super.buildContent(container);
    }

    @Override
    protected boolean isAnyOfTriggers(final BlockType trigger) {
        return CosmosButtonPortal.isAnyOfTriggers(trigger);
    }

}
