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

public class EnableStructures extends AbstractPropertyCommand {

    public EnableStructures() {
        super(
                GenericArguments.optional(
                        GenericArguments.bool(ArgKeys.STATE.t)
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<Boolean> optionalState = args.getOne(ArgKeys.STATE.t);
        boolean state = worldProperties.usesMapFeatures();
        String mutableText = "currently";

        if (optionalState.isPresent()) {
            state = optionalState.get();
            mutableText = "successfully";
            worldProperties.setMapFeaturesEnabled(state);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.STRUCTURES.asText(worldProperties, mutableText, state ? "enabled" : "disabled"));

        if (optionalState.isPresent()) {
            src.sendMessage(Outputs.STRUCTURES_TIP.asText(worldProperties));
        }
    }
}
