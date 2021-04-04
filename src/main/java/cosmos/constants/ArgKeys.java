package cosmos.constants;

import org.spongepowered.api.text.Text;

public enum ArgKeys {
    AMOUNT("amount"),
    COMMAND("command"),
    CRITERION("criterion"),
    DESTINATION_TARGET("destination-target"),
    DIAMETER("diameter"),
    DISPLAY_NAME("display-name"),
    DISPLAY_SLOT("display-slot"),
    DISTANCE("distance"),
    DISTANCE_IN_CHUNKS("distance-in-chunks"),
    DISABLED_WORLD("disabled-world"),
    DURATION_MILLISECONDS("duration-milliseconds"),
    DURATION_SECONDS("duration-seconds"),
    END_DIAMETER("end-diameter"),
    EXPORTED_WORLD("exported-world"),
    GAME_RULE("game-rule"),
    IS_FILLED("is-filled"),
    LEVEL("level"),
    LOADED_WORLD("loaded-world"),
    MAX("max"),
    MESSAGE_RECEIVER("message-receiver"),
    MIN("min"),
    MODIFY_COMMAND("modify-command"),
    NEW_NAME("new-name"),
    OBJECTIVE("objective"),
    OBJECTIVE_TRIGGER("trigger-objective"),
    OBJECTIVE_NAME("objective-name"),
    OPERAND("operand"),
    PER_WORLD_COMMAND("per-world-command"),
    PLAYER("player"),
    POSITION_XYZ("x> <y> <z"),
    POSITION_XZ("x> <z"),
    ROTATION("pitch> <yaw> <roll"),
    SAFE_ONLY("safe-only"),
    SAVE_CONFIG("save-config"),
    SCORE("score"),
    SEED("seed"),
    SOURCES("source"),
    SOURCE_OBJECTIVE("source-objective"),
    START_DIAMETER("start-diameter"),
    STATE("state"),
    TAG("tag"),
    TARGETS("targets"),
    TEAM("team"),
    TEAM_NAME("team-name"),
    TEAM_OPTION("team-option"),
    UNLOADED_WORLD("unloaded-world"),
    VALUE("value"),
    WORLD("world"),
    WORLD_BACKUP("world-backup"),
    WORLD_DIMENSION("world-dimension"),
    WORLD_GENERATOR("world-generator"),
    WORLD_MODIFIERS("world-modifiers"),
    WORLD_TIME("world-time");

    public final String f;
    public final Text t;

    ArgKeys(String text) {
        f = "-" + text;
        t = Text.of(text);
    }
}
