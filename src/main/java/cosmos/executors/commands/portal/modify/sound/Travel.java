package cosmos.executors.commands.portal.modify.sound;

import cosmos.registries.portal.CosmosFramePortal;
import net.kyori.adventure.sound.Sound;

public class Travel extends AbstractSoundModifyCommand {

    @Override
    protected CosmosFramePortal getNewPortal(final CosmosFramePortal portal, final Sound sound) {
        return portal.asBuilder().soundTravel(sound).build();
    }

}
