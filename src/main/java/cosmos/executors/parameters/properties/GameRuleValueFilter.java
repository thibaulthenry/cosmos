package cosmos.executors.parameters.properties;

import cosmos.constants.CosmosKeys;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.world.gamerule.GameRule;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

class GameRuleValueFilter implements ValueParameter<Object> {

    private final Parameter.Value<Boolean> booleanParameter = Parameter.bool().key("").build();
    private final Predicate<Object> filter;

    GameRuleValueFilter(final Predicate<Object> filter) {
        this.filter = filter;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return context.one(CosmosKeys.GAME_RULE)
                .map(GameRule::valueType)
                .filter(type -> Boolean.class.equals(type) || boolean.class.equals(type))
                .map(gameRule -> this.booleanParameter.completer().complete(context, currentInput))
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<?> parseValue(final Parameter.Key<? super Object> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        try {
            return Optional.of(reader.parseBoolean()).filter(this.filter);
        } catch (final Exception ignored) {
        }

        try {
            return Optional.of(reader.parseInt()).filter(this.filter);
        } catch (final Exception ignored) {
        }

        try {
            return Optional.of(reader.parseString()).filter(this.filter);
        } catch (final Exception ignored) { }

        return Optional.empty();
    }

}
