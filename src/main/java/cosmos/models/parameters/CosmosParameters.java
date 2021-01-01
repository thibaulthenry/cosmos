package cosmos.models.parameters;

import cosmos.models.enums.Operands;
import io.leangen.geantyref.TypeToken;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.dimension.DimensionType;
import org.spongepowered.math.vector.Vector2d;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class CosmosParameters {

    public static final Parameter.Value<Difficulty> DIFFICULTY_OPTIONAL = Parameter.registryElement(TypeToken.get(Difficulty.class), RegistryTypes.DIFFICULTY)
            .setKey(CosmosKeys.DIFFICULTY)
            .optional()
            .build();

    public static final Parameter.Value<DimensionType> DIMENSION_OPTIONAL = Parameter.registryElement(TypeToken.get(DimensionType.class), RegistryTypes.DIMENSION_TYPE)
            .setKey(CosmosKeys.DIMENSION)
            .optional()
            .build();

    public static final Parameter.Value<List<Entity>> ENTITIES = Parameter.builder(new TypeToken<List<Entity>>() {
    }, ResourceKeyedValueParameters.MANY_ENTITIES)
            .setKey(CosmosKeys.ENTITIES)
            .orDefault(cause -> cause.getCause().root() instanceof Entity ? Collections.singletonList((Entity) cause.getCause().root()) : null)
            .build();

    public static final Parameter.Value<GameMode> GAME_MODE_OPTIONAL = Parameter.registryElement(TypeToken.get(GameMode.class), RegistryTypes.GAME_MODE)
            .setKey(CosmosKeys.GAME_MODE)
            .optional()
            .build();

    /*
    public static final Parameter.Value<GeneratorModifierType> GENERATOR_MODIFIER_TYPE_OPTIONAL = Parameter.catalogedElementWithMinecraftAndSpongeDefaults(GeneratorModifierType.class)
            .setKey(CosmosKeys.GENERATOR_MODIFIER)
            .optional()
            .build();
    */

    public static final Parameter.Value<Vector2d> POSITION_2D_OPTIONAL = Parameter.builder(Vector2d.class, ResourceKeyedValueParameters.VECTOR2D)
            .setKey(CosmosKeys.XZ)
            .optional()
            .build();

    public static final Parameter.Value<Operands> STANDARD_OPERAND = Parameter.builder(Operands.class, CosmosValueParameters.STANDARD_OPERANDS)
            .setKey(CosmosKeys.OPERAND)
            .build();

    private static final Parameter.Value<ChronoUnit> TIME_UNIT_OPTIONAL = Parameter.builder(ChronoUnit.class, CosmosValueParameters.TIME_UNIT)
            .setKey(CosmosKeys.TIME_UNIT)
            .optional()
            .build();

    public static final Parameter DURATION_WITH_TIME_UNIT_OPTIONAL = Parameter.seqBuilder(Parameter.longNumber().setKey(CosmosKeys.DURATION).build())
            .then(TIME_UNIT_OPTIONAL)
            .optional()
            .build();
}
