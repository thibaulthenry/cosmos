package cosmos.executors.parameters.scoreboard;

import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.parameters.CosmosFirstOfBuilder;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.Entity;

import java.util.List;

public class Targets implements CosmosFirstOfBuilder {

    private final Parameter.FirstOfBuilder builder;

    private Parameter.Key<List<Entity>> entitiesKey;
    private Parameter.Key<List<Component>> scoreHoldersKey;
    private Parameter.Key<Component> textAmpersandKey;
    private Parameter.Key<Component> textJsonKey;

    public Targets() {
        this.builder = Sponge.game().builderProvider().provide(Parameter.FirstOfBuilder.class);
        this.entitiesKey = CosmosKeys.ENTITIES;
        this.scoreHoldersKey = CosmosKeys.MANY_SCORE_HOLDER;
        this.textAmpersandKey = CosmosKeys.TEXT_AMPERSAND;
        this.textJsonKey = CosmosKeys.TEXT_JSON;
    }

    @Override
    public Parameter build() {
        return this.builder
                .or(CosmosParameters.ENTITIES.get().key(this.entitiesKey).build())
                .or(
                        Parameter.builder(new TypeToken<List<Component>>() {})
                                .addParser(new ScoreHolders())
                                .key(this.scoreHoldersKey)
                                .build()
                )
                .or(Parameter.formattingCodeText().key(this.textAmpersandKey).build())
                .or(Parameter.jsonText().key(this.textJsonKey).build())
                .build();
    }

    public Targets entitiesKey(final Parameter.Key<List<Entity>> entitiesKey) {
        this.entitiesKey = entitiesKey;
        return this;
    }

    public Targets entitiesKey(final String entitiesKey) {
        return this.entitiesKey(Parameter.key(entitiesKey, new TypeToken<List<Entity>>() {}));
    }

    @Override
    public Targets optional() {
        this.builder.optional();
        return this;
    }

    public Targets scoreHoldersKey(final Parameter.Key<List<Component>> scoreHoldersKey) {
        this.scoreHoldersKey = scoreHoldersKey;
        return this;
    }

    public Targets scoreHoldersKey(final String scoreHoldersKey) {
        return this.scoreHoldersKey(Parameter.key(scoreHoldersKey, new TypeToken<List<Component>>() {}));
    }

    public Targets textAmpersandKey(final Parameter.Key<Component> textAmpersandKey) {
        this.textAmpersandKey = textAmpersandKey;
        return this;
    }

    public Targets textAmpersandKey(final String textAmpersandKey) {
        return this.textAmpersandKey(Parameter.key(textAmpersandKey, TypeToken.get(Component.class)));
    }

    public Targets textJsonKey(final Parameter.Key<Component> textJsonKey) {
        this.textJsonKey = textJsonKey;
        return this;
    }

    public Targets textJsonKey(final String textJsonKey) {
        return this.textJsonKey(Parameter.key(textJsonKey, TypeToken.get(Component.class)));
    }

}
