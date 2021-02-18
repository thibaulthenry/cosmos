package cosmos.registries.portal.impl;

import cosmos.constants.CosmosPortalTypes;
import cosmos.constants.DelayFormat;
import cosmos.registries.portal.CosmosButtonPortal;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collection;
import java.util.Set;

public class CosmosButtonPortalImpl extends AbstractCosmosPortal implements CosmosButtonPortal {

    public CosmosButtonPortalImpl(final ResourceKey key, final Set<ServerLocation> origins, final BlockType trigger,
                                  @Nullable final Ticks delay, @Nullable final DelayFormat delayFormat,
                                  @Nullable final Collection<? extends TextColor> delayGradientColors,
                                  @Nullable final Boolean delayShown, @Nullable final ServerLocation destination,
                                  @Nullable final Boolean nausea, @Nullable final ParticleEffect particles,
                                  @Nullable final Boolean particlesFluctuation, @Nullable final Ticks particlesSpawnInterval,
                                  @Nullable final Integer particlesViewDistance, @Nullable final Sound soundAmbient,
                                  @Nullable final Sound soundDelay, @Nullable final Ticks soundDelayInterval,
                                  @Nullable final Sound soundTravel, @Nullable final Sound soundTrigger) {

        super(
                key, origins, trigger, CosmosPortalTypes.BUTTON.get(),
                delay, delayFormat, delayGradientColors, delayShown, destination,
                nausea, particles, particlesFluctuation, particlesSpawnInterval, particlesViewDistance,
                soundAmbient, soundDelay, soundDelayInterval, soundTravel, soundTrigger
        );
    }

}
