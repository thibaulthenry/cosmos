package cosmos.executors.commands.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.portal.PortalAll;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.effect.particle.ParticleType;

@Singleton
public class Trigger extends AbstractPortalModifyCommand {

    @Inject
    public Trigger(final Injector injector) {
        super(
                injector.getInstance(PortalAll.class).key(CosmosKeys.PORTAL_COSMOS).build(),
                CosmosParameters.PORTAL_BLOCK_TYPE
        );
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final BlockType blockType = context.getOne(CosmosKeys.BLOCK_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.BLOCK_TYPE));

        return portal.asBuilder().trigger(blockType).build();
    }

}
