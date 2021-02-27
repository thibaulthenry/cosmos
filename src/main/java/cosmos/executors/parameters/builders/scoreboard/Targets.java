package cosmos.executors.parameters.builders.scoreboard;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.builders.CosmosFirstOfBuilder;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.List;

public class Targets implements CosmosFirstOfBuilder {

    private final Parameter.FirstOfBuilder builder;
    private Parameter.Key<List<Component>> scoreHoldersKey;

    public Targets() {
        this.builder = Sponge.getGame().getBuilderProvider().provide(Parameter.FirstOfBuilder.class);
        this.scoreHoldersKey = CosmosKeys.MANY_SCORE_HOLDER;
    }

    @Override
    public Parameter build() {
        return this.builder
                .or(CosmosParameters.Builder.ENTITIES.get().setKey(CosmosKeys.ENTITIES).build())
                .or(
                        Parameter.builder(new TypeToken<List<Component>>() {})
                                .parser(new ScoreHolders())
                                .setKey(this.scoreHoldersKey)
                                .build()
                )
                .or(Parameter.formattingCodeText().setKey(CosmosKeys.TEXT_AMPERSAND).build())
                .or(Parameter.jsonText().setKey(CosmosKeys.TEXT_JSON).build())
                .build();
    }

    @Override
    public Targets optional() {
        this.builder.optional();
        return this;
    }

}
