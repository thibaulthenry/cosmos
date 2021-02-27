package cosmos.executors.commands.portal.modify;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

@Singleton
public class Trigger extends AbstractPortalModifyCommand {

    public Trigger() {
        super(
                CosmosParameters.Builder.PORTAL_ALL.get().build(),
                CosmosParameters.Builder.PORTAL_BLOCK_TYPE.get().setKey(CosmosKeys.BLOCK_TYPE).build() // todo type dependent
        );
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final BlockType blockType = context.getOne(CosmosKeys.BLOCK_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.BLOCK_TYPE));

        super.serviceProvider.portal().fill(src, portal);

        return portal.asBuilder().trigger(blockType).build();
    }

}
