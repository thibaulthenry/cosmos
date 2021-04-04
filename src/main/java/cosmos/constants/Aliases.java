package cosmos.constants;

import cosmos.commands.AbstractCommand;
import cosmos.commands.border.Information;
import cosmos.commands.border.Transpose;
import cosmos.commands.properties.Rules;
import cosmos.commands.root.Delete;
import cosmos.commands.root.Duplicate;
import cosmos.commands.root.Move;
import cosmos.commands.root.MoveTo;
import cosmos.commands.root.New;
import cosmos.commands.root.Position;
import cosmos.commands.weather.Sun;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Aliases {

    BORDER_INFORMATION(Information.class, "info"),
    BORDER_TRANSPOSE(Transpose.class, "move"),
    PER_WORLD_INFORMATION(cosmos.commands.perworld.Information.class, "info"),
    PROPERTIES_RULES(Rules.class, "gamerules"),
    ROOT_DELETE(Delete.class, "remove"),
    ROOT_DUPLICATE(Duplicate.class, "copy"),
    ROOT_INFORMATION(cosmos.commands.root.Information.class, "info"),
    ROOT_NEW(New.class, "create"),
    ROOT_MOVE(Move.class, "mv", "tp"),
    ROOT_MOVE_TO(MoveTo.class, "mvto", "tpto"),
    ROOT_POSITION(Position.class, "pos"),
    WEATHER_SUN(Sun.class, "clear");

    private static final Map<Class<? extends AbstractCommand>, Aliases> FROM_CLASS = new HashMap<>();

    static {
        Arrays.stream(Aliases.values())
                .forEach(aliases -> FROM_CLASS.putIfAbsent(aliases.commandClass, aliases));
    }

    private final Class<? extends AbstractCommand> commandClass;
    private final String[] aliases;

    Aliases(Class<? extends AbstractCommand> commandClass, String... aliases) {
        this.commandClass = commandClass;
        this.aliases = ArrayUtils.addAll(new String[]{baseAlias(commandClass)}, aliases);
    }

    private static String baseAlias(Class<? extends AbstractCommand> commandClass) {
        return commandClass.getSimpleName().toLowerCase();
    }

    public static String[] fromClass(Class<? extends AbstractCommand> commandClass) {
        return Optional.ofNullable(FROM_CLASS.get(commandClass)).map(aliases -> aliases.aliases).orElse(new String[]{baseAlias(commandClass)});
    }
}
