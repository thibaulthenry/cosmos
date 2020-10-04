package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldArguments;
import cosmos.statics.finders.FinderWorld;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class ViewDistance extends AbstractCommand {

    public ViewDistance() {
        super(
                GenericArguments.optionalWeak(
                        Arguments.limitCompleteElement(
                                WorldArguments.loadedChoices(ArgKeys.LOADED_WORLD)
                        )
                ),
                GenericArguments.optional(
                        GenericArguments.integer(ArgKeys.DISTANCE_IN_CHUNKS.t)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        World world = FinderWorld.getGivenWorldOrElse(src, args, ArgKeys.LOADED_WORLD)
                .orElseThrow(Outputs.INVALID_LOADED_WORLD_CHOICE.asSupplier());

        Optional<Integer> optionalViewDistance = args.getOne(ArgKeys.DISTANCE_IN_CHUNKS.t);
        int viewDistance = world.getViewDistance();
        String mutableText = "currently";

        if (optionalViewDistance.isPresent()) {
            viewDistance = optionalViewDistance.get();
            viewDistance = Math.min(Math.max(viewDistance, 3), 32);
            mutableText = "successfully";
            world.setViewDistance(viewDistance);
        }

        src.sendMessage(Outputs.VIEW_DISTANCE.asText(world.getName(), mutableText, viewDistance));
    }
}
