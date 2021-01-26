package cosmos.executors.parameters.impl.scoreboard;

import com.google.inject.Inject;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.CosmosFirstOfBuilder;
import cosmos.services.ServiceProvider;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.List;

public class Targets implements CosmosFirstOfBuilder {

    private final Parameter.FirstOfBuilder builder;
    private final ServiceProvider serviceProvider;
    private Parameter.Key<List<Component>> scoreHoldersKey;

    @Inject
    public Targets(final ServiceProvider serviceProvider) {
        this.builder = Sponge.getGame().getBuilderProvider().provide(Parameter.FirstOfBuilder.class);
        this.scoreHoldersKey = CosmosKeys.MANY_SCORE_HOLDER;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public Parameter build() {
        return this.builder
                .or(CosmosParameters.ENTITY_TARGETS)
                .or(
                        Parameter.builder(new TypeToken<List<Component>>() {})
                                .parser(new ScoreHolders(this.serviceProvider))
                                .setKey(this.scoreHoldersKey)
                                .build()
                )
                .or(CosmosParameters.TEXT_AMPERSAND)
                .or(CosmosParameters.TEXT_JSON)
                .build();
    }

    @Override
    public Targets optional() {
        this.builder.optional();
        return this;
    }

}
