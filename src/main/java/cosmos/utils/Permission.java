package cosmos.utils;

import cosmos.Cosmos;
import cosmos.commands.AbstractCommand;
import cosmos.commands.management.*;
import cosmos.commands.management.properties.Basics;
import cosmos.commands.management.properties.Border;
import cosmos.commands.management.properties.Rules;
import cosmos.commands.management.properties.Weather;
import cosmos.commands.restoration.Backup;
import cosmos.commands.restoration.Restore;
import cosmos.commands.transportation.Move;

public enum Permission {
    ROOT(Cosmos.class.getPackage().getName()),


    COMMAND(AbstractCommand.class.getPackage().getName()),


    COMMAND_MANAGEMENT(COMMAND.toString(), "management"),
    COMMAND_MANAGEMENT_DELETE(COMMAND_MANAGEMENT.toString(), AbstractCommand.getName(Delete.class)),
    COMMAND_MANAGEMENT_DUPLICATE(COMMAND_MANAGEMENT.toString(), AbstractCommand.getName(Duplicate.class)),
    COMMAND_MANAGEMENT_IMPORT(COMMAND_MANAGEMENT.toString(), AbstractCommand.getName(Import.class)),
    COMMAND_MANAGEMENT_LIST(COMMAND_MANAGEMENT.toString(), AbstractCommand.getName(List.class)),
    COMMAND_MANAGEMENT_LOAD(COMMAND_MANAGEMENT.toString(), AbstractCommand.getName(Load.class)),
    COMMAND_MANAGEMENT_NEW(COMMAND_MANAGEMENT.toString(), AbstractCommand.getName(New.class)),
    COMMAND_MANAGEMENT_RENAME(COMMAND_MANAGEMENT.toString(), AbstractCommand.getName(Rename.class)),
    COMMAND_MANAGEMENT_UNLOAD(COMMAND_MANAGEMENT.toString(), AbstractCommand.getName(Unload.class)),

    COMMAND_MANAGEMENT_PROPERTIES(COMMAND_MANAGEMENT.toString(), "properties"),
    COMMAND_MANAGEMENT_PROPERTIES_BASICS(COMMAND_MANAGEMENT_PROPERTIES.toString(), AbstractCommand.getName(Basics.class)),
    COMMAND_MANAGEMENT_PROPERTIES_BORDER(COMMAND_MANAGEMENT_PROPERTIES.toString(), AbstractCommand.getName(Border.class)),
    COMMAND_MANAGEMENT_PROPERTIES_RULES(COMMAND_MANAGEMENT_PROPERTIES.toString(), AbstractCommand.getName(Rules.class)),
    COMMAND_MANAGEMENT_PROPERTIES_WEATHER(COMMAND_MANAGEMENT_PROPERTIES.toString(), AbstractCommand.getName(Weather.class)),


    COMMAND_RESTORATION(COMMAND.toString(), "restoration"),
    COMMAND_RESTORATION_BACKUP(COMMAND_RESTORATION.toString(), AbstractCommand.getName(Backup.class)),
    COMMAND_RESTORATION_RESTORE(COMMAND_RESTORATION.toString(), AbstractCommand.getName(Restore.class)),


    COMMAND_TRANSPORTATION(COMMAND.toString(), "transportation"),
    COMMAND_TRANSPORTATION_MOVE(COMMAND_TRANSPORTATION.toString(), AbstractCommand.getName(Move.class));

    private final String permission;

    Permission(String... paths) {
        this.permission = String.join(".", paths);
        System.out.println(permission);
    }

    public String toString() {
        return permission;
    }
}
