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

public class Rules extends AbstractPropertyCommand {

    public Rules() {
        super(
                Arguments.gameRuleChoices(ArgKeys.GAME_RULE),
                GenericArguments.optional(
                        GenericArguments.string(ArgKeys.VALUE.t)
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        String gameRule = args.<String>getOne(ArgKeys.GAME_RULE.t).orElseThrow(Outputs.INVALID_GAME_RULE.asSupplier());
        Optional<String> optionalValue = args.getOne(ArgKeys.VALUE.t);
        String value = worldProperties.getGameRule(gameRule).orElseThrow(Outputs.INVALID_VALUE.asSupplier());
        String mutableText = "currently";

        if (optionalValue.isPresent()) {
            value = optionalValue.get();
            mutableText = "successfully";
            worldProperties.setGameRule(gameRule, value);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.GAME_RULES.asText(worldProperties, gameRule, mutableText, value));
    }
}
