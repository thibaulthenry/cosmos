package cosmos.executors.parameters;

import org.spongepowered.api.command.parameter.Parameter;

public interface CosmosSequenceBuilder {

    Parameter build();

    CosmosSequenceBuilder optional();

}
