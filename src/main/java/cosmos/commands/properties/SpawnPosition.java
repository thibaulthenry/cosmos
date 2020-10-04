package cosmos.commands.properties;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class SpawnPosition extends AbstractPropertyCommand {

    public SpawnPosition() {
        super(
                GenericArguments.optional(
                        GenericArguments.vector3d(ArgKeys.POSITION_XYZ.t)
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<Vector3d> optionalSpawnPosition = args.getOne(ArgKeys.POSITION_XYZ.t);
        Vector3i spawnPosition = worldProperties.getSpawnPosition();
        String mutableText = "currently";

        if (optionalSpawnPosition.isPresent()) {
            spawnPosition = optionalSpawnPosition.get().toInt();
            mutableText = "successfully";
            worldProperties.setSpawnPosition(spawnPosition);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.SPAWN_POSITION.asText(worldProperties, mutableText, spawnPosition));
    }
}
