package cosmos.registries.formatter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.formatter.LocaleFormatter;
import cosmos.registries.message.Message;
import cosmos.registries.portal.CosmosPortal;
import cosmos.services.message.MessageService;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Singleton
public class CosmosPortalFormatter implements LocaleFormatter<CosmosPortal> {

    private final KeyFormatter keyFormatter;
    private final MessageService messageService;
    private final SoundFormatter soundFormatter;

    @Inject
    public CosmosPortalFormatter(final Injector injector) {
        this.keyFormatter = injector.getInstance(KeyFormatter.class);
        this.messageService = injector.getInstance(MessageService.class);
        this.soundFormatter = injector.getInstance(SoundFormatter.class);
    }

    @Override
    public TextComponent asText(final CosmosPortal value, final Locale locale) {
        final Optional<Ticks> optionalDelay = value.delay();
        final Optional<ServerLocation> optionalDestination = value.destination();
        final Optional<ParticleEffect> optionalParticles = value.particles();
        final Optional<Ticks> optionalParticlesSpawnInterval = value.particlesSpawnInterval();
        final Optional<Sound> optionalSoundAmbient = value.soundAmbient();
        final Optional<Sound> optionalSoundDelay = value.soundDelay();
        final Optional<Sound> optionalSoundTravel = value.soundTravel();
        final Optional<Sound> optionalSoundTrigger = value.soundTrigger();

        final Message message = this.messageService.getMessage(locale, "formatter.cosmos-portal.hover")
                .replace("name", value.key().formatted())
                .replace("origin_position", value.origin().position())
                .replace("origin_size", value.originsSize())
                .replace("origin_world", value.origin().world())
                .replace("particles", optionalParticles.map(particles -> this.asTextParticles(particles, locale)).orElse(Component.empty()))
                .replace("particles_spawn_interval", optionalParticlesSpawnInterval.map(ticks -> ticks.ticks() * 50).orElse(0L))
                .replace("particles_view_distance", value.particlesViewDistance().map(Objects::toString).orElse("∞"))
                .replace("sound_ambient", optionalSoundAmbient.map(sound -> this.soundFormatter.asTextHover(sound, locale)).orElse(Component.empty()))
                .replace("sound_delay", optionalSoundDelay.map(sound -> this.soundFormatter.asTextHover(sound, value.soundDelayInterval().orElse(Ticks.of(20)), locale)).orElse(Component.empty()))
                .replace("sound_travel", optionalSoundTravel.map(sound -> this.soundFormatter.asTextHover(sound, locale)).orElse(Component.empty()))
                .replace("sound_trigger", optionalSoundTrigger.map(sound -> this.soundFormatter.asTextHover(sound, locale)).orElse(Component.empty()))
                .replace("trigger", value.trigger().key(RegistryTypes.BLOCK_TYPE))
                .replace("type", value.type().key(RegistryTypes.PORTAL_TYPE))
                .condition("delay", optionalDelay.isPresent())
                .condition("destination_position", optionalDestination.isPresent())
                .condition("destination_world", optionalDestination.isPresent())
                .condition("nausea", value.nausea().orElse(false))
                .condition("particles", optionalParticles.isPresent())
                .condition("particles_spawn_interval", optionalParticles.isPresent() && optionalParticlesSpawnInterval.isPresent())
                .condition("particles_view_distance", optionalParticles.isPresent())
                .condition("sound_ambient", optionalSoundAmbient.isPresent())
                .condition("sound_delay", optionalSoundDelay.isPresent())
                .condition("sound_travel", optionalSoundTravel.isPresent())
                .condition("sound_trigger", optionalSoundTrigger.isPresent())
                .gray();

        optionalDelay.ifPresent(delay -> message.replace("delay", delay.ticks() * 50));

        optionalDestination.ifPresent(destination -> message
                .replace("destination_position", destination.position())
                .replace("destination_world", destination.world())
        );

        return this.keyFormatter.asText(value.key()).hoverEvent(HoverEvent.showText(message.asText()));
    }

    private TextComponent asTextParticles(final ParticleEffect particles, final Locale locale) {
        final Message message = this.messageService.getMessage(locale, "formatter.cosmos-portal.hover.particles")
                .condition("particles_block", false)
                .condition("particles_color", false)
                .condition("particles_direction", false)
                .condition("particles_fluctuation", false)
                .condition("particles_item", false)
                .condition("particles_offset", false)
                .condition("particles_potion_type", false)
                .condition("particles_quantity", false)
                .condition("particles_type", false)
                .condition("particles_velocity", false);

        particles.option(ParticleOptions.BLOCK_STATE)
                .map(BlockState::type)
                .map(type -> type.key(RegistryTypes.BLOCK_TYPE))
                .map(replacement -> message.replace("particles_block", replacement))
                .map(m -> message.condition("particles_block", true));

        particles.option(ParticleOptions.COLOR)
                .map(color -> NamedTextColor.ofExact(color.rgb()))
                .map(namedTextColor -> Component.text(namedTextColor.toString(), namedTextColor))
                .map(replacement -> message.replace("particles_color", replacement))
                .map(m -> message.condition("particles_color", true));

        particles.option(ParticleOptions.DIRECTION)
                .map(Enum::name)
                .map(String::toLowerCase)
                .map(replacement -> message.replace("particles_direction", replacement))
                .map(m -> message.condition("particles_direction", true));

        particles.option(ParticleOptions.ITEM_STACK_SNAPSHOT)
                .map(ItemStackSnapshot::type)
                .map(type -> type.key(RegistryTypes.ITEM_TYPE))
                .map(replacement -> message.replace("particles_item", replacement))
                .map(m -> message.condition("particles_item", true));

        particles.option(ParticleOptions.OFFSET)
                .map(replacement -> message.replace("particles_offset", replacement))
                .map(m -> message.condition("particles_offset", true));

        particles.option(ParticleOptions.POTION_EFFECT_TYPE)
                .map(type -> type.key(RegistryTypes.POTION_EFFECT_TYPE))
                .map(replacement -> message.replace("particles_potion_type", replacement))
                .map(m -> message.condition("particles_potion_type", true));

        particles.option(ParticleOptions.QUANTITY)
                .map(replacement -> message.replace("particles_quantity", replacement))
                .map(m -> message.condition("particles_quantity", true));

        message.replace("particles_type", particles.type().key(RegistryTypes.PARTICLE_TYPE))
                .condition("particles_type", true);

        particles.option(ParticleOptions.VELOCITY)
                .map(replacement -> message.replace("particles_velocity", replacement))
                .map(m -> message.condition("particles_velocity", true));

        return message.gray().asText();
    }

}
