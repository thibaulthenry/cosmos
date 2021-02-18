package cosmos.executors.parameters.properties;

import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class GameRuleValueAll implements CosmosBuilder<Object> {

    private final Parameter.Value.Builder<Object> builder;

    public GameRuleValueAll() {
        final ValueParameter<Object> value = new GameRuleValueFilter(gameRuleValue -> true);
        this.builder = Parameter.builder(Object.class, value);
        this.builder.key(CosmosKeys.GAME_RULE_VALUE);
    }

    @Override
    public Parameter.Value<Object> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<Object> key(final Parameter.Key<Object> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<Object> key(final String key) {
        return this.key(Parameter.key(key, Object.class));
    }

    @Override
    public CosmosBuilder<Object> optional() {
        this.builder.optional();
        return this;
    }

}
