package cosmos.executors.parameters.impl.scoreboard;

import com.google.inject.Inject;
import cosmos.Cosmos;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.CosmosFirstOfBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;

public class Extremum implements CosmosFirstOfBuilder {

    private final Parameter.FirstOfBuilder builder;
    private Parameter.Key<Integer> integerKey;

    @Inject
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
