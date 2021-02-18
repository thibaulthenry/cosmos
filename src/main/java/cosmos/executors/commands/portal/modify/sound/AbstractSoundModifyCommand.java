package cosmos.executors.commands.portal.modify.sound;

import cosmos.constants.CosmosKeys;
import cosmos.executors.commands.portal.modify.AbstractPortalModifyCommand;
import cosmos.registries.portal.CosmosPortal;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.registry.RegistryTypes;

abstract class AbstractSoundModifyCommand extends AbstractPortalModifyCommand {

    AbstractSoundModifyCommand(final Parameter... parameters) {
        super(
                Parameter.seqBuilder(
                        Parameter.registryElement(TypeToken.get(SoundType.class), RegistryTypes.SOUND_TYPE, ResourceKey.MINECRAFT_NAMESPACE)
                                .key(CosmosKeys.SOUND_TYPE)
                                .build()
                ).then(
                        Parameter.doubleNumber().key(CosmosKeys.VOLUME).optional().build(),
                        Parameter.doubleNumber().key(CosmosKeys.PITCH).optional().build()

                ).then(parameters).optional().build()
        );
    }

    @Override
    protected final CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Sound sound = context.one(CosmosKeys.SOUND_TYPE)
                .map(value -> {
                    final float volume = context.one(CosmosKeys.VOLUME).orElse(1.0).floatValue();
                    final float pitch = context.one(CosmosKeys.PITCH).orElse(1.0).floatValue();
                    return Sound.sound(value, Sound.Source.BLOCK, volume, pitch);
                })
                .map(value -> {
                    super.formattedModifiedValue = value;
                    return value;
                })
                .orElse(null);

        return this.newPortal(src, context, portal, sound);
    }

    @Override
    protected String propertyPrefix() {
        return "sound";
    }

    protected abstract CosmosPortal newPortal(Audience src, CommandContext context, CosmosPortal portal, Sound sound) throws CommandException;

}
