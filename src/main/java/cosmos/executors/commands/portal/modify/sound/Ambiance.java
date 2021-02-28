package cosmos.executors.commands.portal.modify.sound;

import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.command.parameter.CommandContext;

public class Ambiance extends AbstractSoundModifyCommand {

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal, final Sound sound) {
        return portal.asBuilder().soundAmbiance(sound).build();
    }

}
