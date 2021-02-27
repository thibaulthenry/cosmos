package cosmos.executors.parameters.builders.scoreboard;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.builders.CosmosFirstOfBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;

public class Extremum implements CosmosFirstOfBuilder {

    private final Parameter.FirstOfBuilder builder;
    private Parameter.Key<Integer> integerKey;

    public Extremum() {
        this.builder = Sponge.getGame().getBuilderProvider().provide(Parameter.FirstOfBuilder.class);
        this.integerKey = CosmosKeys.MAX;
    }

    @Override
    public Parameter build() {
        return this.builder
                .or(Parameter.integerNumber().setKey(this.integerKey).build())
                .or(Parameter.choices("*").setKey(CosmosKeys.WILDCARD).build())
                .build();
    }

    public Extremum integerKey(final Parameter.Key<Integer> integerKey) {
        this.integerKey = integerKey;
        return this;
    }

    @Override
    public Extremum optional() {
        this.builder.optional();
        return this;
    }

}
