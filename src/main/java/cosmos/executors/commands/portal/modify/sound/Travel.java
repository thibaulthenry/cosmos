package cosmos.executors.commands.portal.modify.sound;

import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.sound.Sound;

public class Travel extends AbstractSoundModifyCommand {

    @Override
    protected CosmosPortal getNewPortal(final CosmosPortal portal, final Sound sound) {
        return portal.asBuilder().soundTravel(sound).build();
    }

}
