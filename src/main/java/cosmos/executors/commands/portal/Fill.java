package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.managed.Flag;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.blockray.RayTrace;
import org.spongepowered.api.util.blockray.RayTraceResult;
import org.spongepowered.api.world.LocatableBlock;

import java.util.Optional;

@Singleton
public class Fill extends AbstractCommand {

    public Fill() {
        super(CosmosParameters.PORTAL_ALL.get().build());
    }

    @Override
    protected Flag[] flags() {
        return new Flag[]{Flag.of(CosmosKeys.Flag.WITH_TARGET_BLOCK)};
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final CosmosPortal portal = context.one(CosmosKeys.PORTAL_COSMOS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_COSMOS));
// todo duplication bug when you update blocks surrounding flying sign or buttons
        if (!context.hasFlag(CosmosKeys.Flag.WITH_TARGET_BLOCK)) {
            super.serviceProvider.portal().fill(src, portal);

            super.serviceProvider.message()
                    .getMessage(src, "success.portal.fill")
                    .replace("portal", portal)
                    .replace("type", portal.trigger().key(RegistryTypes.BLOCK_TYPE))
                    .green()
                    .sendTo(src);

            return;
        }

        if (!(src instanceof Living)) {
            throw super.serviceProvider.message().getError(src, "error.missing.portal.fill");
        }

        final Living living = (Living) src;

        final Optional<RayTraceResult<LocatableBlock>> optionalRayTraceResult = RayTrace.block()
                .continueWhileBlock(RayTrace.onlyAir())
                .direction(living.headDirection())
                .limit(50)
                .select(RayTrace.nonAir())
                .sourceEyePosition(living)
                .world(living.serverLocation().world())
                .execute();

        if (!optionalRayTraceResult.isPresent()) {
            throw super.serviceProvider.message().getError(src, "error.missing.portal.fill.target");
        }

        final LocatableBlock locatableBlock = optionalRayTraceResult.get().selectedObject();
        final BlockType blockType = locatableBlock.blockState().type();

        if (!portal.type().isAnyOfTriggers(blockType)) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.portal.trigger")
                    .replace("block", blockType)
                    .replace("type", portal.type().key(RegistryTypes.PORTAL_TYPE))
                    .asError();
        }

        final Optional<? extends BlockEntity> optionalBlockEntity = locatableBlock.serverLocation().blockEntity();

        if (optionalBlockEntity.isPresent()) {
            super.serviceProvider.portal().fill(src, portal, optionalBlockEntity.get());
        } else {
            super.serviceProvider.portal().fill(src, portal, locatableBlock.blockState());
        }

        super.serviceProvider.message()
                .getMessage(src, "success.portal.fill")
                .replace("portal", portal)
                .replace("type", locatableBlock.blockState().type().key(RegistryTypes.BLOCK_TYPE))
                .green()
                .sendTo(src);
    }

}
