package cosmos.executors.commands.portal.modify.sound;

import cosmos.executors.commands.portal.modify.AbstractPortalModifyCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.registries.portal.CosmosPortal;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.registry.RegistryTypes;

abstract class AbstractSoundModifyCommand extends AbstractPortalModifyCommand {

    AbstractSoundModifyCommand(Parameter... parameters) {
        super(
                Parameter.seqBuilder(
                        Parameter.registryElement(TypeToken.get(SoundType.class), RegistryTypes.SOUND_TYPE)
                                .setKey(CosmosKeys.SOUND_TYPE)
                                .build()
                ).then(
                        Parameter.doubleNumber().setKey(CosmosKeys.VOLUME).optional().build(),
                        Parameter.doubleNumber().setKey(CosmosKeys.PITCH).optional().build()

                ).then(parameters).optional().build()
        );
    }

    @Override
    protected final CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Sound sound = context.getOne(CosmosKeys.SOUND_TYPE)
                .map(soundType -> {
                    final float volume = context.getOne(CosmosKeys.VOLUME).orElse(1d).floatValue();
                    final float pitch = context.getOne(CosmosKeys.PITCH).orElse(1d).floatValue();
                    return Sound.sound(soundType, Sound.Source.BLOCK, volume, pitch);
                })
                .orElse(null);

        return this.getNewPortal(src, context, portal, sound);
    }

    protected abstract CosmosPortal getNewPortal(Audience src, CommandContext context, CosmosPortal portal, Sound sound) throws CommandException;

}
