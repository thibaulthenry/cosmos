package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
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
        Preconditions.checkNotNull(super.origins, "CosmosSignPortal trigger cannot be null");
        Preconditions.checkArgument(!super.origins.isEmpty(), "CosmosSignPortal origins cannot be null");
        Preconditions.checkNotNull(super.trigger, "CosmosSignPortal key cannot be null");

        return new CosmosSignPortalImpl(
                super.key, super.origins, super.trigger,
                super.delay, super.delayFormat, super.delayGradientColors, super.delayShown,
                super.destination, super.nausea,
                super.particles, super.particlesFluctuation, super.particlesSpawnInterval, super.particlesViewDistance,
                super.soundAmbient, super.soundDelay, super.soundDelayInterval, super.soundTravel, super.soundTrigger
        );
    }

    @Override
    protected Optional<CosmosSignPortal> buildContent(final DataView container) throws InvalidDataException {
        return super.buildContent(container);
    }

    @Override
    protected boolean isAnyOfTriggers(final BlockType trigger) {
        return CosmosSignPortal.isAnyOfTriggers(trigger);
    }

}
