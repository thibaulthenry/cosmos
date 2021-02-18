package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.executors.commands.portal.modify.AbstractPortalModifyCommand;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Fluctuation extends AbstractPortalModifyCommand {

    public Fluctuation() {
        super(Parameter.bool().key(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Boolean fluctuation = context.one(CosmosKeys.STATE)
                .map(value -> {
                    super.formattedModifiedValue = value;
                    return value;
                })
                .orElse(null);

        return portal.asBuilder().particlesFluctuation(fluctuation).build();
    }

    @Override
    protected String propertyPrefix() {
        return "particles";
    }

}
