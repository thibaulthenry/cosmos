package cosmos.executors.commands.portal.modify;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Nausea extends AbstractPortalModifyCommand {

    public Nausea() {
        super(
                CosmosParameters.PORTAL_ALL.get().build(),
                Parameter.bool().key(CosmosKeys.STATE).optional().build()
        );
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Boolean nausea = context.one(CosmosKeys.STATE)
                .map(value -> {
                    super.formattedModifiedValue = value;
                    return value;
                })
                .orElse(null);

        return portal.asBuilder().nausea(nausea).build();
    }

}
