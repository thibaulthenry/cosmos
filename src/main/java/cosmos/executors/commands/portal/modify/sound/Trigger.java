package cosmos.executors.commands.portal.modify.sound;

import cosmos.registries.portal.CosmosFramePortal;
import net.kyori.adventure.sound.Sound;

public class Trigger extends AbstractSoundModifyCommand {

    @Override
    protected CosmosFramePortal getNewPortal(final CosmosFramePortal portal, final Sound sound) {
        return portal.asBuilder().soundTrigger(sound).build();
    }

}
