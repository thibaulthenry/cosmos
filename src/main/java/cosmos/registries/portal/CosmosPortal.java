package cosmos.registries.portal;

import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.util.CopyableBuilder;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.Portal;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Set;

public interface CosmosPortal extends DataSerializable, Portal {

    static <T extends CosmosPortal> Builder<T> builder(final Class<Builder<T>> builderClass) {
        return Sponge.getGame().getBuilderProvider().provide(builderClass).reset();
    }

    <T extends CosmosPortal> Builder<T> asBuilder();

    Optional<Ticks> delay();

    boolean isTriggeredBy(BlockType blockType);

    ResourceKey key();

    boolean nausea();

    Set<ServerLocation> origins();

    int originsSize();

    Optional<ParticleEffect> particles();

    Ticks particlesInterval();

    Optional<Sound> soundAmbiance();

    Optional<Sound> soundDelay();

    Ticks soundDelayInterval();

    Optional<Sound> soundTravel();

    Optional<Sound> soundTrigger();

    BlockType trigger();

    PortalType type();

    interface Builder<T extends CosmosPortal> extends org.spongepowered.api.util.Builder<T, Builder<T>>, CopyableBuilder<T, Builder<T>> {

        Builder<T> delay(Ticks delay);

        Builder<T> destination(ServerLocation destination);

        Builder<T> key(ResourceKey key);

        Builder<T> nausea(boolean nausea);

        Builder<T> addOrigin(ServerLocation origin);

        Builder<T> origins(Set<ServerLocation> origins);

        Builder<T> particles(ParticleEffect particles);

        Builder<T> particlesInterval(Ticks particlesInterval);

        Builder<T> soundAmbiance(Sound soundAmbiance);

        Builder<T> soundDelay(Sound soundDelay);

        Builder<T> soundDelayInterval(Ticks soundDelayInterval);

        Builder<T> soundTravel(Sound soundTravel);

        Builder<T> soundTrigger(Sound soundTrigger);

        Builder<T> trigger(BlockType trigger);

    }

}
