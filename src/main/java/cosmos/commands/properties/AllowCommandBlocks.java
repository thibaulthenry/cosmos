package cosmos.commands.properties;

import cosmos.commands.perworld.GroupRegister;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.PerWorldCommands;
import cosmos.statics.finders.FinderWorldProperties;
import cosmos.statics.handlers.OutputFormatter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllowCommandBlocks extends AbstractPropertyCommand {

    public AllowCommandBlocks() {
        super(
                GenericArguments.optional(
                        GenericArguments.bool(ArgKeys.STATE.t)
                )
        );
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        Optional<Boolean> optionalState = args.getOne(ArgKeys.STATE.t);
        boolean state = worldProperties.areCommandsAllowed();
        String mutableText = "currently";

        if (!optionalState.isPresent()) {
            src.sendMessage(Outputs.COMMAND_BLOCKS.asText(worldProperties, mutableText, state ? "allowed" : "disabled"));
            return;
        }

        state = optionalState.get();
        mutableText = "successfully";

        Collection<WorldProperties> group = GroupRegister.find(Tuple.of(PerWorldCommands.COMMAND_BLOCKS, worldProperties.getWorldName()))
                .orElse(Collections.singleton(worldProperties.getWorldName()))
                .stream()
                .map(worldName -> Sponge.getServer().getWorld(worldName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(World::getProperties)
                .collect(Collectors.toList());

        for (WorldProperties properties : group) {
            properties.setCommandsAllowed(state);
            FinderWorldProperties.saveProperties(properties);
        }

        if (group.size() > 1) {
            Collection<Text> formattedWorlds = group.stream()
                    .map(OutputFormatter::formatOutput)
                    .map(Text::of)
                    .collect(Collectors.toList());

            src.sendMessage(Outputs.COMMAND_BLOCKS_GROUP.asText(Text.joinWith(Text.of(TextColors.GRAY, ", "), formattedWorlds), mutableText, state ? "allowed" : "disabled"));
        } else {
            src.sendMessage(Outputs.COMMAND_BLOCKS.asText(worldProperties, mutableText, state ? "allowed" : "disabled"));
        }

    }
}
