package cosmos.commands.management;

import com.google.common.collect.Iterables;
import cosmos.commands.AbstractCommand;
import cosmos.utils.Permission;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.stream.Collectors;

public class List extends AbstractCommand {

    public List() {
        super(Permission.COMMAND_MANAGEMENT_LIST);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        PaginationList.Builder builder = PaginationList.builder();
        //TODO : modify with Finder package

        Iterable<Text> loadedWorlds = Sponge.getServer().getWorlds().stream()
                .map(world -> world.getProperties().getWorldName())
                .map(this::mapNetherEnd)
                .map(worldName -> Text.builder(worldName)
                        .color(TextColors.GREEN)
                        .onHover(TextActions.showText(Text.of("Click to teleport yourself")))
                        .onClick(TextActions.runCommand("/cm move " + worldName))
                        .build()
                )
                .collect(Collectors.toList());


        Iterable<Text> unloadedWorlds = Sponge.getServer().getUnloadedWorlds().stream()
                .map(WorldProperties::getWorldName)
                .map(this::mapNetherEnd)
                .map(worldName -> Text.builder(worldName)
                        .color(TextColors.RED)
                        .build()
                )
                .collect(Collectors.toList());

        Iterable<Text> allWorlds = Iterables.concat(loadedWorlds, unloadedWorlds);

        if (Iterables.size(allWorlds) != 0) {
            Text title = Text.builder().append(
                    Text.of("Worlds ["),
                    Text.of(TextColors.GREEN, "Loaded"),
                    Text.of(" - "),
                    Text.of(TextColors.RED, "Unloaded"),
                    Text.of("]")).build();

            builder.title(title)
                    .contents(allWorlds)
                    .build()
                    .sendTo(src);
        }

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .permission(permission)
                .executor(this)
                .build();
    }

    private String mapNetherEnd(String worldName) {
        switch (worldName) {
            case "DIM-1":
                return "Nether";
            case "DIM1":
                return "The End";
            default:
                return worldName;
        }
    }
}
