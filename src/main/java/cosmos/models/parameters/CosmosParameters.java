package cosmos.models.parameters;

import cosmos.models.enums.Operands;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.CatalogedValueParameters;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gen.GeneratorModifierType;
import org.spongepowered.math.vector.Vector2d;

import java.time.temporal.ChronoUnit;

public class CosmosParameters {

    public static final Parameter.Value<Difficulty> DIFFICULTY_OPTIONAL = Parameter.catalogedElementWithMinecraftAndSpongeDefaults(Difficulty.class)
            .setKey(CosmosKeys.DIFFICULTY)
            .optional()
            .build();

    public static final Parameter.Value<GameMode> GAME_MODE_OPTIONAL = Parameter.catalogedElementWithMinecraftAndSpongeDefaults(GameMode.class)
            .setKey(CosmosKeys.GAME_MODE)
            .optional()
            .build();

    public static final Parameter.Value<GeneratorModifierType> GENERATOR_MODIFIER_TYPE_OPTIONAL = Parameter.catalogedElementWithMinecraftAndSpongeDefaults(GeneratorModifierType.class)
            .setKey(CosmosKeys.GENERATOR_MODIFIER)
            .optional()
            .build();

    public static final Parameter.Value<Vector2d> POSITION_2D_OPTIONAL = Parameter.builder(Vector2d.class, CatalogedValueParameters.VECTOR2D)
            .setKey(CosmosKeys.XZ)
            .optional()
            .build();

    public static final Parameter.Value<Operands> STANDARD_OPERAND = Parameter.builder(Operands.class, CosmosValueParameters.STANDARD_OPERANDS)
            .setKey(CosmosKeys.OPERAND)
            .build();

    public static final Parameter.Value<ChronoUnit> TIME_UNIT_OPTIONAL = Parameter.builder(ChronoUnit.class, CosmosValueParameters.TIME_UNIT)
            .setKey(CosmosKeys.TIME_UNIT)
            .optional()
            .build();
}
