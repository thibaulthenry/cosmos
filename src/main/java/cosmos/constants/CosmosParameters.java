package cosmos.constants;

import cosmos.executors.parameters.CosmosBuilder;
import cosmos.executors.parameters.backup.Backup;
import cosmos.executors.parameters.backup.BackupWorld;
import cosmos.executors.parameters.portal.PortalAll;
import cosmos.executors.parameters.portal.PortalBlockTypeAll;
import cosmos.executors.parameters.portal.PortalTypeCosmos;
import cosmos.executors.parameters.properties.GameRuleValueAll;
import cosmos.executors.parameters.scoreboard.Extremum;
import cosmos.executors.parameters.scoreboard.ObjectiveAll;
import cosmos.executors.parameters.scoreboard.ObjectiveTrigger;
import cosmos.executors.parameters.scoreboard.Targets;
import cosmos.executors.parameters.scoreboard.TeamAll;
import cosmos.executors.parameters.time.DurationWithUnit;
import cosmos.executors.parameters.world.WorldAll;
import cosmos.executors.parameters.world.WorldDistinct;
import cosmos.executors.parameters.world.WorldExported;
import cosmos.executors.parameters.world.WorldOffline;
import cosmos.executors.parameters.world.WorldOnline;
import cosmos.registries.backup.BackupArchetype;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosPortal;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.util.List;
import java.util.function.Supplier;

public class CosmosParameters {

    public static final Supplier<CosmosBuilder<BackupArchetype>> BACKUP = Backup::new;

    public static final Supplier<CosmosBuilder<ResourceKey>> BACKUP_WORLD = BackupWorld::new;

    public static final Supplier<Parameter.Value.Builder<NamedTextColor>> COLOR = () -> Parameter
            .builder(NamedTextColor.class)
            .addParser(Parser.COLORS)
            .key(CosmosKeys.COLOR);

    public static final Supplier<DurationWithUnit> DURATION_WITH_UNIT = DurationWithUnit::new;

    public static final Supplier<Parameter.Value.Builder<List<Entity>>> ENTITIES = () -> Parameter
            .builder(new TypeToken<List<Entity>>() {})
            .addParser(ResourceKeyedValueParameters.MANY_ENTITIES);

    public static final Supplier<Extremum> EXTREMUM = Extremum::new;

    public static final Supplier<CosmosBuilder<Object>> GAME_RULE_VALUE_ALL = GameRuleValueAll::new;

    public static final Supplier<CosmosBuilder<Objective>> OBJECTIVE_ALL = ObjectiveAll::new;

    public static final Supplier<CosmosBuilder<Objective>> OBJECTIVE_TRIGGER = ObjectiveTrigger::new;

    public static final Supplier<CosmosBuilder<CosmosPortal>> PORTAL_ALL = PortalAll::new;

    public static final Supplier<CosmosBuilder<BlockType>> PORTAL_BLOCK_TYPE = PortalBlockTypeAll::new;

    public static final Supplier<Parameter.Value.Builder<DelayFormat>> PORTAL_DELAY_FORMAT = () -> Parameter
            .builder(DelayFormat.class)
            .addParser(Parser.PORTAL_DELAY_TIMER_FORMATS);

    public static final Supplier<CosmosBuilder<CosmosPortalType>> PORTAL_TYPE_COSMOS = PortalTypeCosmos::new;

    public static final Supplier<Parameter.Value.Builder<Operands>> SCOREBOARD_OPERANDS = () -> Parameter
            .builder(Operands.class)
            .addParser(Parser.SCOREBOARD_OPERANDS);

    // TODO https://github.com/SpongePowered/Sponge/issues/3343
    public static final Supplier<Targets> TARGETS = Targets::new;

    public static final Supplier<CosmosBuilder<Team>> TEAM_ALL = TeamAll::new;

    public static final Supplier<Parameter.FirstOfBuilder> TEXTS_ALL = () -> Parameter
            .firstOfBuilder(Parameter.formattingCodeText().key(CosmosKeys.TEXT_AMPERSAND).build())
            .orFirstOf(Parameter.jsonText().key(CosmosKeys.TEXT_JSON).build());

    public static final Supplier<CosmosBuilder<ResourceKey>> WORLD_ALL = WorldAll::new;

    public static final Supplier<WorldDistinct> WORLD_DISTINCT = WorldDistinct::new;

    public static final Supplier<CosmosBuilder<ResourceKey>> WORLD_EXPORTED = WorldExported::new;

    public static final Supplier<CosmosBuilder<ResourceKey>> WORLD_OFFLINE = WorldOffline::new;

    public static final Supplier<CosmosBuilder<ResourceKey>> WORLD_ONLINE = WorldOnline::new;

    private static class Parser {

        private static final ValueParameter<NamedTextColor> COLORS = VariableValueParameters.staticChoicesBuilder(NamedTextColor.class)
                .addChoice(NamedTextColor.AQUA.toString(), NamedTextColor.AQUA)
                .addChoice(NamedTextColor.BLACK.toString(), NamedTextColor.BLACK)
                .addChoice(NamedTextColor.BLUE.toString(), NamedTextColor.BLUE)
                .addChoice(NamedTextColor.DARK_AQUA.toString(), NamedTextColor.DARK_AQUA)
                .addChoice(NamedTextColor.DARK_BLUE.toString(), NamedTextColor.DARK_BLUE)
                .addChoice(NamedTextColor.DARK_GRAY.toString(), NamedTextColor.DARK_GRAY)
                .addChoice(NamedTextColor.DARK_GREEN.toString(), NamedTextColor.DARK_GREEN)
                .addChoice(NamedTextColor.DARK_PURPLE.toString(), NamedTextColor.DARK_PURPLE)
                .addChoice(NamedTextColor.DARK_RED.toString(), NamedTextColor.DARK_RED)
                .addChoice(NamedTextColor.GOLD.toString(), NamedTextColor.GOLD)
                .addChoice(NamedTextColor.GRAY.toString(), NamedTextColor.GRAY)
                .addChoice(NamedTextColor.GREEN.toString(), NamedTextColor.GREEN)
                .addChoice(NamedTextColor.LIGHT_PURPLE.toString(), NamedTextColor.LIGHT_PURPLE)
                .addChoice(NamedTextColor.RED.toString(), NamedTextColor.RED)
                .addChoice("reset", NamedTextColor.WHITE)
                .addChoice(NamedTextColor.WHITE.toString(), NamedTextColor.WHITE)
                .addChoice(NamedTextColor.YELLOW.toString(), NamedTextColor.YELLOW)
                .build();

        private static final ValueParameter<DelayFormat> PORTAL_DELAY_TIMER_FORMATS = VariableValueParameters.staticChoicesBuilder(DelayFormat.class)
                .addChoice(DataKeys.Portal.Delay.Format.DIGITAL_CLOCK.value(), DelayFormat.DIGITAL_CLOCK)
                .addChoice(DataKeys.Portal.Delay.Format.SECONDS.value(), DelayFormat.SECONDS)
                .addChoice(DataKeys.Portal.Delay.Format.PERCENTAGE.value(), DelayFormat.PERCENTAGE)
                .addChoice(DataKeys.Portal.Delay.Format.TICKS.value(), DelayFormat.TICKS)
                .build();

        private static final ValueParameter<Operands> SCOREBOARD_OPERANDS = VariableValueParameters.staticChoicesBuilder(Operands.class)
                .addChoice(Operands.PLUS.operand(), Operands.PLUS)
                .addChoice(Operands.MINUS.operand(), Operands.MINUS)
                .addChoice(Operands.TIMES.operand(), Operands.TIMES)
                .addChoice(Operands.DIVIDE.operand(), Operands.DIVIDE)
                .addChoice(Operands.MODULUS.operand(), Operands.MODULUS)
                .addChoice(Operands.ASSIGN.operand(), Operands.ASSIGN)
                .addChoice(Operands.MIN.operand(), Operands.MIN)
                .addChoice(Operands.MAX.operand(), Operands.MAX)
                .addChoice(Operands.SWAPS.operand(), Operands.SWAPS)
                .build();

    }

}
