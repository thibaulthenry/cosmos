package cosmos.executors.commands.portal.modify.sound;

import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.spongepowered.api.command.parameter.CommandContext;

public class Travel extends AbstractSoundModifyCommand {

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal, final Sound sound) {
        return portal.asBuilder().soundTravel(sound).build();
    }

}
