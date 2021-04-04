package cosmos.statics.arguments.implementations;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import cosmos.statics.arguments.implementations.selector.ExactMatchingCommandElement;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.SpongeApiTranslationHelper;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class WorldDistinctElement extends ExactMatchingCommandElement {

    private static final Text nullKeyArg = SpongeApiTranslationHelper.t("argument");

    public WorldDistinctElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        Iterable<String> choices = getChoices();
        String arg = args.next();

        Optional<Object> exactMatch = getExactMatch(choices, arg);
        if (exactMatch.isPresent()) {
            return Collections.singleton(exactMatch.get());
        }

        throw args.createError(SpongeApiTranslationHelper.t("No values matching pattern '%s' present for %s!", arg, getKey() == null
                ? nullKeyArg : getKey()));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        Iterable<String> choices = getOnCompleteChoices();
        Optional<String> nextArg = args.nextIfPresent();

        if (nextArg.isPresent()) {
            String arg = nextArg.get();
            choices = StreamSupport.stream(choices.spliterator(), false)
                    .filter(input -> input.regionMatches(true, 0, arg, 0, arg.length()))
                    .filter(input -> !context.<String>getAll(super.getKey()).contains(input))
                    .collect(Collectors.toList());
        }

        return ImmutableList.copyOf(choices);
    }

    private Optional<Object> getExactMatch(Iterable<String> choices, String potentialChoice) {
        return Iterables.tryFind(choices, potentialChoice::equalsIgnoreCase).toJavaUtil().map(this::getValue);
    }

    @Override
    protected Iterable<String> getChoices() {
        return Sponge.getServer().getWorlds().stream().map(World::getName).collect(Collectors.toSet());
    }

    @Override
    protected Iterable<String> getOnCompleteChoices() {
        return getChoices();
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        Optional<String> ret = Sponge.getServer().getWorld(choice).map(World::getName);

        if (!ret.isPresent()) {
            throw new IllegalArgumentException("Invalid input " + choice + " was found");
        }

        return ret.get();
    }

}
