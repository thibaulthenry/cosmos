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

public class Seed extends AbstractPropertyCommand {

    public Seed() {
        super(
                GenericArguments.optional(
                        GenericArguments.longNum(ArgKeys.SEED.t)
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<Long> optionalSeed = args.getOne(ArgKeys.SEED.t);
        long seed = worldProperties.getSeed();
        String mutableText = "currently";

        if (optionalSeed.isPresent()) {
            seed = optionalSeed.get();
            mutableText = "successfully";
            worldProperties.setSeed(seed);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.SEED.asText(worldProperties, mutableText, seed));

        if (optionalSeed.isPresent()) {
            src.sendMessage(Outputs.SEED_TIP.asText(worldProperties));
        }
    }
}
