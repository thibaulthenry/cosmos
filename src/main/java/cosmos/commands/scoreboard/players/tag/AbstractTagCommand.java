package cosmos.commands.scoreboard.players.tag;

import cosmos.commands.scoreboard.AbstractMultiTargetCommand;
import cosmos.constants.Outputs;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractTagCommand extends AbstractMultiTargetCommand {

    protected AbstractTagCommand(CommandElement... commandElements) {
        super(commandElements);
    }

    @Override
    protected void runWithTargets(CommandSource src, CommandContext args, String worldName, Collection<Text> targets) throws CommandException {
        World world = Sponge.getServer().getWorld(getWorldProperties().getUniqueId())
                .orElseThrow(Outputs.INVALID_LOADED_WORLD_CHOICE.asSupplier());

        Map<String, Player> worldPlayerMap = world.getPlayers()
                .stream()
                .collect(Collectors.toMap(
                        Player::getName,
                        Function.identity(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map<String, Entity> worldEntityMap = world.getEntities()
                .stream()
                .collect(Collectors.toMap(
                        entity -> entity.getUniqueId().toString(),
                        Function.identity(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map<Text, Entity> entities = new HashMap<>();

        targets.forEach(target -> {
            String targetName = target.toPlain();
            if (isFormatted(target)) {
                entities.put(target, null);
            } else if (worldPlayerMap.containsKey(targetName)) {
                entities.put(target, worldPlayerMap.get(targetName));
            } else {
                entities.put(target, worldEntityMap.getOrDefault(targetName, null));
            }
        });

        runWithEntities(src, args, worldName, entities);
    }

    protected abstract void runWithEntities(CommandSource src, CommandContext args, String worldName, Map<Text, Entity> entities) throws CommandException;

    private boolean isFormatted(Text text) {
        if (text == null) {
            return false;
        }

        return StreamSupport
                .stream(text.withChildren().spliterator(), true)
                .anyMatch(item -> !item.getFormat().isEmpty());
    }
}
