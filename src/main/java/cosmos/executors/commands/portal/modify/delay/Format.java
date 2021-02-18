package cosmos.executors.commands.portal.modify.delay;

import com.google.common.base.CaseFormat;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.constants.DelayFormat;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Format extends AbstractDelayModifyCommand {

    public Format() {
        super(CosmosParameters.PORTAL_DELAY_FORMAT.get().key(CosmosKeys.PORTAL_DELAY_FORMAT).optional().build());
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final DelayFormat delayFormat = context.one(CosmosKeys.PORTAL_DELAY_FORMAT)
                .map(value -> {
                    super.formattedModifiedValue = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, value.name());
                    return value;
                })
                .orElse(null);

        return portal.asBuilder().delayFormat(delayFormat).build();
    }

}
