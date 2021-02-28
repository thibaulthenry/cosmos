package cosmos.executors.parameters.builders.time;

import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.builders.CosmosSequenceBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

import java.time.temporal.ChronoUnit;

public class DurationWithUnit implements CosmosSequenceBuilder {

    private static final ValueParameter<ChronoUnit> TIME_UNIT = VariableValueParameters.staticChoicesBuilder(ChronoUnit.class)
            .choice("milliseconds", ChronoUnit.MILLIS)
            .choice("seconds", ChronoUnit.SECONDS)
            .choice("minutes", ChronoUnit.MINUTES)
            .choice("hours", ChronoUnit.HOURS)
            .choice("days", ChronoUnit.DAYS)
            .choice("weeks", ChronoUnit.WEEKS)
            .choice("months", ChronoUnit.MONTHS)
            .choice("years", ChronoUnit.YEARS)
            .build();

    private final Parameter.SequenceBuilder builder;
    private Parameter.Key<Long> durationKey;
    private Parameter.Key<ChronoUnit> timeUnitKey;

    public DurationWithUnit() {
        this.builder = Sponge.getGame().getBuilderProvider().provide(Parameter.SequenceBuilder.class);
        this.durationKey = CosmosKeys.DURATION;
        this.timeUnitKey = CosmosKeys.TIME_UNIT;
    }

    @Override
    public Parameter build() {
        return this.builder
                .then(Parameter.longNumber().setKey(this.durationKey).build())
                .then(
                        Parameter.builder(ChronoUnit.class)
                                .setKey(this.timeUnitKey)
                                .parser(DurationWithUnit.TIME_UNIT)
                                .optional()
                                .build()
                )
                .build();
    }

    public DurationWithUnit durationKey(final Parameter.Key<Long> durationKey) {
        this.durationKey = durationKey;
        return this;
    }

    @Override
    public DurationWithUnit optional() {
        this.builder.optional();
        return this;
    }

    public DurationWithUnit timeUnitKey(final Parameter.Key<ChronoUnit> timeUnitKey) {
        this.timeUnitKey = timeUnitKey;
        return this;
    }

}
