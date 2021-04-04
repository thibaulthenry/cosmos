package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.finders.FinderWorldProperties;
import cosmos.statics.handlers.OutputFormatter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.time.Instant;

public class Import extends AbstractCommand {

    public Import() {
        super(
                Arguments.limitCompleteElement(
                        WorldNameArguments.exportedChoices(ArgKeys.EXPORTED_WORLD)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne(ArgKeys.EXPORTED_WORLD.t)
                .orElseThrow(Outputs.INVALID_EXPORTED_WORLD_CHOICE.asSupplier());

        WorldArchetype worldArchetype = WorldArchetype.builder()
                .enabled(true)
                .generateSpawnOnLoad(true)
                .build(worldName + Instant.now(), worldName);

        WorldProperties worldProperties;
        try {
            worldProperties = Sponge.getServer().createWorldProperties(worldName, worldArchetype);
        } catch (IOException ignored) {
            throw Outputs.IMPORTING_WORLD.asException(worldName);
        }

        if (!worldProperties.getWorldName().equals(worldName)) {
            Sponge.getServer().deleteWorld(worldProperties);
            FinderFile.exportWorld(worldName);
            String levelName = worldProperties.getWorldName();
            Text.Builder textBuilder = Text.of(levelName).toBuilder();
            textBuilder.onHover(TextActions.showText(Text.of("Click to fill the valid world name in chat")));
            textBuilder.onClick(TextActions.suggestCommand(levelName));
            throw Outputs.IMPORTING_MISMATCHED_LEVEL_NAME.asException(OutputFormatter.noFormatCode + worldName, textBuilder);
        }

        FinderWorldProperties.saveProperties(worldProperties);
        src.sendMessage(Outputs.IMPORT_WORLD.asText(worldName));
    }
}
