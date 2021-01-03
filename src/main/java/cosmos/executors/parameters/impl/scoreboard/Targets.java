package cosmos.executors.parameters.impl.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.CosmosComplex;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.parameter.Parameter;

@Singleton
public class Targets implements CosmosComplex {

    private final Parameter parameter;

    @Inject
    public Targets(final Injector injector) {
//        final Parameter.Value<Component> worldPlayerNames = Parameter.builder(Component.class)
//                .parser(injector.getInstance(PlayerNames.class))
//                .setKey(CosmosKeys.PLAYER_NAME)
//                .build();
        final Parameter.Value<Component> worldScoreboardTracked = Parameter.builder(Component.class)
                .parser(injector.getInstance(TrackedNames.class))
                .setKey(CosmosKeys.TRACKED)
                .build();
        this.parameter = Parameter.firstOf(
                //worldPlayerNames,
                worldScoreboardTracked,
                CosmosParameters.TEXT_AMPERSAND,
                CosmosParameters.TEXT_JSON
        );
    }

    @Override
    public Parameter get() {
        return this.parameter;
    }
}
