package cosmos.registries.portal.impl;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import cosmos.registries.portal.CosmosPressurePlatePortal;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class CosmosPressurePlatePortalBuilderImpl extends AbstractCosmosPortalBuilder<CosmosPressurePlatePortal> implements CosmosPressurePlatePortal.Builder {

    @Inject
    public CosmosPressurePlatePortalBuilderImpl() {
        this(CosmosPressurePlatePortal.class, 1);
    }

    protected CosmosPressurePlatePortalBuilderImpl(final Class<CosmosPressurePlatePortal> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    public @NonNull CosmosPressurePlatePortal build() {
        Preconditions.checkNotNull(super.key, "CosmosPressurePlatePortal cannot be null!");
        Preconditions.checkNotNull(super.origins, "CosmosPressurePlatePortal trigger cannot be null");
        Preconditions.checkArgument(!super.origins.isEmpty(), "CosmosPressurePlatePortal origins cannot be null");
        Preconditions.checkNotNull(super.trigger, "CosmosPressurePlatePortal key cannot be null");

        return new CosmosPressurePlatePortalImpl(
                super.key, super.origins, super.trigger,
                super.delay, super.delayFormat, super.delayGradientColors, super.delayShown,
                super.destination, super.nausea,
                super.particles, super.particlesFluctuation, super.particlesSpawnInterval, super.particlesViewDistance,
                super.soundAmbient, super.soundDelay, super.soundDelayInterval, super.soundTravel, super.soundTrigger
        );
    }

    @Override
    protected Optional<CosmosPressurePlatePortal> buildContent(final DataView container) throws InvalidDataException {
        return super.buildContent(container);
    }

    @Override
    protected boolean isAnyOfTriggers(final BlockType trigger) {
        return CosmosPressurePlatePortal.isAnyOfTriggers(trigger);
    }

}
