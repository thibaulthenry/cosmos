package cosmos.executors.parameters.impl;

import org.spongepowered.api.command.parameter.Parameter;

public interface CosmosBuilder<T> {

    Parameter.Value<T> build();

    CosmosBuilder<T> key(Parameter.Key<T> key);

    CosmosBuilder<T> key(String key);

    CosmosBuilder<T> optional();

}
