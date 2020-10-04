package cosmos.commands.properties;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class GenerateSpawnOnLoad extends AbstractPropertyCommand {

    public GenerateSpawnOnLoad() {
        super(
                GenericArguments.optional(
                        GenericArguments.bool(ArgKeys.STATE.t)
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<Boolean> optionalState = args.getOne(ArgKeys.STATE.t);
        boolean state = worldProperties.doesGenerateSpawnOnLoad();
        String mutableText = "currently";

        if (optionalState.isPresent()) {
            state = optionalState.get();
            mutableText = "successfully";
            worldProperties.setGenerateSpawnOnLoad(state);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(
                Outputs.GENERATE_SPAWN_ON_LOAD.asText(
                        worldProperties,
                        mutableText,
                        state ? "activated" : "disabled"
                )
        );
    }
}
