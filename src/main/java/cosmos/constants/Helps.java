package cosmos.constants;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import cosmos.commands.AbstractCommand;
import cosmos.commands.backup.Reset;
import cosmos.commands.backup.Restore;
import cosmos.commands.backup.Save;
import cosmos.commands.backup.Tag;
import cosmos.commands.border.Center;
import cosmos.commands.border.DamageAmount;
import cosmos.commands.border.DamageThreshold;
import cosmos.commands.border.Information;
import cosmos.commands.border.Operate;
import cosmos.commands.border.Remove;
import cosmos.commands.border.Size;
import cosmos.commands.border.Transpose;
import cosmos.commands.border.WarningDistance;
import cosmos.commands.border.WarningTime;
import cosmos.commands.perworld.Bypass;
import cosmos.commands.perworld.Group;
import cosmos.commands.perworld.Toggle;
import cosmos.commands.properties.AllowCommandBlocks;
import cosmos.commands.properties.Difficulty;
import cosmos.commands.properties.EnableStructures;
import cosmos.commands.properties.GameMode;
import cosmos.commands.properties.GenerateSpawnOnLoad;
import cosmos.commands.properties.GeneratorType;
import cosmos.commands.properties.Hardcore;
import cosmos.commands.properties.KeepSpawnLoaded;
import cosmos.commands.properties.LoadOnStartup;
import cosmos.commands.properties.Pvp;
import cosmos.commands.properties.Rules;
import cosmos.commands.properties.Seed;
import cosmos.commands.properties.SpawnPosition;
import cosmos.commands.root.Delete;
import cosmos.commands.root.Disable;
import cosmos.commands.root.Duplicate;
import cosmos.commands.root.Enable;
import cosmos.commands.root.Help;
import cosmos.commands.root.Import;
import cosmos.commands.root.Load;
import cosmos.commands.root.Move;
import cosmos.commands.root.MoveTo;
import cosmos.commands.root.New;
import cosmos.commands.root.Position;
import cosmos.commands.root.Rename;
import cosmos.commands.root.Unload;
import cosmos.commands.root.ViewDistance;
import cosmos.commands.scoreboard.objectives.Add;
import cosmos.commands.scoreboard.objectives.Modify;
import cosmos.commands.scoreboard.objectives.SetDisplay;
import cosmos.commands.scoreboard.players.Get;
import cosmos.commands.scoreboard.players.Operation;
import cosmos.commands.scoreboard.players.Random;
import cosmos.commands.scoreboard.players.Set;
import cosmos.commands.scoreboard.players.Test;
import cosmos.commands.scoreboard.teams.Empty;
import cosmos.commands.scoreboard.teams.Join;
import cosmos.commands.scoreboard.teams.Leave;
import cosmos.commands.scoreboard.teams.Option;
import cosmos.commands.time.Calendar;
import cosmos.commands.time.Dawn;
import cosmos.commands.time.Dusk;
import cosmos.commands.time.IgnorePlayersSleeping;
import cosmos.commands.time.Midday;
import cosmos.commands.time.Midnight;
import cosmos.commands.time.RealTime;
import cosmos.commands.time.Tomorrow;
import cosmos.commands.weather.Forecast;
import cosmos.commands.weather.Rain;
import cosmos.commands.weather.Sun;
import cosmos.commands.weather.Thunder;
import cosmos.statics.handlers.OutputFormatter;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.scoreboard.Visibilities;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.difficulty.Difficulties;
import org.spongepowered.api.world.gamerule.DefaultGameRules;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings({"HardcodedFileSeparator", "unchecked"})
public enum Helps {
    BACKUP_DELETE(
            cosmos.commands.backup.Delete.class,
            Outputs.USAGE_BACKUP_DELETE.asText(ArgKeys.WORLD_BACKUP),
            Pair.of(Outputs.EX_BACKUP_DELETE_0.asText(OutputFormatter.noFormatCode + "world_20201130_203500"), Outputs.DESC_BACKUP_DELETE_0.asText(OutputFormatter.noFormatCode + "world_20201130_203500"))
    ),

    BACKUP_LIST(
            cosmos.commands.backup.List.class,
            Outputs.USAGE_BACKUP_LIST.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_BACKUP_LIST_0.asText(), Outputs.DESC_BACKUP_LIST_0.asText()),
            Pair.of(Outputs.EX_BACKUP_LIST_1.asText("SavedWorld"), Outputs.DESC_BACKUP_LIST_1.asText("SavedWorld"))
    ),

    BACKUP_RESET(
            Reset.class,
            Outputs.USAGE_BACKUP_RESET.asText(ArgKeys.WORLD_BACKUP),
            Pair.of(Outputs.EX_BACKUP_RESET_0.asText(OutputFormatter.noFormatCode + "world_20201130_203500"), Outputs.DESC_BACKUP_RESET_0.asText(OutputFormatter.noFormatCode + "world_20201130_203500"))
    ),

    BACKUP_RESTORE(
            Restore.class,
            Outputs.USAGE_BACKUP_RESTORE.asText(ArgKeys.WORLD_BACKUP),
            Pair.of(Outputs.EX_BACKUP_RESTORE_0.asText(OutputFormatter.noFormatCode + "world_20201130_203500"), Outputs.DESC_BACKUP_RESTORE_0.asText(OutputFormatter.noFormatCode + "world_20201130_203500"))
    ),

    BACKUP_SAVE(
            Save.class,
            Outputs.USAGE_BACKUP_SAVE.asText(ArgKeys.UNLOADED_WORLD, ArgKeys.DISABLED_WORLD),
            Pair.of(Outputs.EX_BACKUP_SAVE_0.asText("MyWorld"), Outputs.DESC_BACKUP_SAVE_0.asText("MyWorld"))
    ),

    BACKUP_TAG(
            Tag.class,
            Outputs.USAGE_BACKUP_TAG.asText(ArgKeys.WORLD_BACKUP, ArgKeys.TAG),
            Pair.of(Outputs.EX_BACKUP_TAG_0.asText(OutputFormatter.noFormatCode + "world_20201130_203500", "monday"), Outputs.DESC_BACKUP_TAG_0.asText(OutputFormatter.noFormatCode + "world_20201130_203500", "monday", "world_monday"))
    ),

    BORDER_CENTER(
            Center.class,
            Outputs.USAGE_BORDER_CENTER.asText(ArgKeys.LOADED_WORLD, "<" + ArgKeys.POSITION_XZ.t.toPlain() + ">"),
            Pair.of(Outputs.EX_BORDER_CENTER_0.asText("MyWorld", 0, 0), Outputs.DESC_BORDER_CENTER_0.asText("MyWorld", Vector2d.from(0, 0))),
            Pair.of(Outputs.EX_BORDER_CENTER_1.asText(0, 0), Outputs.DESC_BORDER_CENTER_1.asText(Vector2d.from(0, 0))),
            Pair.of(Outputs.EX_BORDER_CENTER_2.asText("#me"), Outputs.DESC_BORDER_CENTER_2.asText()),
            Pair.of(Outputs.EX_BORDER_CENTER_3.asText("MyWorld"), Outputs.DESC_BORDER_CENTER_3.asText("MyWorld")),
            Pair.of(Outputs.EX_BORDER_CENTER_4.asText(), Outputs.DESC_BORDER_CENTER_4.asText())
    ),

    BORDER_DAMAGE_AMOUNT(
            DamageAmount.class,
            Outputs.USAGE_BORDER_DAMAGE_AMOUNT.asText(ArgKeys.LOADED_WORLD, ArgKeys.AMOUNT),
            Pair.of(Outputs.EX_BORDER_DAMAGE_AMOUNT_0.asText("MyWorld", 0.5), Outputs.DESC_BORDER_DAMAGE_AMOUNT_0.asText("MyWorld", 0.5)),
            Pair.of(Outputs.EX_BORDER_DAMAGE_AMOUNT_1.asText(1.5), Outputs.DESC_BORDER_DAMAGE_AMOUNT_1.asText(1.5)),
            Pair.of(Outputs.EX_BORDER_DAMAGE_AMOUNT_2.asText("MyWorld"), Outputs.DESC_BORDER_DAMAGE_AMOUNT_2.asText("MyWorld")),
            Pair.of(Outputs.EX_BORDER_DAMAGE_AMOUNT_3.asText(), Outputs.DESC_BORDER_DAMAGE_AMOUNT_3.asText())
    ),

    BORDER_DAMAGE_THRESHOLD(
            DamageThreshold.class,
            Outputs.USAGE_BORDER_DAMAGE_THRESHOLD.asText(ArgKeys.LOADED_WORLD, ArgKeys.DISTANCE),
            Pair.of(Outputs.EX_BORDER_DAMAGE_THRESHOLD_0.asText("MyWorld", 5), Outputs.DESC_BORDER_DAMAGE_THRESHOLD_0.asText("MyWorld", 5)),
            Pair.of(Outputs.EX_BORDER_DAMAGE_THRESHOLD_1.asText(3.5), Outputs.DESC_BORDER_DAMAGE_THRESHOLD_1.asText(3.5)),
            Pair.of(Outputs.EX_BORDER_DAMAGE_THRESHOLD_2.asText("MyWorld"), Outputs.DESC_BORDER_DAMAGE_THRESHOLD_2.asText("MyWorld")),
            Pair.of(Outputs.EX_BORDER_DAMAGE_THRESHOLD_3.asText(), Outputs.DESC_BORDER_DAMAGE_THRESHOLD_3.asText())
    ),

    BORDER_INFORMATION(
            Information.class,
            Outputs.USAGE_BORDER_INFORMATION.asText(ArgKeys.LOADED_WORLD),
            Pair.of(Outputs.EX_BORDER_INFORMATION_0.asText("MyWorld"), Outputs.DESC_BORDER_INFORMATION_0.asText("MyWorld")),
            Pair.of(Outputs.EX_BORDER_INFORMATION_1.asText(), Outputs.DESC_BORDER_INFORMATION_1.asText())
    ),

    BORDER_OPERATE(
            Operate.class,
            Outputs.USAGE_BORDER_OPERATE.asText(ArgKeys.LOADED_WORLD, ArgKeys.OPERAND, ArgKeys.VALUE, ArgKeys.DURATION_MILLISECONDS),
            Pair.of(Outputs.EX_BORDER_OPERATE_0.asText("MyWorld", Operands.PLUS, 100, 3000), Outputs.DESC_BORDER_OPERATE_0.asText(Operands.PLUS, 100, "MyWorld", 3000)),
            Pair.of(Outputs.EX_BORDER_OPERATE_1.asText(Operands.MINUS, 50), Outputs.DESC_BORDER_OPERATE_1.asText(Operands.MINUS, 50)),
            Pair.of(Outputs.EX_BORDER_OPERATE_2.asText("MyWorld", Operands.TIMES, 2), Outputs.DESC_BORDER_OPERATE_2.asText(Operands.TIMES, "MyWorld", 2)),
            Pair.of(Outputs.EX_BORDER_OPERATE_3.asText(Operands.DIVIDE, 4, 15000), Outputs.DESC_BORDER_OPERATE_3.asText(Operands.DIVIDE, 4, 15000))
    ),


    BORDER_REMOVE(
            Remove.class,
            Outputs.USAGE_BORDER_REMOVE.asText(ArgKeys.LOADED_WORLD),
            Pair.of(Outputs.EX_BORDER_REMOVE_0.asText("MyWorld"), Outputs.DESC_BORDER_REMOVE_0.asText("MyWorld")),
            Pair.of(Outputs.EX_BORDER_REMOVE_1.asText(), Outputs.DESC_BORDER_REMOVE_1.asText())
    ),

    BORDER_SIZE(
            Size.class,
            Outputs.USAGE_BORDER_SIZE.asText(ArgKeys.LOADED_WORLD, ArgKeys.DIAMETER),
            Pair.of(Outputs.EX_BORDER_SIZE_0.asText("MyWorld", 500), Outputs.DESC_BORDER_SIZE_0.asText("MyWorld", 500)),
            Pair.of(Outputs.EX_BORDER_SIZE_1.asText(300), Outputs.DESC_BORDER_SIZE_1.asText(300)),
            Pair.of(Outputs.EX_BORDER_SIZE_2.asText("MyWorld"), Outputs.DESC_BORDER_SIZE_2.asText("MyWorld")),
            Pair.of(Outputs.EX_BORDER_SIZE_3.asText(), Outputs.DESC_BORDER_SIZE_3.asText())
    ),

    BORDER_TRANSPOSE(
            Transpose.class,
            Outputs.USAGE_BORDER_TRANSPOSE.asText(ArgKeys.LOADED_WORLD, ArgKeys.DURATION_MILLISECONDS, ArgKeys.END_DIAMETER, ArgKeys.START_DIAMETER),
            Pair.of(Outputs.EX_BORDER_TRANSPOSE_0.asText("MyWorld", 8000, 0, 500), Outputs.DESC_BORDER_TRANSPOSE_0.asText("MyWorld", 500, 0, 8000)),
            Pair.of(Outputs.EX_BORDER_TRANSPOSE_1.asText("MyWorld", 20000, 1000), Outputs.DESC_BORDER_TRANSPOSE_1.asText("MyWorld", 1000, 20000)),
            Pair.of(Outputs.EX_BORDER_TRANSPOSE_2.asText(200000, 100, 1000), Outputs.DESC_BORDER_TRANSPOSE_2.asText(1000, 100, 200000)),
            Pair.of(Outputs.EX_BORDER_TRANSPOSE_3.asText(3000, 500), Outputs.DESC_BORDER_TRANSPOSE_3.asText(500, 3000))
    ),

    BORDER_WARNING_DISTANCE(
            WarningDistance.class,
            Outputs.USAGE_BORDER_WARNING_DISTANCE.asText(ArgKeys.LOADED_WORLD, ArgKeys.DISTANCE),
            Pair.of(Outputs.EX_BORDER_WARNING_DISTANCE_0.asText("MyWorld", 5), Outputs.DESC_BORDER_WARNING_DISTANCE_0.asText("MyWorld", 0.5)),
            Pair.of(Outputs.EX_BORDER_WARNING_DISTANCE_1.asText(15), Outputs.DESC_BORDER_WARNING_DISTANCE_1.asText(1.5)),
            Pair.of(Outputs.EX_BORDER_WARNING_DISTANCE_2.asText("MyWorld"), Outputs.DESC_BORDER_WARNING_DISTANCE_2.asText("MyWorld")),
            Pair.of(Outputs.EX_BORDER_WARNING_DISTANCE_3.asText(), Outputs.DESC_BORDER_WARNING_DISTANCE_3.asText())
    ),

    BORDER_WARNING_TIME(
            WarningTime.class,
            Outputs.USAGE_BORDER_WARNING_TIME.asText(ArgKeys.LOADED_WORLD, ArgKeys.DURATION_SECONDS),
            Pair.of(Outputs.EX_BORDER_WARNING_TIME_0.asText("MyWorld", 0.5), Outputs.DESC_BORDER_WARNING_TIME_0.asText("MyWorld", 0.5)),
            Pair.of(Outputs.EX_BORDER_WARNING_TIME_1.asText(1.5), Outputs.DESC_BORDER_WARNING_TIME_1.asText(1.5)),
            Pair.of(Outputs.EX_BORDER_WARNING_TIME_2.asText("MyWorld"), Outputs.DESC_BORDER_WARNING_TIME_2.asText("MyWorld")),
            Pair.of(Outputs.EX_BORDER_WARNING_TIME_3.asText(), Outputs.DESC_BORDER_WARNING_TIME_3.asText())
    ),

    DELETE(
            Delete.class,
            Outputs.USAGE_DELETE.asText(ArgKeys.UNLOADED_WORLD, ArgKeys.DISABLED_WORLD, ArgKeys.EXPORTED_WORLD),
            Pair.of(Outputs.EX_DELETE_0.asText("MyWorld"), Outputs.DESC_DELETE_0.asText("MyWorld"))
    ),

    DISABLE(
            Disable.class,
            Outputs.USAGE_DISABLE.asText(ArgKeys.UNLOADED_WORLD),
            Pair.of(Outputs.EX_DISABLE_0.asText("MyWorld"), Outputs.DESC_DISABLE_0.asText("MyWorld"))
    ),

    DUPLICATE(
            Duplicate.class,
            Outputs.USAGE_DUPLICATE.asText(ArgKeys.WORLD, ArgKeys.NEW_NAME),
            Pair.of(Outputs.EX_DUPLICATE_0.asText("MyWorld", "AnotherWorld"), Outputs.DESC_DUPLICATE_0.asText("MyWorld", "AnotherWorld"))
    ),
    
    ENABLE(
            Enable.class,
            Outputs.USAGE_ENABLE.asText(ArgKeys.DISABLED_WORLD),
            Pair.of(Outputs.EX_ENABLE_0.asText("MyWorld"), Outputs.DESC_ENABLE_0.asText("MyWorld"))
    ),

    HELP(
            Help.class,
            Outputs.USAGE_HELP.asText(ArgKeys.COMMAND),
            Pair.of(Outputs.EX_HELP_0.asText(), Outputs.DESC_HELP_0.asText()),
            Pair.of(Outputs.EX_HELP_1.asText("move"), Outputs.DESC_HELP_1.asText("move"))
    ),

    IMPORT(
            Import.class,
            Outputs.USAGE_IMPORT.asText(ArgKeys.EXPORTED_WORLD),
            Pair.of(Outputs.EX_IMPORT_0.asText("MyWorld"), Outputs.DESC_IMPORT_0.asText("MyWorld"))
    ),

    INFORMATION(
            cosmos.commands.root.Information.class,
            Outputs.USAGE_INFORMATION.asText(ArgKeys.LOADED_WORLD, ArgKeys.UNLOADED_WORLD, ArgKeys.DISABLED_WORLD),
            Pair.of(Outputs.EX_INFORMATION_0.asText("MyWorld"), Outputs.DESC_INFORMATION_0.asText("MyWorld"))
    ),

    LIST(
            cosmos.commands.root.List.class,
            Outputs.USAGE_LIST.asText(ArgKeys.WORLD_DIMENSION),
            Pair.of(Outputs.EX_LIST_0.asText(), Outputs.DESC_LIST_0.asText()),
            Pair.of(Outputs.EX_LIST_1.asText("nether"), Outputs.DESC_LIST_1.asText("nether"))
    ),

    LOAD(
            Load.class,
            Outputs.USAGE_LOAD.asText(ArgKeys.UNLOADED_WORLD),
            Pair.of(Outputs.EX_LOAD_0.asText("MyWorld"), Outputs.DESC_LOAD_0.asText("MyWorld"))
    ),

    MOVE(
            Move.class,
            Outputs.USAGE_MOVE.asText(ArgKeys.TARGETS, ArgKeys.LOADED_WORLD, "<" + ArgKeys.POSITION_XYZ.t.toPlain() + ">",  "<" + ArgKeys.ROTATION.t.toPlain() + ">", "--" + ArgKeys.SAFE_ONLY.t.toPlain()),
            Pair.of(Outputs.EX_MOVE_0.asText("Someone", "MyWorld", 100, 64, 100, -90, 180, 0), Outputs.DESC_MOVE_0.asText("Someone", "MyWorld",  Vector3i.from(100, 64, -100), Vector3i.from(-90, 180, 0))),
            Pair.of(Outputs.EX_MOVE_1.asText(), Outputs.DESC_MOVE_1.asText()),
            Pair.of(Outputs.EX_MOVE_2.asText("@e[r=35]"), Outputs.DESC_MOVE_2.asText("@e[r=35]")),
            Pair.of(Outputs.EX_MOVE_3.asText("MyWorld", -300, 100, 0), Outputs.DESC_MOVE_3.asText("MyWorld", Vector3i.from(-300, 120, 0))),
            Pair.of(Outputs.EX_MOVE_4.asText("Somebody", "MyWorld", "--" + ArgKeys.SAFE_ONLY.t.toPlain()), Outputs.DESC_MOVE_4.asText("Somebody", "MyWorld", "Somebody"))
    ),

    MOVE_TO(
            MoveTo.class,
            Outputs.USAGE_MOVE_TO.asText(ArgKeys.DESTINATION_TARGET, ArgKeys.TARGETS, "--" + ArgKeys.SAFE_ONLY.t.toPlain()),
            Pair.of(Outputs.EX_MOVE_TO_0.asText("SomebodyElse", "Someone"), Outputs.DESC_MOVE_TO_0.asText("Someone", "SomebodyElse")),
            Pair.of(Outputs.EX_MOVE_TO_1.asText("Someone"), Outputs.DESC_MOVE_TO_1.asText("Someone")),
            Pair.of(Outputs.EX_MOVE_TO_2.asText("Someone", "@e[r=35]"), Outputs.DESC_MOVE_TO_2.asText("@e[r=35]", "Someone")),
            Pair.of(Outputs.EX_MOVE_TO_3.asText("SomebodyElse", "Someone", "--" + ArgKeys.SAFE_ONLY.t.toPlain()), Outputs.DESC_MOVE_TO_3.asText("Someone", "SomebodyElse", "Someone"))
    ),

    NEW(
            New.class,
            Outputs.USAGE_NEW.asText(ArgKeys.NEW_NAME, ArgKeys.WORLD_DIMENSION, ArgKeys.WORLD_GENERATOR, ArgKeys.WORLD_MODIFIERS),
            Pair.of(Outputs.EX_NEW_0.asText("MyNewWorld"), Outputs.DESC_NEW_0.asText("MyNewWorld")),
            Pair.of(Outputs.EX_NEW_1.asText("MyNewWorld", "nether"), Outputs.DESC_NEW_1.asText("MyNewWorld", "nether")),
            Pair.of(Outputs.EX_NEW_2.asText("MyNewWorld", "the_end"), Outputs.DESC_NEW_2.asText("MyNewWorld", "the_end")),
            Pair.of(Outputs.EX_NEW_3.asText("MyNewWorld", "overworld", "the_end"), Outputs.DESC_NEW_3.asText("MyNewWorld", "the_end", "overworld")),
            Pair.of(Outputs.EX_NEW_4.asText("MyNewWorld", "nether", "overworld", "skylands"), Outputs.DESC_NEW_4.asText("MyNewWorld", "overworld", "skylands", "nether"))
    ),

    PER_WORLD_BYPASS(
            Bypass.class,
            Outputs.USAGE_PER_WORLD_BYPASS.asText(ArgKeys.PER_WORLD_COMMAND, ArgKeys.PLAYER, ArgKeys.STATE),
            Pair.of(Outputs.EX_PER_WORLD_BYPASS_0.asText(PerWorldCommands.ADVANCEMENTS), Outputs.DESC_PER_WORLD_BYPASS_0.asText(PerWorldCommands.ADVANCEMENTS)),
            Pair.of(Outputs.EX_PER_WORLD_BYPASS_1.asText(PerWorldCommands.TAB_LISTS, false), Outputs.DESC_PER_WORLD_BYPASS_1.asText(PerWorldCommands.TAB_LISTS)),
            Pair.of(Outputs.EX_PER_WORLD_BYPASS_2.asText(PerWorldCommands.SCOREBOARDS, "Someone", true), Outputs.DESC_PER_WORLD_BYPASS_2.asText("Someone", PerWorldCommands.SCOREBOARDS)),
            Pair.of(Outputs.EX_PER_WORLD_BYPASS_3.asText(PerWorldCommands.INVENTORIES, "Someone", false), Outputs.DESC_PER_WORLD_BYPASS_3.asText("Someone", PerWorldCommands.INVENTORIES))
    ),

    PER_WORLD_GROUP(
            Group.class,
            Outputs.USAGE_PER_WORLD_GROUP.asText(ArgKeys.PER_WORLD_COMMAND, ArgKeys.WORLD),
            Pair.of(Outputs.EX_PER_WORLD_GROUP_0.asText(PerWorldCommands.INVENTORIES, "world", "DIM-1", "DIM1"), Outputs.DESC_PER_WORLD_GROUP_0.asText("world", "DIM-1", "DIM1", PerWorldCommands.INVENTORIES)),
            Pair.of(Outputs.EX_PER_WORLD_GROUP_1.asText(PerWorldCommands.ENDER_CHESTS, "world"), Outputs.DESC_PER_WORLD_GROUP_1.asText("world", PerWorldCommands.ENDER_CHESTS)),
            Pair.of(Outputs.EX_PER_WORLD_GROUP_2.asText(PerWorldCommands.SCOREBOARDS, "creative", "creative_end"), Outputs.DESC_PER_WORLD_GROUP_2.asText("creative", "creative_end", PerWorldCommands.SCOREBOARDS))
    ),

    PER_WORLD_INFORMATION(
            cosmos.commands.perworld.Information.class,
            Outputs.USAGE_PER_WORLD_INFORMATION.asText(ArgKeys.PER_WORLD_COMMAND),
            Pair.of(Outputs.EX_PER_WORLD_INFORMATION_0.asText(PerWorldCommands.CHATS), Outputs.DESC_PER_WORLD_INFORMATION_0.asText(PerWorldCommands.CHATS)),
            Pair.of(Outputs.EX_PER_WORLD_INFORMATION_1.asText(PerWorldCommands.TAB_LISTS), Outputs.DESC_PER_WORLD_INFORMATION_1.asText(PerWorldCommands.TAB_LISTS))
    ),

    PER_WORLD_TOGGLE(
            Toggle.class,
            Outputs.USAGE_PER_WORLD_TOGGLE.asText(ArgKeys.PER_WORLD_COMMAND, ArgKeys.STATE, "--" + ArgKeys.SAVE_CONFIG.t.toPlain()),
            Pair.of(Outputs.EX_PER_WORLD_TOGGLE_0.asText(PerWorldCommands.COMMAND_BLOCKS, true), Outputs.DESC_PER_WORLD_TOGGLE_0.asText(PerWorldCommands.COMMAND_BLOCKS, true)),
            Pair.of(Outputs.EX_PER_WORLD_TOGGLE_1.asText(PerWorldCommands.TAB_LISTS, 0), Outputs.DESC_PER_WORLD_TOGGLE_1.asText(PerWorldCommands.TAB_LISTS, 0)),
            Pair.of(Outputs.EX_PER_WORLD_TOGGLE_2.asText(PerWorldCommands.SCOREBOARDS, "yes", "--" + ArgKeys.SAVE_CONFIG.t.toPlain()), Outputs.DESC_PER_WORLD_TOGGLE_2.asText(PerWorldCommands.SCOREBOARDS, "yes")),
            Pair.of(Outputs.EX_PER_WORLD_TOGGLE_3.asText(PerWorldCommands.INVENTORIES), Outputs.DESC_PER_WORLD_TOGGLE_3.asText(PerWorldCommands.INVENTORIES)),
            Pair.of(Outputs.EX_PER_WORLD_TOGGLE_4.asText(PerWorldCommands.INVENTORIES, "--" + ArgKeys.SAVE_CONFIG.t.toPlain()), Outputs.DESC_PER_WORLD_TOGGLE_4.asText(PerWorldCommands.INVENTORIES))
    ),

    POSITION(
            Position.class,
            Outputs.USAGE_POSITION.asText(ArgKeys.TARGETS, ArgKeys.MESSAGE_RECEIVER),
            Pair.of(Outputs.EX_POSITION_0.asText(), Outputs.DESC_POSITION_0.asText()),
            Pair.of(Outputs.EX_POSITION_1.asText("@a[r=35]", "Someone"), Outputs.DESC_POSITION_1.asText("@a[r=35]", "Someone")),
            Pair.of(Outputs.EX_POSITION_2.asText("@a[r=35]"), Outputs.DESC_POSITION_2.asText("@a[r=35]"))
    ),

    PROPERTIES_ALLOW_COMMAND_BLOCKS(
            AllowCommandBlocks.class,
            Outputs.USAGE_PROPERTIES_ALLOW_COMMAND_BLOCKS.asText(ArgKeys.WORLD, ArgKeys.STATE),
            Pair.of(Outputs.EX_PROPERTIES_ALLOW_COMMAND_BLOCKS_0.asText("MyWorld", true), Outputs.DESC_PROPERTIES_ALLOW_COMMAND_BLOCKS_0.asText("MyWorld", true)),
            Pair.of(Outputs.EX_PROPERTIES_ALLOW_COMMAND_BLOCKS_1.asText(false), Outputs.DESC_PROPERTIES_ALLOW_COMMAND_BLOCKS_1.asText(false)),
            Pair.of(Outputs.EX_PROPERTIES_ALLOW_COMMAND_BLOCKS_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_ALLOW_COMMAND_BLOCKS_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_ALLOW_COMMAND_BLOCKS_3.asText(), Outputs.DESC_PROPERTIES_ALLOW_COMMAND_BLOCKS_3.asText())
    ),

    PROPERTIES_DIFFICULTY(
            Difficulty.class,
            Outputs.USAGE_PROPERTIES_DIFFICULTY.asText(ArgKeys.WORLD, ArgKeys.LEVEL),
            Pair.of(Outputs.EX_PROPERTIES_DIFFICULTY_0.asText("MyWorld", Difficulties.HARD), Outputs.DESC_PROPERTIES_DIFFICULTY_0.asText("MyWorld", Difficulties.HARD)),
            Pair.of(Outputs.EX_PROPERTIES_DIFFICULTY_1.asText(Difficulties.PEACEFUL), Outputs.DESC_PROPERTIES_DIFFICULTY_1.asText(Difficulties.PEACEFUL)),
            Pair.of(Outputs.EX_PROPERTIES_DIFFICULTY_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_DIFFICULTY_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_DIFFICULTY_3.asText(), Outputs.DESC_PROPERTIES_DIFFICULTY_3.asText())
    ),

    PROPERTIES_ENABLE_STRUCTURES(
            EnableStructures.class,
            Outputs.USAGE_PROPERTIES_ENABLE_STRUCTURES.asText(ArgKeys.WORLD, ArgKeys.STATE),
            Pair.of(Outputs.EX_PROPERTIES_ENABLE_STRUCTURES_0.asText("MyWorld", true), Outputs.DESC_PROPERTIES_ENABLE_STRUCTURES_0.asText("MyWorld", true)),
            Pair.of(Outputs.EX_PROPERTIES_ENABLE_STRUCTURES_1.asText(false), Outputs.DESC_PROPERTIES_ENABLE_STRUCTURES_1.asText(false)),
            Pair.of(Outputs.EX_PROPERTIES_ENABLE_STRUCTURES_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_ENABLE_STRUCTURES_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_ENABLE_STRUCTURES_3.asText(), Outputs.DESC_PROPERTIES_ENABLE_STRUCTURES_3.asText())
    ),

    PROPERTIES_GAME_MODE(
            GameMode.class,
            Outputs.USAGE_PROPERTIES_GAME_MODE.asText(ArgKeys.WORLD, ArgKeys.VALUE),
            Pair.of(Outputs.EX_PROPERTIES_GAME_MODE_0.asText("MyWorld", GameModes.ADVENTURE), Outputs.DESC_PROPERTIES_GAME_MODE_0.asText("MyWorld", GameModes.ADVENTURE)),
            Pair.of(Outputs.EX_PROPERTIES_GAME_MODE_1.asText(GameModes.SPECTATOR), Outputs.DESC_PROPERTIES_GAME_MODE_1.asText(GameModes.SPECTATOR)),
            Pair.of(Outputs.EX_PROPERTIES_GAME_MODE_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_GAME_MODE_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_GAME_MODE_3.asText(), Outputs.DESC_PROPERTIES_GAME_MODE_3.asText())
    ),

    PROPERTIES_GENERATE_SPAWN_ON_LOAD(
            GenerateSpawnOnLoad.class,
            Outputs.USAGE_PROPERTIES_GENERATE_SPAWN_ON_LOAD.asText(ArgKeys.WORLD, ArgKeys.STATE),
            Pair.of(Outputs.EX_PROPERTIES_GENERATE_SPAWN_ON_LOAD_0.asText("MyWorld", true), Outputs.DESC_PROPERTIES_GENERATE_SPAWN_ON_LOAD_0.asText("MyWorld", true)),
            Pair.of(Outputs.EX_PROPERTIES_GENERATE_SPAWN_ON_LOAD_1.asText(false), Outputs.DESC_PROPERTIES_GENERATE_SPAWN_ON_LOAD_1.asText(false)),
            Pair.of(Outputs.EX_PROPERTIES_GENERATE_SPAWN_ON_LOAD_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_GENERATE_SPAWN_ON_LOAD_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_GENERATE_SPAWN_ON_LOAD_3.asText(), Outputs.DESC_PROPERTIES_GENERATE_SPAWN_ON_LOAD_3.asText())
    ),
    
    PROPERTIES_GENERATOR_TYPE(
            GeneratorType.class,
            Outputs.USAGE_PROPERTIES_GENERATOR_TYPE.asText(ArgKeys.WORLD, ArgKeys.VALUE),
            Pair.of(Outputs.EX_PROPERTIES_GENERATOR_TYPE_0.asText("MyWorld", GeneratorTypes.AMPLIFIED), Outputs.DESC_PROPERTIES_GENERATOR_TYPE_0.asText("MyWorld", GeneratorTypes.AMPLIFIED)),
            Pair.of(Outputs.EX_PROPERTIES_GENERATOR_TYPE_1.asText(GeneratorTypes.FLAT), Outputs.DESC_PROPERTIES_GENERATOR_TYPE_1.asText(GeneratorTypes.FLAT)),
            Pair.of(Outputs.EX_PROPERTIES_GENERATOR_TYPE_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_GENERATOR_TYPE_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_GENERATOR_TYPE_3.asText(), Outputs.DESC_PROPERTIES_GENERATOR_TYPE_3.asText())
    ),
    
    PROPERTIES_HARDCORE(
            Hardcore.class,
            Outputs.USAGE_PROPERTIES_HARDCORE.asText(ArgKeys.WORLD, ArgKeys.STATE),
            Pair.of(Outputs.EX_PROPERTIES_HARDCORE_0.asText("MyWorld", true), Outputs.DESC_PROPERTIES_HARDCORE_0.asText("MyWorld", true)),
            Pair.of(Outputs.EX_PROPERTIES_HARDCORE_1.asText(false), Outputs.DESC_PROPERTIES_HARDCORE_1.asText(false)),
            Pair.of(Outputs.EX_PROPERTIES_HARDCORE_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_HARDCORE_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_HARDCORE_3.asText(), Outputs.DESC_PROPERTIES_HARDCORE_3.asText())
    ),

    PROPERTIES_KEEP_SPAWN_LOADED(
            KeepSpawnLoaded.class,
            Outputs.USAGE_PROPERTIES_KEEP_SPAWN_LOADED.asText(ArgKeys.WORLD, ArgKeys.STATE),
            Pair.of(Outputs.EX_PROPERTIES_KEEP_SPAWN_LOADED_0.asText("MyWorld", true), Outputs.DESC_PROPERTIES_KEEP_SPAWN_LOADED_0.asText("MyWorld", true)),
            Pair.of(Outputs.EX_PROPERTIES_KEEP_SPAWN_LOADED_1.asText(false), Outputs.DESC_PROPERTIES_KEEP_SPAWN_LOADED_1.asText(false)),
            Pair.of(Outputs.EX_PROPERTIES_KEEP_SPAWN_LOADED_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_KEEP_SPAWN_LOADED_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_KEEP_SPAWN_LOADED_3.asText(), Outputs.DESC_PROPERTIES_KEEP_SPAWN_LOADED_3.asText())
    ),

    PROPERTIES_LOAD_ON_STARTUP(
            LoadOnStartup.class,
            Outputs.USAGE_PROPERTIES_LOAD_ON_STARTUP.asText(ArgKeys.WORLD, ArgKeys.STATE),
            Pair.of(Outputs.EX_PROPERTIES_LOAD_ON_STARTUP_0.asText("MyWorld", true), Outputs.DESC_PROPERTIES_LOAD_ON_STARTUP_0.asText("MyWorld", true)),
            Pair.of(Outputs.EX_PROPERTIES_LOAD_ON_STARTUP_1.asText(false), Outputs.DESC_PROPERTIES_LOAD_ON_STARTUP_1.asText(false)),
            Pair.of(Outputs.EX_PROPERTIES_LOAD_ON_STARTUP_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_LOAD_ON_STARTUP_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_LOAD_ON_STARTUP_3.asText(), Outputs.DESC_PROPERTIES_LOAD_ON_STARTUP_3.asText())
    ),

    PROPERTIES_PVP(
            Pvp.class,
            Outputs.USAGE_PROPERTIES_PVP.asText(ArgKeys.WORLD, ArgKeys.STATE),
            Pair.of(Outputs.EX_PROPERTIES_PVP_0.asText("MyWorld", true), Outputs.DESC_PROPERTIES_PVP_0.asText("MyWorld", true)),
            Pair.of(Outputs.EX_PROPERTIES_PVP_1.asText(false), Outputs.DESC_PROPERTIES_PVP_1.asText(false)),
            Pair.of(Outputs.EX_PROPERTIES_PVP_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_PVP_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_PVP_3.asText(), Outputs.DESC_PROPERTIES_PVP_3.asText())
    ),

    PROPERTIES_RULES(
            Rules.class,
            Outputs.USAGE_PROPERTIES_RULES.asText(ArgKeys.WORLD, ArgKeys.GAME_RULE, ArgKeys.VALUE),
            Pair.of(Outputs.EX_PROPERTIES_RULES_0.asText("MyWorld", DefaultGameRules.DO_MOB_SPAWNING, false), Outputs.DESC_PROPERTIES_RULES_0.asText(DefaultGameRules.DO_MOB_SPAWNING, "MyWorld", false)),
            Pair.of(Outputs.EX_PROPERTIES_RULES_1.asText(DefaultGameRules.RANDOM_TICK_SPEED, 15), Outputs.DESC_PROPERTIES_RULES_1.asText(DefaultGameRules.RANDOM_TICK_SPEED, 15)),
            Pair.of(Outputs.EX_PROPERTIES_RULES_2.asText("MyWorld", DefaultGameRules.DO_WEATHER_CYCLE), Outputs.DESC_PROPERTIES_RULES_2.asText(DefaultGameRules.DO_WEATHER_CYCLE, "MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_RULES_3.asText(DefaultGameRules.DO_FIRE_TICK), Outputs.DESC_PROPERTIES_RULES_3.asText(DefaultGameRules.DO_FIRE_TICK))
    ),

    PROPERTIES_SEED(
            Seed.class,
            Outputs.USAGE_PROPERTIES_SEED.asText(ArgKeys.WORLD, ArgKeys.SEED),
            Pair.of(Outputs.EX_PROPERTIES_SEED_0.asText("MyWorld", -7244175196467643454L), Outputs.DESC_PROPERTIES_SEED_0.asText("MyWorld", -7244175196467643454L)),
            Pair.of(Outputs.EX_PROPERTIES_SEED_1.asText(4258581462117660292L), Outputs.DESC_PROPERTIES_SEED_1.asText(4258581462117660292L)),
            Pair.of(Outputs.EX_PROPERTIES_SEED_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_SEED_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_SEED_3.asText(), Outputs.DESC_PROPERTIES_SEED_3.asText())
    ),

    PROPERTIES_SPAWN_POSITION(
            SpawnPosition.class,
            Outputs.USAGE_PROPERTIES_SPAWN_POSITION.asText(ArgKeys.WORLD, "<" + ArgKeys.POSITION_XYZ.t.toPlain() + ">"),
            Pair.of(Outputs.EX_PROPERTIES_SPAWN_POSITION_0.asText("MyWorld", Vector3d.from(100, 64, -100)), Outputs.DESC_PROPERTIES_SPAWN_POSITION_0.asText("MyWorld", Vector3d.from(100, 64, -100))),
            Pair.of(Outputs.EX_PROPERTIES_SPAWN_POSITION_1.asText(Vector3d.from(0, 80, 1500)), Outputs.DESC_PROPERTIES_SPAWN_POSITION_1.asText(Vector3d.from(0, 80, 1500))),
            Pair.of(Outputs.EX_PROPERTIES_SPAWN_POSITION_2.asText("MyWorld"), Outputs.DESC_PROPERTIES_SPAWN_POSITION_2.asText("MyWorld")),
            Pair.of(Outputs.EX_PROPERTIES_SPAWN_POSITION_3.asText(), Outputs.DESC_PROPERTIES_SPAWN_POSITION_3.asText())
    ),
    
    RENAME(
            Rename.class,
            Outputs.USAGE_RENAME.asText(ArgKeys.UNLOADED_WORLD, ArgKeys.NEW_NAME),
            Pair.of(Outputs.EX_RENAME_0.asText("MyWorld", "AnotherWorld"), Outputs.DESC_RENAME_0.asText("MyWorld", "AnotherWorld"))
    ),

    SCOREBOARD_OBJECTIVES_ADD(
            Add.class,
            Outputs.USAGE_SCOREBOARD_OBJECTIVES_ADD.asText(ArgKeys.WORLD, ArgKeys.OBJECTIVE_NAME, ArgKeys.CRITERION, ArgKeys.DISPLAY_NAME),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_ADD_0.asText("MyWorld", "obj1", Criteria.DUMMY), Outputs.DESC_SCOREBOARD_OBJECTIVES_ADD_0.asText("obj1", "MyWorld", Criteria.DUMMY)),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_ADD_1.asText("deaths", Criteria.DEATHS), Outputs.DESC_SCOREBOARD_OBJECTIVES_ADD_1.asText("deaths", Criteria.DEATHS)),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_ADD_2.asText("MyWorld", "home_sweet_home", OutputFormatter.noFormatCode + "stat.use_item.minecraft.wooden_door", "&bHomeland"), Outputs.DESC_SCOREBOARD_OBJECTIVES_ADD_2.asText("home_sweet_home", "MyWorld", OutputFormatter.noFormatCode + "stat.use_item.minecraft.wooden_door", Text.of(TextColors.AQUA, "Homeland"))),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_ADD_3.asText("life", Criteria.HEALTH, OutputFormatter.noFormatCode + "\"[{\\\"text\\\":\\\"Number\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"},{\\\"text\\\":\\\" of \\\",\\\"italic\\\":true,\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"hearts\\\",\\\"underlined\\\":true,\\\"color\\\":\\\"red\\\"}]\""), Outputs.DESC_SCOREBOARD_OBJECTIVES_ADD_3.asText("life", Criteria.HEALTH, Text.of(TextColors.BLUE, "Number", TextStyles.ITALIC, TextColors.WHITE, " of ", TextStyles.UNDERLINE, TextColors.RED, "hearts")))
    ),

    SCOREBOARD_OBJECTIVES_LIST(
            cosmos.commands.scoreboard.objectives.List.class,
            Outputs.USAGE_SCOREBOARD_OBJECTIVES_LIST.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_LIST_0.asText("MyWorld"), Outputs.DESC_SCOREBOARD_OBJECTIVES_LIST_0.asText("MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_LIST_1.asText(), Outputs.DESC_SCOREBOARD_OBJECTIVES_LIST_1.asText())
    ),

    SCOREBOARD_OBJECTIVES_MODIFY(
            Modify.class,
            Outputs.USAGE_SCOREBOARD_OBJECTIVES_MODIFY.asText(ArgKeys.WORLD, ArgKeys.OBJECTIVE, ArgKeys.MODIFY_COMMAND, ArgKeys.VALUE),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_MODIFY_0.asText("MyWorld", "life", ModifyCommands.RENDER_TYPE.getKey(), ObjectiveDisplayModes.HEARTS), Outputs.DESC_SCOREBOARD_OBJECTIVES_MODIFY_0.asText(ModifyCommands.RENDER_TYPE, "life", "MyWorld", ObjectiveDisplayModes.HEARTS)),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_MODIFY_1.asText("deaths", ModifyCommands.DISPLAY_NAME.getKey(), "&dWasted"), Outputs.DESC_SCOREBOARD_OBJECTIVES_MODIFY_1.asText(ModifyCommands.DISPLAY_NAME, "deaths", Text.of(TextColors.LIGHT_PURPLE, "Wasted"))),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_MODIFY_2.asText("kills", ModifyCommands.DISPLAY_NAME.getKey(), OutputFormatter.noFormatCode + "\"[{\\\"text\\\":\\\"Number\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"},{\\\"text\\\":\\\" of \\\",\\\"italic\\\":true,\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"kills\\\",\\\"underlined\\\":true,\\\"color\\\":\\\"red\\\"}]\""), Outputs.DESC_SCOREBOARD_OBJECTIVES_MODIFY_2.asText(ModifyCommands.DISPLAY_NAME, "kills", Text.of(TextColors.BLUE, "Number", TextStyles.ITALIC, TextColors.WHITE, " of ", TextStyles.UNDERLINE, TextColors.RED, "kills")))
    ),

    SCOREBOARD_OBJECTIVES_REMOVE(
            cosmos.commands.scoreboard.objectives.Remove.class,
            Outputs.USAGE_SCOREBOARD_OBJECTIVES_REMOVE.asText(ArgKeys.WORLD, ArgKeys.OBJECTIVE),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_REMOVE_0.asText("MyWorld", "obj1"), Outputs.DESC_SCOREBOARD_OBJECTIVES_REMOVE_0.asText("obj1", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_REMOVE_1.asText("obj2"), Outputs.DESC_SCOREBOARD_OBJECTIVES_REMOVE_1.asText("obj2"))
    ),

    SCOREBOARD_OBJECTIVES_SET_DISPLAY(
            SetDisplay.class,
            Outputs.USAGE_SCOREBOARD_OBJECTIVES_SET_DISPLAY.asText(ArgKeys.WORLD, ArgKeys.DISPLAY_SLOT, ArgKeys.OBJECTIVE),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_SET_DISPLAY_0.asText("MyWorld", DisplaySlots.BELOW_NAME, "health"), Outputs.DESC_SCOREBOARD_OBJECTIVES_SET_DISPLAY_0.asText("health", DisplaySlots.BELOW_NAME, "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_SET_DISPLAY_1.asText(DisplaySlots.LIST, "deaths"), Outputs.DESC_SCOREBOARD_OBJECTIVES_SET_DISPLAY_1.asText("deaths", DisplaySlots.LIST)),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_SET_DISPLAY_2.asText("MyWorld", DisplaySlots.LIST), Outputs.DESC_SCOREBOARD_OBJECTIVES_SET_DISPLAY_2.asText(DisplaySlots.LIST, "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_OBJECTIVES_SET_DISPLAY_3.asText(DisplaySlots.SIDEBAR), Outputs.DESC_SCOREBOARD_OBJECTIVES_SET_DISPLAY_3.asText(DisplaySlots.SIDEBAR))
    ),

    SCOREBOARD_PLAYERS_ADD(
            cosmos.commands.scoreboard.players.Add.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_ADD.asText(ArgKeys.WORLD, ArgKeys.TARGETS, ArgKeys.OBJECTIVE, ArgKeys.AMOUNT),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_ADD_0.asText("NotEvenEntity", "obj1", 5), Outputs.DESC_SCOREBOARD_PLAYERS_ADD_0.asText(5, "NotEvenEntity", "obj1")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_ADD_1.asText("*", "obj2", 2), Outputs.DESC_SCOREBOARD_PLAYERS_ADD_1.asText(2, "obj2", "*")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_ADD_2.asText("MyWorld", "@e[r=35]", "obj3", 2), Outputs.DESC_SCOREBOARD_PLAYERS_ADD_2.asText(-2, "obj3", "@e[r=35]", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_ADD_3.asText("&bSomeone", "obj4", 3), Outputs.DESC_SCOREBOARD_PLAYERS_ADD_3.asText(3, Text.of(TextColors.AQUA, "Someone"), "obj4")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_ADD_4.asText(OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"}\"", "obj5", 666), Outputs.DESC_SCOREBOARD_PLAYERS_ADD_4.asText(666, Text.of(TextStyles.BOLD, TextColors.BLUE, "Beast Player"), "obj5"))
    ),

    SCOREBOARD_PLAYERS_ENABLE(
            Enable.class,
            Text.EMPTY
    ),

    SCOREBOARD_PLAYERS_GET(
            Get.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_GET.asText(ArgKeys.WORLD, ArgKeys.TARGETS, ArgKeys.OBJECTIVE),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_GET_0.asText("MyWorld", "NotEvenEntity", "obj1"), Outputs.DESC_SCOREBOARD_PLAYERS_GET_0.asText("NotEvenEntity", "obj1", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_GET_1.asText("*", "obj2"), Outputs.DESC_SCOREBOARD_PLAYERS_GET_1.asText("*", "obj2")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_GET_2.asText("@e[r=35]", "obj3"), Outputs.DESC_SCOREBOARD_PLAYERS_GET_2.asText("@e[r=35]", "obj3")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_GET_3.asText("MyWorld", "&3Someone", "obj4"), Outputs.DESC_SCOREBOARD_PLAYERS_GET_3.asText(Text.of(TextColors.DARK_AQUA, "Someone"), "obj4", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_GET_4.asText(OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"}\"", "obj5"), Outputs.DESC_SCOREBOARD_PLAYERS_GET_4.asText(Text.of(TextStyles.BOLD, TextColors.BLUE, "Beast Player"), "obj5"))
    ),

    SCOREBOARD_PLAYERS_LIST(
            cosmos.commands.scoreboard.players.List.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_LIST.asText(ArgKeys.WORLD, ArgKeys.TARGETS),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_LIST_0.asText("MyWorld", "NotEvenEntity"), Outputs.DESC_SCOREBOARD_PLAYERS_LIST_0.asText("NotEvenEntity", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_LIST_1.asText(), Outputs.DESC_SCOREBOARD_PLAYERS_LIST_1.asText()),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_LIST_2.asText("*"), Outputs.DESC_SCOREBOARD_PLAYERS_LIST_2.asText("*")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_LIST_3.asText("MyWorld", "@e[r=35]"), Outputs.DESC_SCOREBOARD_PLAYERS_LIST_3.asText("@e[r=35]","MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_LIST_4.asText("&3Someone"), Outputs.DESC_SCOREBOARD_PLAYERS_LIST_4.asText(Text.of(TextColors.DARK_AQUA, "Someone"))),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_LIST_5.asText( OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"italic\\\":true,\\\"color\\\":\\\"gray\\\"}\""), Outputs.DESC_SCOREBOARD_PLAYERS_LIST_5.asText(Text.of(TextStyles.ITALIC, TextColors.GRAY, "Beast Player")))
    ),

    SCOREBOARD_PLAYERS_OPERATION(
            Operation.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_OPERATION.asText(ArgKeys.WORLD, ArgKeys.TARGETS, ArgKeys.OBJECTIVE, ArgKeys.OPERAND, ArgKeys.SOURCES, ArgKeys.SOURCE_OBJECTIVE),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_OPERATION_0.asText("MyWorld", "NotEvenEntity", "obj", Operands.PLUS, "Someone", "sourceObj"), Outputs.DESC_SCOREBOARD_PLAYERS_OPERATION_0.asText(Operands.PLUS, "Someone", "sourceObj", "NotEvenEntity", "obj", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_OPERATION_1.asText("*", "obj", Operands.TIMES, "&9Someone", "sourceObj"), Outputs.DESC_SCOREBOARD_PLAYERS_OPERATION_1.asText(Operands.TIMES, "*", "obj", Text.of(TextColors.BLUE, "Someone"), "sourceObj")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_OPERATION_2.asText("@e[r=35]", "obj", Operands.ASSIGN, "Player1", "sourceObj"), Outputs.DESC_SCOREBOARD_PLAYERS_OPERATION_2.asText(Operands.ASSIGN, "Player1", "sourceObj", "@e[r-35]", "obj")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_OPERATION_3.asText("MyWorld", OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"bold\\\":true,\\\"color\\\":\\\"light_purple\\\"}\"", "obj",Operands.SWAPS, "&eSun","sourceObj"), Outputs.DESC_SCOREBOARD_PLAYERS_OPERATION_3.asText(Operands.SWAPS, Text.of(TextStyles.BOLD, TextColors.LIGHT_PURPLE, "Beast Player"), "obj", Text.of(TextColors.YELLOW, "Sun"), "sourceObj", "MyWorld"))
    ),

    SCOREBOARD_PLAYERS_RANDOM(
            Random.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_RANDOM.asText(ArgKeys.WORLD, ArgKeys.TARGETS, ArgKeys.OBJECTIVE, ArgKeys.MIN, ArgKeys.MAX),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RANDOM_0.asText("MyWorld", "NotEvenEntity", "obj1", 0, 10), Outputs.DESC_SCOREBOARD_PLAYERS_RANDOM_0.asText(0, 10, "NotEvenEntity", "obj1", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RANDOM_1.asText("*", "obj2", -15, -5), Outputs.DESC_SCOREBOARD_PLAYERS_RANDOM_1.asText(-15, -5, "*", "obj2")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RANDOM_2.asText("@e[r=35]", "obj3", -5, 20), Outputs.DESC_SCOREBOARD_PLAYERS_RANDOM_2.asText(-5, 20, "@e[r=35]", "obj3")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RANDOM_3.asText("MyWorld", "&cSomeone", "obj5", 100, "*"), Outputs.DESC_SCOREBOARD_PLAYERS_RANDOM_3.asText(100, Integer.MAX_VALUE, Text.of(TextColors.RED, "Someone"), "obj5", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RANDOM_4.asText(OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"strikethrough\\\":true,\\\"color\\\":\\\"yellow\\\"}\"", "obj4", "*", 20), Outputs.DESC_SCOREBOARD_PLAYERS_RANDOM_4.asText(Integer.MIN_VALUE, 20, Text.of(TextStyles.STRIKETHROUGH, TextColors.YELLOW, "Beast Player"), "obj4"))
    ),

    SCOREBOARD_PLAYERS_REMOVE(
            cosmos.commands.scoreboard.players.Remove.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_REMOVE.asText(ArgKeys.WORLD, ArgKeys.TARGETS, ArgKeys.OBJECTIVE, ArgKeys.AMOUNT),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_REMOVE_0.asText("NotEvenEntity", "obj1", 5), Outputs.DESC_SCOREBOARD_PLAYERS_REMOVE_0.asText(5, "NotEvenEntity", "obj1")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_REMOVE_1.asText("*", "obj2", 2), Outputs.DESC_SCOREBOARD_PLAYERS_REMOVE_1.asText(2, "obj2", "*")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_REMOVE_2.asText("MyWorld", "@e[r=35]", "obj3", 2), Outputs.DESC_SCOREBOARD_PLAYERS_REMOVE_2.asText(-2, "obj3", "@e[r=35]", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_REMOVE_3.asText("&bSomeone", "obj4", 3), Outputs.DESC_SCOREBOARD_PLAYERS_REMOVE_3.asText(3, Text.of(TextColors.AQUA, "Someone"), "obj4")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_REMOVE_4.asText(OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"}\"", "obj5", 666), Outputs.DESC_SCOREBOARD_PLAYERS_REMOVE_4.asText(666, Text.of(TextStyles.BOLD, TextColors.BLUE, "Beast Player"), "obj5"))
    ),

    SCOREBOARD_PLAYERS_RESET(
            cosmos.commands.scoreboard.players.Reset.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_RESET.asText(ArgKeys.WORLD, ArgKeys.TARGETS, ArgKeys.OBJECTIVE),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RESET_0.asText("MyWorld", "NotEvenEntity", "obj1"), Outputs.DESC_SCOREBOARD_PLAYERS_RESET_0.asText("NotEvenEntity", "obj1", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RESET_1.asText("*"), Outputs.DESC_SCOREBOARD_PLAYERS_RESET_1.asText("*")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RESET_2.asText("MyWorld", "@e[r=35]", "obj3"), Outputs.DESC_SCOREBOARD_PLAYERS_RESET_2.asText("@e[r=35]", "obj3", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RESET_3.asText("&bSomeone", "obj4"), Outputs.DESC_SCOREBOARD_PLAYERS_RESET_3.asText(Text.of(TextColors.AQUA, "Someone"), "obj4")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_RESET_4.asText("MyWorld", OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"}\"", "obj5"), Outputs.DESC_SCOREBOARD_PLAYERS_RESET_4.asText(Text.of(TextStyles.BOLD, TextColors.BLUE, "Beast Player"), "obj5", "MyWorld"))
    ),

    SCOREBOARD_PLAYERS_SET(
            Set.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_SET.asText(ArgKeys.WORLD, ArgKeys.TARGETS, ArgKeys.OBJECTIVE, ArgKeys.SCORE),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_SET_0.asText("MyWorld", "NotEvenEntity", "obj1", 5), Outputs.DESC_SCOREBOARD_PLAYERS_SET_0.asText(5, "NotEvenEntity", "obj1", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_SET_1.asText("*", "obj2", -18), Outputs.DESC_SCOREBOARD_PLAYERS_SET_1.asText(-18, "*", "obj2")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_SET_2.asText("@e[r=35]", "obj3", 47), Outputs.DESC_SCOREBOARD_PLAYERS_SET_2.asText(47, "@e[r=35]", "obj3")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_SET_3.asText("MyWorld", "&bSomeone", "obj4", 0), Outputs.DESC_SCOREBOARD_PLAYERS_SET_3.asText(0, Text.of(TextColors.AQUA, "Someone"), "obj4", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_SET_4.asText(OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"}\"", "obj5", 9999), Outputs.DESC_SCOREBOARD_PLAYERS_SET_4.asText(9999, Text.of(TextStyles.BOLD, TextColors.BLUE, "Beast Player"), "obj5"))
    ),

    SCOREBOARD_PLAYERS_TAG_ADD(
            cosmos.commands.scoreboard.players.tag.Add.class,
            Text.EMPTY
    ),

    SCOREBOARD_PLAYERS_TAG_LIST(
            cosmos.commands.scoreboard.players.tag.List.class,
            Text.EMPTY
    ),

    SCOREBOARD_PLAYERS_TAG_REMOVE(
            cosmos.commands.scoreboard.players.tag.Remove.class,
            Text.EMPTY
    ),

    SCOREBOARD_PLAYERS_TEST(
            Test.class,
            Outputs.USAGE_SCOREBOARD_PLAYERS_TEST.asText(ArgKeys.WORLD, ArgKeys.TARGETS, ArgKeys.OBJECTIVE, ArgKeys.MIN, ArgKeys.MAX),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_TEST_0.asText("MyWorld", "NotEvenEntity", "obj", 0, 10), Outputs.DESC_SCOREBOARD_PLAYERS_TEST_0.asText("NotEvenEntity", "obj", "MyWorld", 0, 10)),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_TEST_1.asText("*", "obj2", -15, -5), Outputs.DESC_SCOREBOARD_PLAYERS_TEST_1.asText("*", "obj2", -15, -5)),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_TEST_2.asText("@e[r=35]", "obj3", -5, 20), Outputs.DESC_SCOREBOARD_PLAYERS_TEST_2.asText("@e[r=35]", "obj3", -5, 20)),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_TEST_3.asText("MyWorld", "&cSomeone", "obj5", 100), Outputs.DESC_SCOREBOARD_PLAYERS_TEST_3.asText(Text.of(TextColors.RED, "Someone"), "obj5", "MyWorld", 100, Integer.MAX_VALUE)),
            Pair.of(Outputs.EX_SCOREBOARD_PLAYERS_TEST_4.asText(OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"strikethrough\\\":true,\\\"color\\\":\\\"yellow\\\"}\"", "obj4", "*", 20), Outputs.DESC_SCOREBOARD_PLAYERS_TEST_4.asText(Text.of(TextStyles.STRIKETHROUGH, TextColors.YELLOW, "Beast Player"), "obj4", Integer.MIN_VALUE, 20))
    ),

    SCOREBOARD_TEAMS_ADD(
            cosmos.commands.scoreboard.teams.Add.class,
            Outputs.USAGE_SCOREBOARD_TEAMS_ADD.asText(ArgKeys.WORLD, ArgKeys.TEAM_NAME, ArgKeys.DISPLAY_NAME),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_ADD_0.asText("MyWorld", "team1", "TeamOne"), Outputs.DESC_SCOREBOARD_TEAMS_ADD_0.asText("obj1", "MyWorld", "TeamOne")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_ADD_1.asText("team2"), Outputs.DESC_SCOREBOARD_TEAMS_ADD_1.asText("team2")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_ADD_2.asText("MyWorld", "team3", "&bDreamers"), Outputs.DESC_SCOREBOARD_TEAMS_ADD_2.asText("team3", "MyWorld", Text.of(TextColors.AQUA, "Dreamers"))),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_ADD_3.asText("team4", OutputFormatter.noFormatCode + "\"[{\\\"text\\\":\\\"The\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"},{\\\"text\\\":\\\" French \\\",\\\"italic\\\":true,\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"Guys\\\",\\\"underlined\\\":true,\\\"color\\\":\\\"red\\\"}]\""), Outputs.DESC_SCOREBOARD_TEAMS_ADD_3.asText("team4", Text.of(TextColors.BLUE, "The", TextStyles.ITALIC, TextColors.WHITE, " French ", TextStyles.UNDERLINE, TextColors.RED, "Guys")))
    ),

    SCOREBOARD_TEAMS_EMPTY(
            Empty.class,
            Outputs.USAGE_SCOREBOARD_TEAMS_EMPTY.asText(ArgKeys.WORLD, ArgKeys.TEAM),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_EMPTY_0.asText("MyWorld", "team1"), Outputs.DESC_SCOREBOARD_TEAMS_EMPTY_0.asText("team1", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_EMPTY_1.asText("team2"), Outputs.DESC_SCOREBOARD_TEAMS_EMPTY_1.asText("team2"))
    ),

    SCOREBOARD_TEAMS_JOIN(
            Join.class,
            Outputs.USAGE_SCOREBOARD_TEAMS_JOIN.asText(ArgKeys.WORLD, ArgKeys.TEAM, ArgKeys.TARGETS),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_JOIN_0.asText("MyWorld", "team1", "NotEvenEntity"), Outputs.DESC_SCOREBOARD_TEAMS_JOIN_0.asText("Someone", "team1", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_JOIN_1.asText("team2", "*"), Outputs.DESC_SCOREBOARD_TEAMS_JOIN_1.asText("*", "team2")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_JOIN_2.asText("team3", "@e[r=35]"), Outputs.DESC_SCOREBOARD_TEAMS_JOIN_2.asText("@e[r=35]", "team3")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_JOIN_3.asText("MyWorld", "team4", "&9Someone"), Outputs.DESC_SCOREBOARD_TEAMS_JOIN_3.asText(Text.of(TextColors.BLUE, "Someone"), "team4", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_JOIN_4.asText("team5", OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"bold\\\":true,\\\"color\\\":\\\"yellow\\\"}\""), Outputs.DESC_SCOREBOARD_TEAMS_JOIN_4.asText(Text.of(TextStyles.BOLD, TextColors.YELLOW, "Beast Player"), "team5"))
    ),

    SCOREBOARD_TEAMS_LEAVE(
            Leave.class,
            Outputs.USAGE_SCOREBOARD_TEAMS_LEAVE.asText(ArgKeys.WORLD, ArgKeys.TARGETS),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_LEAVE_0.asText("MyWorld", "NotEvenEntity"), Outputs.DESC_SCOREBOARD_TEAMS_LEAVE_0.asText("Someone", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_LEAVE_1.asText("*"), Outputs.DESC_SCOREBOARD_TEAMS_LEAVE_1.asText("*")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_LEAVE_2.asText("@e[r=35]"), Outputs.DESC_SCOREBOARD_TEAMS_LEAVE_2.asText("@e[r=35]")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_LEAVE_3.asText("MyWorld", "&9Someone"), Outputs.DESC_SCOREBOARD_TEAMS_LEAVE_3.asText(Text.of(TextColors.BLUE, "Someone"), "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_LEAVE_4.asText(OutputFormatter.noFormatCode + "\"{\\\"text\\\":\\\"Beast Player\\\",\\\"bold\\\":true,\\\"color\\\":\\\"yellow\\\"}\""), Outputs.DESC_SCOREBOARD_TEAMS_LEAVE_4.asText(Text.of(TextStyles.BOLD, TextColors.YELLOW, "Beast Player")))
    ),

    SCOREBOARD_TEAMS_LIST(
            cosmos.commands.scoreboard.teams.List.class,
            Outputs.USAGE_SCOREBOARD_TEAMS_LIST.asText(ArgKeys.WORLD, ArgKeys.TEAM),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_LIST_0.asText("MyWorld"), Outputs.DESC_SCOREBOARD_TEAMS_LIST_0.asText("MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_LIST_1.asText("team1"), Outputs.DESC_SCOREBOARD_TEAMS_LIST_1.asText("team1"))
    ),

    SCOREBOARD_TEAMS_OPTION(
            Option.class,
            Outputs.USAGE_SCOREBOARD_TEAMS_OPTION.asText(ArgKeys.WORLD, ArgKeys.TEAM, ArgKeys.TEAM_OPTION, ArgKeys.VALUE),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_OPTION_0.asText("MyWorld", "team1", OutputFormatter.noFormatCode + TeamOptions.DEATH_MESSAGE_VISIBILITY.getKey(), Visibilities.HIDE_FOR_OWN_TEAM), Outputs.DESC_SCOREBOARD_TEAMS_OPTION_0.asText("MyWorld", TeamOptions.DEATH_MESSAGE_VISIBILITY, Visibilities.HIDE_FOR_OWN_TEAM, "team1")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_OPTION_1.asText("team2", TeamOptions.FRIENDLYFIRE.getKey(), false), Outputs.DESC_SCOREBOARD_TEAMS_OPTION_1.asText(TeamOptions.FRIENDLYFIRE, false, "team2")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_OPTION_2.asText("team3", TeamOptions.COLOR.getKey(), TextColors.AQUA), Outputs.DESC_SCOREBOARD_TEAMS_OPTION_2.asText(TeamOptions.COLOR, TextColors.AQUA, "team3"))
    ),

    SCOREBOARD_TEAMS_REMOVE(
            cosmos.commands.scoreboard.teams.Remove.class,
            Outputs.USAGE_SCOREBOARD_TEAMS_REMOVE.asText(ArgKeys.WORLD, ArgKeys.TEAM),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_REMOVE_0.asText("MyWorld", "team1"), Outputs.DESC_SCOREBOARD_TEAMS_REMOVE_0.asText("team1", "MyWorld")),
            Pair.of(Outputs.EX_SCOREBOARD_TEAMS_REMOVE_1.asText("team2"), Outputs.DESC_SCOREBOARD_TEAMS_REMOVE_1.asText("team2"))
    ),
    
    TIME_CALENDAR(
            Calendar.class,
            Outputs.USAGE_TIME_CALENDAR.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_TIME_CALENDAR_0.asText("MyWorld"), Outputs.DESC_TIME_CALENDAR_0.asText("MyWorld")),
            Pair.of(Outputs.EX_TIME_CALENDAR_1.asText(), Outputs.DESC_TIME_CALENDAR_1.asText())
    ),

    TIME_DAWN(
            Dawn.class,
            Outputs.USAGE_TIME_DAWN.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_TIME_DAWN_0.asText("MyWorld", Units.DAWN), Outputs.DESC_TIME_DAWN_0.asText("MyWorld", Units.DAWN)),
            Pair.of(Outputs.EX_TIME_DAWN_1.asText(), Outputs.DESC_TIME_DAWN_1.asText(Units.DAWN))
    ),

    TIME_DUSK(
            Dusk.class,
            Outputs.USAGE_TIME_DUSK.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_TIME_DUSK_0.asText("MyWorld", Units.DUSK), Outputs.DESC_TIME_DUSK_0.asText("MyWorld", Units.DUSK)),
            Pair.of(Outputs.EX_TIME_DUSK_1.asText(), Outputs.DESC_TIME_DUSK_1.asText(Units.DUSK))
    ),

    TIME_IGNORE_PLAYERS_SLEEPING(
            IgnorePlayersSleeping.class,
            Outputs.USAGE_TIME_IGNORE_PLAYERS_SLEEPING.asText(ArgKeys.WORLD, ArgKeys.STATE, "--" + ArgKeys.SAVE_CONFIG.t.toPlain()),
            Pair.of(Outputs.EX_TIME_IGNORE_PLAYERS_SLEEPING_0.asText("MyWorld", true), Outputs.DESC_TIME_IGNORE_PLAYERS_SLEEPING_0.asText(true, "MyWorld")),
            Pair.of(Outputs.EX_TIME_IGNORE_PLAYERS_SLEEPING_1.asText(), Outputs.DESC_TIME_IGNORE_PLAYERS_SLEEPING_1.asText()),
            Pair.of(Outputs.EX_TIME_IGNORE_PLAYERS_SLEEPING_2.asText("no", "--" + ArgKeys.SAVE_CONFIG.t.toPlain()), Outputs.DESC_TIME_IGNORE_PLAYERS_SLEEPING_2.asText("no")),
            Pair.of(Outputs.EX_TIME_IGNORE_PLAYERS_SLEEPING_3.asText("MyWorld", "--" + ArgKeys.SAVE_CONFIG.t.toPlain()), Outputs.DESC_TIME_IGNORE_PLAYERS_SLEEPING_3.asText("MyWorld"))
    ),

    TIME_MIDDAY(
            Midday.class,
            Outputs.USAGE_TIME_MIDDAY.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_TIME_MIDDAY_0.asText("MyWorld", Units.MIDDAY), Outputs.DESC_TIME_MIDDAY_0.asText("MyWorld", Units.MIDDAY)),
            Pair.of(Outputs.EX_TIME_MIDDAY_1.asText(), Outputs.DESC_TIME_MIDDAY_1.asText(Units.MIDDAY))
    ),

    TIME_MIDNIGHT(
            Midnight.class,
            Outputs.USAGE_TIME_MIDNIGHT.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_TIME_MIDNIGHT_0.asText("MyWorld", Units.MIDNIGHT), Outputs.DESC_TIME_MIDNIGHT_0.asText("MyWorld", Units.MIDNIGHT)),
            Pair.of(Outputs.EX_TIME_MIDNIGHT_1.asText(), Outputs.DESC_TIME_MIDNIGHT_1.asText(Units.MIDNIGHT))
    ),

    TIME_REAL_TIME(
            RealTime.class,
            Outputs.USAGE_TIME_REAL_TIME.asText(ArgKeys.WORLD, ArgKeys.STATE, "--" + ArgKeys.SAVE_CONFIG.t.toPlain()),
            Pair.of(Outputs.EX_TIME_REAL_TIME_0.asText("MyWorld", true), Outputs.DESC_TIME_REAL_TIME_0.asText(true, "MyWorld")),
            Pair.of(Outputs.EX_TIME_REAL_TIME_1.asText(), Outputs.DESC_TIME_REAL_TIME_1.asText()),
            Pair.of(Outputs.EX_TIME_REAL_TIME_2.asText("no", "--" + ArgKeys.SAVE_CONFIG.t.toPlain()), Outputs.DESC_TIME_REAL_TIME_2.asText("no")),
            Pair.of(Outputs.EX_TIME_REAL_TIME_3.asText("MyWorld", "--" + ArgKeys.SAVE_CONFIG.t.toPlain()), Outputs.DESC_TIME_REAL_TIME_3.asText("MyWorld"))
    ),

    TIME_SET(
            cosmos.commands.time.Set.class,
            Outputs.USAGE_TIME_SET.asText(ArgKeys.WORLD, ArgKeys.WORLD_TIME),
            Pair.of(Outputs.EX_TIME_SET_0.asText("MyWorld", 0), Outputs.DESC_TIME_SET_0.asText("MyWorld", 0)),
            Pair.of(Outputs.EX_TIME_SET_1.asText(138000), Outputs.DESC_TIME_SET_1.asText(138000))
    ),
    
    TIME_TOMORROW(
            Tomorrow.class,
            Outputs.USAGE_TIME_TOMORROW.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_TIME_TOMORROW_0.asText("MyWorld"), Outputs.DESC_TIME_TOMORROW_0.asText("MyWorld")),
            Pair.of(Outputs.EX_TIME_TOMORROW_1.asText(), Outputs.DESC_TIME_TOMORROW_1.asText())
    ),

    UNLOAD(
            Unload.class,
            Outputs.USAGE_UNLOAD.asText(ArgKeys.LOADED_WORLD),
            Pair.of(Outputs.EX_UNLOAD_0.asText("MyWorld"), Outputs.DESC_UNLOAD_0.asText("MyWorld"))
    ),

    VIEW_DISTANCE(
            ViewDistance.class,
            Outputs.USAGE_VIEW_DISTANCE.asText(ArgKeys.LOADED_WORLD, ArgKeys.DISTANCE_IN_CHUNKS),
            Pair.of(Outputs.EX_VIEW_DISTANCE_0.asText("MyWorld", 32), Outputs.DESC_VIEW_DISTANCE_0.asText("MyWorld", 32)),
            Pair.of(Outputs.EX_VIEW_DISTANCE_1.asText(3), Outputs.DESC_VIEW_DISTANCE_1.asText(3)),
            Pair.of(Outputs.EX_VIEW_DISTANCE_2.asText("MyWorld"), Outputs.DESC_VIEW_DISTANCE_2.asText("MyWorld")),
            Pair.of(Outputs.EX_VIEW_DISTANCE_3.asText(), Outputs.DESC_VIEW_DISTANCE_3.asText())
    ),
    
    WEATHER_FORECAST(
            Forecast.class,
            Outputs.USAGE_WEATHER_FORECAST.asText(ArgKeys.WORLD),
            Pair.of(Outputs.EX_WEATHER_FORECAST_0.asText("MyWorld"), Outputs.DESC_WEATHER_FORECAST_0.asText("MyWorld")),
            Pair.of(Outputs.EX_WEATHER_FORECAST_1.asText(), Outputs.DESC_WEATHER_FORECAST_1.asText())
    ),


    WEATHER_RAIN(
            Rain.class,
            Outputs.USAGE_WEATHER_RAIN.asText(ArgKeys.WORLD, ArgKeys.DURATION_SECONDS),
            Pair.of(Outputs.EX_WEATHER_RAIN_0.asText("MyWorld", 150), Outputs.DESC_WEATHER_RAIN_0.asText("MyWorld", 150)),
            Pair.of(Outputs.EX_WEATHER_RAIN_1.asText(), Outputs.DESC_WEATHER_RAIN_1.asText())
    ),


    WEATHER_SUN(
            Sun.class,
            Outputs.USAGE_WEATHER_SUN.asText(ArgKeys.WORLD, ArgKeys.DURATION_SECONDS),
            Pair.of(Outputs.EX_WEATHER_SUN_0.asText("MyWorld", 300), Outputs.DESC_WEATHER_SUN_0.asText("MyWorld", 300)),
            Pair.of(Outputs.EX_WEATHER_SUN_1.asText(), Outputs.DESC_WEATHER_SUN_1.asText())
    ),

    WEATHER_THUNDER(
            Thunder.class,
            Outputs.USAGE_WEATHER_THUNDER.asText(ArgKeys.WORLD, ArgKeys.DURATION_SECONDS),
            Pair.of(Outputs.EX_WEATHER_THUNDER_0.asText("MyWorld", 60), Outputs.DESC_WEATHER_THUNDER_0.asText("MyWorld", 60)),
            Pair.of(Outputs.EX_WEATHER_THUNDER_1.asText(), Outputs.DESC_WEATHER_THUNDER_1.asText())
    );

    private final Text text;
    private final Text flatText;

    Helps(Class<? extends AbstractCommand> commandClass, Text usage, Pair<Text, Text>... exampleTuples) {
        String permission = commandClass.getPackage().getName() + "." + commandClass.getSimpleName().toLowerCase();

        String name = WordUtils.capitalizeFully(
                String.join(" ", name().split("_"))
        );

        Text bulletPoint = Text.of(TextColors.WHITE, " • ");
        Text breakLine = Text.of(Text.NEW_LINE, Text.NEW_LINE);

        List<Text> exampleTexts = IntStream
                .range(0, exampleTuples.length)
                .distinct()
                .boxed()
                .map(index -> Text.of(
                        index + 1, ") ",
                        TextColors.GRAY, "Example:",
                        Text.NEW_LINE,
                        exampleTuples[index].getLeft(),
                        Text.NEW_LINE,
                        "Description:",
                        Text.NEW_LINE,
                        exampleTuples[index].getRight()
                ))
                .collect(Collectors.toList());

        Text examplesHoverText = Text.of(
                TextColors.WHITE, name,
                breakLine,
                Text.joinWith(breakLine, exampleTexts)
        );

        Text examplesText = exampleTexts.isEmpty() ? Text.EMPTY : Text.builder()
                .append(Text.of(TextColors.BLUE, TextStyles.UNDERLINE, "Examples"))
                .onHover(TextActions.showText(examplesHoverText))
                .build();

        Text usagesWithActionsText = Text.builder().append(usage)
                .onHover(TextActions.showText(Text.of("Click to copy")))
                .onClick(TextActions.suggestCommand("/" + usage.toPlain()))
                .build();

        Text aliasesText = Text.of(TextColors.GREEN, String.join(", ", Aliases.fromClass(commandClass)));

        Text common = Text.of(
                TextColors.WHITE, name,
                breakLine,
                bulletPoint, TextColors.GRAY, "Usage: ",
                Text.NEW_LINE,
                usagesWithActionsText,
                breakLine,
                bulletPoint, TextColors.GRAY, "Aliases: ",
                Text.NEW_LINE,
                aliasesText,
                breakLine,
                bulletPoint, TextColors.GRAY, "Command permission: ",
                Text.NEW_LINE,
                TextColors.GREEN, permission
        );

        text = Text.of(common, breakLine, examplesText);

        flatText = Text.of(
                bulletPoint, TextColors.GRAY, name, ": ",
                Text.builder()
                        .append(Text.of(TextColors.BLUE, TextStyles.UNDERLINE, "Details"))
                        .onHover(TextActions.showText(common))
                        .build(),
                ", ",
                examplesText,
                ", ",
                Text.builder()
                        .append(Text.of(TextColors.BLUE, TextStyles.UNDERLINE, "Use"))
                        .onHover(TextActions.showText(Text.of("Click to copy")))
                        .onClick(TextActions.suggestCommand("/" + usage.toPlain()))
                        .build()
        );
    }

    public Text getText() {
        return text;
    }

    public Text getFlatText() {
        return flatText;
    }
}
