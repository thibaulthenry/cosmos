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
public class ViewDistance extends AbstractPortalModifyCommand {

    public ViewDistance() {
        super(Parameter.rangedInteger(1, Integer.MAX_VALUE).key(CosmosKeys.BLOCKS).optional().build());
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Integer viewDistance = context.one(CosmosKeys.BLOCKS)
                .map(value -> {
                    super.formattedModifiedValue = value;
                    return value;
                })
                .orElse(null);

        return portal.asBuilder().particlesViewDistance(viewDistance).build();
    }

}
