package cosmos.executors.commands.portal.modify.delay;

import cosmos.executors.commands.portal.modify.AbstractPortalModifyCommand;
import org.spongepowered.api.command.parameter.Parameter;

abstract class AbstractDelayModifyCommand extends AbstractPortalModifyCommand {

    AbstractDelayModifyCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected String propertyPrefix() {
        return "delay";
    }

}
