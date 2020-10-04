package cosmos.statics.arguments.implementations.selector;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.source.ProxySource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.SpongeApiTranslationHelper;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("NullableProblems")
public class PlayerCommandElement extends ExactSelectorCommandElement {

    private final boolean returnSource;

    public PlayerCommandElement(Text key, boolean returnSource) {
        super(key);
        this.returnSource = returnSource;
    }

    private static Player tryReturnSource(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if (source instanceof Player) {
            return ((Player) source);
        } else if (source instanceof ProxySource && ((ProxySource) source).getOriginalSource() instanceof Player) {
            return (Player) ((ProxySource) source).getOriginalSource();
        } else {
            throw args.createError(SpongeApiTranslationHelper.t("No players matched and source was not a player!"));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if (!args.hasNext() && returnSource) {
            return tryReturnSource(source, args);
        }

        CommandArgs.Snapshot state = args.getSnapshot();
        try {
            Iterable<Entity> entities = (Iterable<Entity>) super.parseValue(source, args);

            if (entities == null) {
                if (returnSource) {
                    args.applySnapshot(state);
                    return tryReturnSource(source, args);
                }
                throw args.createError(SpongeApiTranslationHelper.t("No players matched and source was not a player!"));
            }

            return StreamSupport
                    .stream(entities.spliterator(), false)
                    .filter(e -> e instanceof Player)
                    .collect(Collectors.toList());
        } catch (ArgumentParseException ex) {
            if (returnSource) {
                args.applySnapshot(state);
                return tryReturnSource(source, args);
            }
            throw ex;
        }
    }

    @Override
    protected Iterable<String> getChoices() {
        return Sponge.getGame().getServer().getOnlinePlayers().stream()
                .filter(Objects::nonNull)
                .map(User::getName)
                .collect(Collectors.toList());
    }

    @Override
    protected Iterable<String> getOnCompleteChoices() {
        return getChoices();
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        Optional<Player> ret = Sponge.getGame().getServer().getPlayer(choice);
        if (!ret.isPresent()) {
            throw new IllegalArgumentException("Input value " + choice + " was not a player");
        }
        return ret.get();
    }

    @Override
    public Text getUsage(CommandSource src) {
        return src instanceof Player && returnSource ? Text.of("[", super.getUsage(src), "]") : super.getUsage(src);
    }
}
