package cosmos.executors.parameters;

import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.CatalogedValueParameters;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.math.vector.Vector2d;
import org.spongepowered.math.vector.Vector3d;

public class CosmosParameters {

    public static final Parameter.Value<Vector2d> POSITION_XZ = Parameter.builder(Vector2d.class, CatalogedValueParameters.VECTOR2D)
            .setKey(CosmosKeys.XZ)
            .build();

    public static final Parameter.Value<Vector3d> POSITION_XYZ = Parameter.vector3d().setKey(CosmosKeys.XYZ).build();

    public static final Parameter.Value<WorldProperties> WORLD_PROPERTIES_ALL_OPTIONAL = Parameter.worldProperties(false)
            .setKey(CosmosKeys.WORLD)
            .optional()
            .build();

}
