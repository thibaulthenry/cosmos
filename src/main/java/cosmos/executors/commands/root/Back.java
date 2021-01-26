package cosmos.executors.commands.root;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.DataKeys;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.world.WorldOnline;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.Axis;

@Singleton
public class Back extends AbstractCommand {

    @Inject
    public Back(final Injector injector) {
        super(injector.getInstance(WorldOnline.class).build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final ResourceKey worldKey = context.getOne(CosmosKeys.WORLD)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.world.online"));

        //super.serviceProvider.data().portal().create(DataKeys.PORTAL_TYPE_WARP, ((ServerPlayer) src).getServerLocation(), Axis.X);
    }

}
