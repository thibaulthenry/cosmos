package cosmos.executors.commands.portal.modify;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Trigger extends AbstractPortalModifyCommand {

    public Trigger() {
        super(
                CosmosParameters.PORTAL_ALL.get().build(),
                CosmosParameters.PORTAL_BLOCK_TYPE.get().build()
        );
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final BlockType blockType = context.one(CosmosKeys.BLOCK_TYPE)
                .map(value -> {
                    super.formattedModifiedValue = value.key(RegistryTypes.BLOCK_TYPE);
                    return value;
                })
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.BLOCK_TYPE));

        final CosmosPortal newPortal = portal.asBuilder().trigger(blockType).build();
        super.serviceProvider.portal().fill(src, newPortal);

        return newPortal;
    }

}
