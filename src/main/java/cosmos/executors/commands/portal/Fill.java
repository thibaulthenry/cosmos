package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.Flag;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.util.blockray.RayTrace;
import org.spongepowered.api.util.blockray.RayTraceResult;
import org.spongepowered.api.world.LocatableBlock;

import java.util.Optional;

@Singleton
public class Fill extends AbstractCommand {

    public Fill() {
        super(CosmosParameters.Builder.PORTAL_ALL.get().build());
    }

    @Override
    protected Flag[] flags() {
        return new Flag[]{Flag.of(CosmosKeys.FLAG_WITH_TARGET_BLOCK)};
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final CosmosPortal portal = context.getOne(CosmosKeys.PORTAL_COSMOS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_COSMOS));

        if (!context.hasFlag(CosmosKeys.FLAG_WITH_TARGET_BLOCK)) {
            super.serviceProvider.portal().fill(src, portal);
            // todo

            return;
        }

        if (!(src instanceof Living)) {
            throw new CommandException(Component.text("cannot check target"));
        }

        final Living living = (Living) src;

        final Optional<RayTraceResult<LocatableBlock>> optionalRayTraceResult = RayTrace.block()
                .continueWhileBlock(RayTrace.onlyAir())
                .direction(living.getHeadDirection())
                .limit(50)
                .select(locatableBlock -> portal.isTriggeredBy(locatableBlock.getBlockState().getType()))
                .sourceEyePosition(living)
                .world(living.getServerLocation().getWorld())
                .execute();

        if (!optionalRayTraceResult.isPresent()) {
            throw new CommandException(Component.text("pas le bon trigger"));
        }

        super.serviceProvider.portal().fill(src, portal, optionalRayTraceResult.get().getSelectedObject());

        // todo
    }

}
