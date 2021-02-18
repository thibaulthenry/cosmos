package cosmos.executors.parameters.scoreboard;

import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosFirstOfBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;

public class Extremum implements CosmosFirstOfBuilder {

    private final Parameter.FirstOfBuilder builder;
    private Parameter.Key<Integer> integerKey;

    public Extremum() {
        this.builder = Sponge.game().builderProvider().provide(Parameter.FirstOfBuilder.class);
        this.integerKey = CosmosKeys.MAX;
    }

    @Override
    public Parameter build() {
        return this.builder
                .or(Parameter.integerNumber().key(this.integerKey).build())
                .or(Parameter.choices("*").key(CosmosKeys.WILDCARD).build())
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
