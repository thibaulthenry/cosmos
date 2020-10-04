package cosmos.statics.arguments.implementations;

import cosmos.statics.arguments.implementations.selector.ExactSelectorCommandElement;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ProxySource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.SpongeApiTranslationHelper;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings({"NullableProblems", "ThrowsRuntimeException", "unchecked"})
public class PlayerNameElement extends ExactSelectorCommandElement {

    private final Text worldKey;
    private final boolean returnSource;
    private Collection<String> worldPlayerNames;

    public PlayerNameElement(@Nullable Text key, @Nonnull Text worldKey, boolean returnSource) {
        super(key);
        this.worldKey = worldKey;
        this.returnSource = returnSource;
    }

    private static boolean isEntityIterable(Object object) {
        if (!(object instanceof Iterable)) {
            return false;
        }

        return StreamSupport
                .stream(Objects.requireNonNull((Iterable<?>) object).spliterator(), false)
                .allMatch(item -> item instanceof Entity);
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        worldPlayerNames = FinderWorldProperties.getGivenWorldPropertiesOrElse(source, context, worldKey)
                .flatMap(worldProperties -> Sponge.getServer().getWorld(worldProperties.getUniqueId()))
                .map(World::getPlayers)
                .orElse(Collections.emptyList())
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());

        super.parse(source, args, context);
    }

    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if (!args.hasNext() && returnSource) {
            return tryReturnSource(source, args);
        }

        String arg = args.peek();

        if (arg.startsWith("\"{") || arg.startsWith("{")) {
            return null;
        }

        if ("*".equals(arg)) {
            return Text.of(args.next());
        }

        Object parsedValue;
        CommandArgs.Snapshot state = args.getSnapshot();
        try {
            parsedValue = super.parseValue(source, args);
        } catch (ArgumentParseException ex) {
            throw ex;
        }

        if (!isEntityIterable(parsedValue)) {
            return null;
        }

        Iterable<Entity> entities = (Iterable<Entity>) parsedValue;
        return StreamSupport
                .stream(Objects.requireNonNull(entities).spliterator(), false)
                .map(entity -> (entity instanceof Player) ?
                        ((Tamer) entity).getName() :
                        entity.getUniqueId().toString())
                .map(Text::of)
                .collect(Collectors.toSet());
    }

    @Override
    protected Iterable<String> getChoices() {
        return worldPlayerNames == null ? Collections.emptyList() : worldPlayerNames;
    }

    @Override
    protected Iterable<String> getOnCompleteChoices() {
        return getChoices();
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        return Sponge.getGame().getServer().getPlayer(choice)
                .orElseThrow(() -> new IllegalArgumentException("Input value " + choice + " was not a player"));
    }

    private static Text tryReturnSource(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String name;

        if (source instanceof Tamer) {
            name = (source instanceof Player) ? ((Tamer) source).getName() : ((Identifiable) source).getUniqueId().toString();
        } else if (source instanceof ProxySource && ((ProxySource) source).getOriginalSource() instanceof Tamer) {
            name = ((Tamer) ((ProxySource) source).getOriginalSource()).getName();
        } else {
            throw args.createError(SpongeApiTranslationHelper.t("No entities matched and source was not an entity !"));
        }

        return Text.of(name);
    }

    @Override
    public Text getUsage(CommandSource src) {
        return src instanceof Player && returnSource ? Text.of("[", super.getUsage(src), "]") : super.getUsage(src);
    }
}
