package cosmos.executors.commands.portal.modify.delay;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Show extends AbstractDelayModifyCommand {

    public Show() {
        super(Parameter.bool().key(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Boolean delayShown = context.one(CosmosKeys.STATE)
                .map(value -> {
                    super.formattedModifiedValue = value;
                    return value;
                })
                .orElse(null);

        return portal.asBuilder().delayShown(delayShown).build();
    }

}
