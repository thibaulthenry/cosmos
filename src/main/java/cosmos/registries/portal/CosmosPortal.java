package cosmos.registries.portal;

import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.util.CopyableBuilder;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.portal.Portal;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public interface CosmosPortal extends DataSerializable, Portal {

    Ticks getCooldown();

    ResourceKey getKey();

    PortalType getType();

    boolean hasCooldown();

    boolean hasNausea();

    Sound soundAmbiance();

    Sound soundTravel();

    Sound soundTrigger();

    interface Builder<T extends CosmosPortal> extends org.spongepowered.api.util.Builder<T, Builder<T>>, CopyableBuilder<T, Builder<T>> {

        Builder<T> cooldown(Ticks ticks);

        Builder<T> destination(ServerLocation destination);

        Builder<T> key(ResourceKey key);

        Builder<T> nausea(boolean state);

        Builder<T> origin(ServerLocation origin);

        Builder<T> soundAmbiance(Sound sound);

        Builder<T> soundTravel(Sound sound);

        Builder<T> soundTrigger(Sound sound);

    }

}
