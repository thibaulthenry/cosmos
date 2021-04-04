package cosmos.statics.arguments;

import cosmos.constants.ArgKeys;
import cosmos.models.BackupArchetype;
import cosmos.statics.arguments.implementations.WorldDistinctElement;
import cosmos.statics.finders.FinderBackup;
import cosmos.statics.finders.FinderWorldName;
import org.spongepowered.api.command.args.CommandElement;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorldNameArguments {

    public static CommandElement loadedChoices(ArgKeys argName) {
        return ChoicesArguments.collectionChoices(
                argName,
                FinderWorldName::getLoadedWorldNames,
                false
        );
    }

    public static CommandElement distinctChoices(ArgKeys argName) {
        return new WorldDistinctElement(argName.t);
    }

    public static CommandElement unloadedChoices(ArgKeys argName, boolean includeDisabled) {
        return ChoicesArguments.collectionChoices(
                argName,
                () -> FinderWorldName.getUnloadedWorldNames(includeDisabled),
                false
        );
    }

    public static CommandElement disabledChoices(ArgKeys argName) {
        return ChoicesArguments.collectionChoices(
                argName,
                FinderWorldName::getDisabledWorldNames,
                false
        );
    }

    public static CommandElement exportedChoices(ArgKeys argName) {
        return ChoicesArguments.collectionChoices(
                argName,
                FinderWorldName::getExportedWorldNames,
                false
        );
    }

    public static CommandElement backupChoices(ArgKeys argName) {
        return ChoicesArguments.choices(
                argName,
                () -> toBackupArchetypeMap(FinderBackup.getBackups()),
                false
        );
    }

    public static CommandElement backupWorldChoices(ArgKeys argName) {
        return ChoicesArguments.collectionChoices(
                argName,
                () -> FinderBackup.getBackups().stream().map(BackupArchetype::getWorldName).collect(Collectors.toList()),
                false
        );
    }

    private static Map<String, BackupArchetype> toBackupArchetypeMap(Collection<BackupArchetype> collection) {
        return collection
                .stream()
                .distinct()
                .collect(Collectors.toMap(BackupArchetype::getName, Function.identity(), (e1, e2) -> e1));
    }

}
