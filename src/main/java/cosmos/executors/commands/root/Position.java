package cosmos.executors.commands.root;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class Position extends AbstractCommand {

    public Position() {
        super(
                CosmosParameters.ENTITIES.get().key(CosmosKeys.ENTITIES).optional().build(),
                Parameter.player().key(CosmosKeys.MESSAGE_RECEIVER).optional().build()
        );
    }

    @Override
    protected List<String> additionalAliases() {
        return Collections.singletonList("pos");
    }

    private TextComponent getPositionText(final Entity target, final ServerPlayer messageReceiver) {
        final ServerLocation targetLocation = target.serverLocation();
        final String targetName = target instanceof Tamer ? ((Tamer) target).name() : target.uniqueId().toString();
        final boolean targetIsNotReceiver = !super.serviceProvider.validation().isSelf((Audience) messageReceiver, target);

        return super.serviceProvider.message()
                .getMessage(messageReceiver, "success.root.position")
                .replace("position", targetLocation)
                .replace("target", targetName)
                .replace("world", targetLocation.world())
                .condition("target", targetIsNotReceiver)
                .condition("verb", targetIsNotReceiver)
                .green()
                .asText();
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final Optional<ServerPlayer> optionalPlayer = context.one(CosmosKeys.MESSAGE_RECEIVER);

        if (!(optionalPlayer.isPresent() || src instanceof ServerPlayer)) {
            throw super.serviceProvider.message().getError(src, "error.missing.player.any");
        }

        final ServerPlayer player = optionalPlayer.orElse((ServerPlayer) src);

        final Collection<Component> contents = context.one(CosmosKeys.ENTITIES)
                .orElse(Collections.singletonList((ServerPlayer) src))
                .stream()
                .map(target -> this.getPositionText(target, player))
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, "success.root.position.header")
                .replace("number", contents.size())
                .gray()
                .asText();

        super.serviceProvider.pagination().send(player, title, contents, true);
    }

}
