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

public class GeneratorType extends AbstractPropertyCommand {

    public GeneratorType() {
        super(
                GenericArguments.optional(
                        Arguments.enhancedCatalogedElement(
                                ArgKeys.VALUE,
                                org.spongepowered.api.world.GeneratorType.class
                        )
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<org.spongepowered.api.world.GeneratorType> optionalGeneratorType = args.getOne(ArgKeys.VALUE.t);
        org.spongepowered.api.world.GeneratorType generatorType = worldProperties.getGeneratorType();
        String mutableText = "currently";

        if (optionalGeneratorType.isPresent()) {
            generatorType = optionalGeneratorType.get();
            mutableText = "successfully";
            worldProperties.setGeneratorType(generatorType);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.GENERATOR_TYPE.asText(worldProperties, mutableText, generatorType));

        if (optionalGeneratorType.isPresent()) {
            src.sendMessage(Outputs.GENERATOR_TYPE_TIP.asText(worldProperties));
        }
    }
}
