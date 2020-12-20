package cosmos.models.parameters;

import cosmos.models.enums.Operands;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gen.GeneratorModifierType;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.math.vector.Vector2d;
import org.spongepowered.math.vector.Vector3d;

import java.time.temporal.ChronoUnit;

public class CosmosKeys {

    public static final Parameter.Key<Double> AMOUNT = Parameter.key("amount", Double.class);
    public static final Parameter.Key<Integer> CHUNKS = Parameter.key("chunks", Integer.class);
    public static final Parameter.Key<Double> DIAMETER = Parameter.key("end-diameter", Double.class);
    public static final Parameter.Key<Difficulty> DIFFICULTY = Parameter.key("difficulty", Difficulty.class);
    public static final Parameter.Key<Double> DISTANCE = Parameter.key("distance", Double.class);
    public static final Parameter.Key<Long> DURATION = Parameter.key("duration", Long.class);
    public static final Parameter.Key<Double> END_DIAMETER = Parameter.key("end-diameter", Double.class);
    public static final Parameter.Key<GameMode> GAME_MODE = Parameter.key("game-mode", GameMode.class);
    public static final Parameter.Key<GeneratorModifierType> GENERATOR_MODIFIER = Parameter.key("generator-modifier", GeneratorModifierType.class);
    public static final Parameter.Key<Operands> OPERAND = Parameter.key("operand", Operands.class);
    public static final Parameter.Key<Double> START_DIAMETER = Parameter.key("start-diameter", Double.class);
    public static final Parameter.Key<Boolean> STATE = Parameter.key("state", Boolean.class);
    public static final Parameter.Key<ChronoUnit> TIME_UNIT = Parameter.key("unit", ChronoUnit.class);
    public static final Parameter.Key<Vector2d> XZ = Parameter.key("x> <z", Vector2d.class);
    public static final Parameter.Key<Vector3d> XYZ = Parameter.key("x> <y> <z", Vector3d.class);
    public static final Parameter.Key<WorldProperties> WORLD = Parameter.key("world", WorldProperties.class);

}