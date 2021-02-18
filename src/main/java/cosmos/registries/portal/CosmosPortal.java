package cosmos.registries.portal;

import cosmos.constants.DelayFormat;
import cosmos.registries.data.portal.CosmosPortalType;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.TextColor;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.CopyableBuilder;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.Portal;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface CosmosPortal extends DataSerializable, Portal {

    static <T extends CosmosPortal> Builder<T> builder(final Class<Builder<T>> builderClass) {
        return Sponge.game().builderProvider().provide(builderClass).reset();
    }

    <T extends CosmosPortal> Builder<T> asBuilder();

    Optional<Ticks> delay();

    Optional<DelayFormat> delayFormat();

    Optional<Collection<? extends TextColor>> delayGradientColors();

    Optional<Boolean> delayShown();

    boolean isTriggeredBy(BlockType blockType);

    ResourceKey key();

    Optional<Boolean> nausea();

    Set<ServerLocation> origins();

    int originsSize();

    Optional<ParticleEffect> particles();

    Optional<Boolean> particlesFluctuation();

    Optional<Ticks> particlesSpawnInterval();

    Optional<Integer> particlesViewDistance();

    Optional<Sound> soundAmbient();

    Optional<Sound> soundDelay();

    Optional<Ticks> soundDelayInterval();

    Optional<Sound> soundTravel();

    Optional<Sound> soundTrigger();

    BlockType trigger();

    CosmosPortalType type();

    interface Builder<T extends CosmosPortal> extends org.spongepowered.api.util.Builder<T, Builder<T>>, CopyableBuilder<T, Builder<T>> {

        Builder<T> addOrigin(ServerLocation origin);

        Builder<T> delay(Ticks delay);

        Builder<T> delayFormat(DelayFormat delayFormat);

        Builder<T> delayGradientColors(Collection<? extends TextColor> delayGradientColors);

        Builder<T> delayShown(Boolean delayShown);

        Builder<T> destination(ServerLocation destination);

        Builder<T> key(ResourceKey key);

        Builder<T> nausea(Boolean nausea);

        Builder<T> origins(Set<ServerLocation> origins);

        Builder<T> particles(ParticleEffect particles);

        Builder<T> particlesFluctuation(Boolean particlesFluctuation);

        Builder<T> particlesSpawnInterval(Ticks particlesSpawnInterval);

        Builder<T> particlesViewDistance(Integer particlesFluctuation);

        Builder<T> soundAmbient(Sound soundAmbient);

        Builder<T> soundDelay(Sound soundDelay);

        Builder<T> soundDelayInterval(Ticks soundDelayInterval);

        Builder<T> soundTravel(Sound soundTravel);

        Builder<T> soundTrigger(Sound soundTrigger);

        Builder<T> trigger(BlockType trigger);

    }

}
