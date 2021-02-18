package cosmos.executors.parameters.time;

import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosSequenceBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

import java.time.temporal.ChronoUnit;

public class DurationWithUnit implements CosmosSequenceBuilder {

    private static final ValueParameter<ChronoUnit> TIME_UNIT = VariableValueParameters.staticChoicesBuilder(ChronoUnit.class)
            .addChoice("milliseconds", ChronoUnit.MILLIS)
            .addChoice("seconds", ChronoUnit.SECONDS)
            .addChoice("minutes", ChronoUnit.MINUTES)
            .addChoice("hours", ChronoUnit.HOURS)
            .addChoice("days", ChronoUnit.DAYS)
            .addChoice("weeks", ChronoUnit.WEEKS)
            .addChoice("months", ChronoUnit.MONTHS)
            .addChoice("years", ChronoUnit.YEARS)
            .build();

    private final Parameter.SequenceBuilder builder;
    private final Parameter.Key<ChronoUnit> timeUnitKey;

    private Parameter.Key<Long> durationKey;

    public DurationWithUnit() {
        this.builder = Sponge.game().builderProvider().provide(Parameter.SequenceBuilder.class);
        this.durationKey = CosmosKeys.DURATION;
        this.timeUnitKey = CosmosKeys.TIME_UNIT;
    }

    @Override
    public Parameter build() {
        return this.builder
                .then(Parameter.longNumber().key(this.durationKey).build())
                .then(
                        Parameter.builder(ChronoUnit.class)
                                .addParser(DurationWithUnit.TIME_UNIT)
                                .key(this.timeUnitKey)
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

}
