package cosmos.executors.parameters.impl;

import org.spongepowered.api.command.parameter.Parameter;

public interface CosmosBuilder<T> {

    Parameter.Value.Builder<T> builder();

}
