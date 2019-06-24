package cosmos.commands;

import cosmos.commands.management.*;
import cosmos.commands.management.properties.Basics;
import cosmos.commands.management.properties.Border;
import cosmos.commands.management.properties.Rules;
import cosmos.commands.management.properties.Weather;
import cosmos.commands.restoration.Backup;
import cosmos.commands.restoration.Restore;
import cosmos.commands.transportation.Move;
import cosmos.utils.Permission;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandRegister implements ICommandSpec {

    @Override
    public CommandSpec getCommandSpec() {
        CommandSpec propertiesCommandSpec = CommandSpec.builder()
                .description(Text.of("Cosmos properties commands"))
                .permission(Permission.COMMAND_MANAGEMENT_PROPERTIES.toString())
                .child(new Basics().getCommandSpec(), AbstractCommand.getName(Basics.class))
                .child(new Border().getCommandSpec(), AbstractCommand.getName(Border.class))
                .child(new Rules().getCommandSpec(), AbstractCommand.getName(Rules.class))
                .child(new Weather().getCommandSpec(), AbstractCommand.getName(Weather.class))
                .build();

        return CommandSpec.builder()
                .description(Text.of("Cosmos commands | Worlds management plugin"))
                .permission(Permission.ROOT.toString())
                .child(propertiesCommandSpec, "properties", "prop")
                .child(new Delete().getCommandSpec(), AbstractCommand.getName(Delete.class))
                .child(new Duplicate().getCommandSpec(), AbstractCommand.getName(Duplicate.class))
                .child(new Import().getCommandSpec(), AbstractCommand.getName(Import.class))
                .child(new List().getCommandSpec(), AbstractCommand.getName(List.class))
                .child(new Load().getCommandSpec(), AbstractCommand.getName(Load.class))
                .child(new New().getCommandSpec(), AbstractCommand.getName(New.class))
                .child(new Rename().getCommandSpec(), AbstractCommand.getName(Rename.class))
                .child(new Unload().getCommandSpec(), AbstractCommand.getName(Unload.class))
                .child(new Backup().getCommandSpec(), AbstractCommand.getName(Backup.class))
                .child(new Restore().getCommandSpec(), AbstractCommand.getName(Restore.class))
                .child(new Move().getCommandSpec(), AbstractCommand.getName(Move.class))
                .build();
    }
}
