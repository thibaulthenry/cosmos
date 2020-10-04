package cosmos.commands.properties;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class Difficulty extends AbstractPropertyCommand {

    public Difficulty() {
        super(
                GenericArguments.optional(
                        Arguments.enhancedCatalogedElement(
                                ArgKeys.LEVEL,
                                org.spongepowered.api.world.difficulty.Difficulty.class
                        )
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<org.spongepowered.api.world.difficulty.Difficulty> optionalDifficulty = args.getOne(ArgKeys.LEVEL.t);
        org.spongepowered.api.world.difficulty.Difficulty difficulty = worldProperties.getDifficulty();
        String mutableText = "currently";

        if (optionalDifficulty.isPresent()) {
            difficulty = optionalDifficulty.get();
            mutableText = "successfully";
            worldProperties.setDifficulty(difficulty);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.DIFFICULTY.asText(worldProperties, mutableText, difficulty));
    }
}
