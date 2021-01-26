package cosmos.executors.parameters.impl.gamerule;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.world.gamerule.GameRule;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public class GameRuleValue implements ValueParameter<Object> {

    private final Parameter.Value<Boolean> booleanParameter = Parameter.bool().setKey("").build();

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return context.getOne(CosmosKeys.GAME_RULE)
                .map(GameRule::getValueType)
                .filter(type -> Boolean.class.equals(type) || boolean.class.equals(type))
                .map(gameRule -> this.booleanParameter.getCompleter().complete(context, currentInput))
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<?> getValue(final Parameter.Key<? super Object> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        try {
            return Optional.of(reader.parseBoolean());
        } catch (final Exception ignored) { }

        try {
            return Optional.of(reader.parseInt());
        } catch (final Exception ignored) { }

        try {
            return Optional.of(reader.parseString());
        } catch (final Exception ignored) { }

        return Optional.empty();
    }
}
