package cosmos.registries.portal.impl;

import cosmos.constants.CosmosPortalTypes;
import cosmos.registries.portal.CosmosButtonPortal;
import cosmos.registries.portal.CosmosSignPortal;
import net.kyori.adventure.sound.Sound;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Set;

public class CosmosSignPortalImpl extends AbstractCosmosPortal implements CosmosSignPortal {

    public CosmosSignPortalImpl(final ResourceKey key, final BlockType trigger, final Set<ServerLocation> origins,
                                @Nullable final Ticks delay, @Nullable final ServerLocation destination,
                                @Nullable final Boolean nausea, @Nullable final ParticleEffect particles,
                                @Nullable final Long particlesInterval, @Nullable final Sound soundAmbiance,
                                @Nullable final Sound soundTravel, @Nullable final Sound soundTrigger) {

        super(
                key, origins, trigger, CosmosPortalTypes.SIGN.get(), delay, destination,
                Optional.ofNullable(nausea).orElse(true), particles,
                Optional.ofNullable(particlesInterval).orElse(20L),
                soundAmbiance,
                Optional.ofNullable(soundTravel).orElse(Sound.sound(SoundTypes.BLOCK_PORTAL_TRAVEL.get(), Sound.Source.BLOCK, 0.1f, 1f)),
                soundTrigger
        );
    }

}
