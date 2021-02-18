package cosmos.executors.commands.portal.modify;

import com.google.common.base.CaseFormat;
import cosmos.constants.CosmosKeys;
import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public abstract class AbstractPortalModifyCommand extends AbstractCommand {

    protected Object formattedModifiedValue;

    protected AbstractPortalModifyCommand(final Parameter... parameters) {
        super(parameters);
    }

    protected String propertyPrefix() {
        return "";
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        final CosmosPortal portal = context.one(CosmosKeys.PORTAL_COSMOS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_COSMOS));

        this.formattedModifiedValue = null;
        final CosmosPortal newPortal = this.newPortal(src, context, portal);
        super.serviceProvider.registry().portal().unregister(newPortal.key());
        super.serviceProvider.registry().portal().register(newPortal.key(), newPortal);
        String property = this.propertyPrefix();
        property += (property.isEmpty() ? "" : "-");
        property += CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName());

        super.serviceProvider.message()
                .getMessage(src, "success.portal.modify")
                .replace("portal", portal)
                .replace("prop", property)
                .replace("value", this.formattedModifiedValue)
                .condition("value", this.formattedModifiedValue != null)
                .green()
                .sendTo(src);
    }

    protected abstract CosmosPortal newPortal(Audience src, CommandContext context, CosmosPortal portal) throws CommandException;

}
