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

public class KeepSpawnLoaded extends AbstractPropertyCommand {

    public KeepSpawnLoaded() {
        super(
                GenericArguments.optional(
                        GenericArguments.bool(ArgKeys.STATE.t)
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<Boolean> optionalState = args.getOne(ArgKeys.STATE.t);
        boolean state = worldProperties.doesKeepSpawnLoaded();
        String mutableText = "currently";

        if (optionalState.isPresent()) {
            state = optionalState.get();
            mutableText = "successfully";
            worldProperties.setKeepSpawnLoaded(state);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(
                Outputs.KEEP_SPAWN_LOADED.asText(
                        worldProperties,
                        mutableText,
                        state ? "activated" : "disabled"
                )
        );

        if (optionalState.isPresent() && !optionalState.get()) {
            src.sendMessage(Outputs.KEEP_SPAWN_LOADED_TIP.asText(worldProperties));
        }
    }
}
