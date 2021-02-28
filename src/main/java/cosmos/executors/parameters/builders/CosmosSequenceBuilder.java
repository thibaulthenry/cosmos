package cosmos.executors.parameters.builders;

import org.spongepowered.api.command.parameter.Parameter;

public interface CosmosSequenceBuilder {

    Parameter build();

    CosmosSequenceBuilder optional();

}
