package cosmos.executors.commands.portal.modify;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Ticks;

@Singleton
public class Delay extends AbstractPortalModifyCommand {

    public Delay() {
        super(
                CosmosParameters.Builder.PORTAL_ALL.get().build(),
                CosmosParameters.Builder.DURATION_WITH_TIME_UNIT.get().build()
        );
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final long duration = context.getOne(CosmosKeys.DURATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.DURATION));

        return portal.asBuilder().delay(Ticks.zero()).build(); // todo
    }

}
