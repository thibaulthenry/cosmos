package cosmos.executors.commands.perworld;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.PerWorldFeatures;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Optional;

@Singleton
public class Bypass extends AbstractPerWorldCommand {

    public Bypass() {
        super(
                Parameter.player().optional().key("player").build(),
                Parameter.bool().key(CosmosKeys.STATE).optional().build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final PerWorldFeatures feature) throws CommandException {
        if (feature.equals(PerWorldFeatures.COMMAND_BLOCK)) {
            throw super.serviceProvider.message().getError(src, "error.invalid.value"); // todo
        }

        if (!super.serviceProvider.listener().isRegisteredToSponge(feature.listenerClass())) {
            throw super.serviceProvider.message().getError(src, "error.invalid.value"); // todo
        }

        final Optional<ServerPlayer> optionalPlayer = context.one(Parameter.key("player", ServerPlayer.class));

        if (!(optionalPlayer.isPresent() || src instanceof ServerPlayer)) {
            throw super.serviceProvider.message().getError(src, "error.invalid.value"); // todo
        }

        final ServerPlayer player = optionalPlayer.orElse((ServerPlayer) src);

        if (context.one(CosmosKeys.STATE).orElse(true)) {
            if (super.serviceProvider.registry().bypass().doesBypass(feature, player)) {
                // todo src.sendMessage(Outputs.BYPASSING_PER_WORLD_LISTENER_START.asText(player.getName(), perWorldCommands.getName()));
            } else {
                super.serviceProvider.registry().bypass().add(feature, player);
                super.serviceProvider.listener().toggle(feature.listenerClass(), false, player);
                // todo src.sendMessage(Outputs.BYPASS_PER_WORLD_LISTENER_START.asText(player.getName(), perWorldCommands.getName()));
            }
        } else {
            if (super.serviceProvider.registry().bypass().doesBypass(feature, player)) {
                super.serviceProvider.registry().bypass().remove(feature, player);
                super.serviceProvider.listener().toggle(feature.listenerClass(), true, player);
               // todo src.sendMessage(Outputs.BYPASS_PER_WORLD_LISTENER_STOP.asText(player.getName(), perWorldCommands.getName()));
            } else {
               // todo src.sendMessage(Outputs.BYPASSING_PER_WORLD_LISTENER_STOP.asText(player.getName(), perWorldCommands.getName()));
            }
        }
    }

}
