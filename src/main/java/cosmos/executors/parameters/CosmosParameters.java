package cosmos.executors.parameters;

import cosmos.constants.Operands;
import cosmos.executors.parameters.builders.CosmosBuilder;
import cosmos.executors.parameters.builders.CosmosFirstOfBuilder;
import cosmos.executors.parameters.builders.backup.Backup;
import cosmos.executors.parameters.builders.backup.BackupWorld;
import cosmos.executors.parameters.builders.gamerule.GameRuleValueAll;
import cosmos.executors.parameters.builders.portal.PortalAll;
import cosmos.executors.parameters.builders.portal.PortalFrame;
import cosmos.executors.parameters.builders.portal.PortalTypeCosmos;
import cosmos.executors.parameters.builders.scoreboard.Extremum;
import cosmos.executors.parameters.builders.scoreboard.ObjectiveAll;
import cosmos.executors.parameters.builders.scoreboard.ObjectiveTrigger;
import cosmos.executors.parameters.builders.scoreboard.Targets;
import cosmos.executors.parameters.builders.scoreboard.TeamAll;
import cosmos.executors.parameters.builders.world.WorldAll;
import cosmos.executors.parameters.builders.world.WorldExported;
import cosmos.executors.parameters.builders.world.WorldOffline;
import cosmos.executors.parameters.builders.world.WorldOnline;
import cosmos.registries.backup.BackupArchetype;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosPortal;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.objective.Objective;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Supplier;

public class CosmosParameters {

    public static class Builder {

        public static final Supplier<CosmosBuilder<BackupArchetype>> BACKUP = Backup::new;

        public static final Supplier<CosmosBuilder<ResourceKey>> BACKUP_WORLD = BackupWorld::new;

        public static final Supplier<Parameter.Value.Builder<NamedTextColor>> COLOR = () -> Parameter
                .builder(NamedTextColor.class)
                .parser(Parser.COLORS)
                .setKey(CosmosKeys.COLOR);

        public static final Supplier<Parameter.SequenceBuilder> DURATION_WITH_TIME_UNIT = () -> Parameter
                .seqBuilder(Parameter.longNumber().setKey(CosmosKeys.DURATION).build())
                .then(
                        Parameter.builder(ChronoUnit.class)
                                .setKey(CosmosKeys.TIME_UNIT)
                                .parser(Parser.TIME_UNIT)
                                .optional()
                                .build()
                );

        public static final Supplier<Parameter.Value.Builder<List<Entity>>> ENTITIES = () -> Parameter
                .builder(new TypeToken<List<Entity>>() {})
                .parser(ResourceKeyedValueParameters.MANY_ENTITIES);

        public static final Supplier<Extremum> EXTREMUM = Extremum::new;

        public static final Supplier<CosmosBuilder<Object>> GAME_RULE_VALUE_ALL = GameRuleValueAll::new;

        public static final Supplier<CosmosBuilder<Objective>> OBJECTIVE_ALL = ObjectiveAll::new;

        public static final Supplier<CosmosBuilder<Objective>> OBJECTIVE_TRIGGER = ObjectiveTrigger::new;

        public static final Supplier<CosmosBuilder<CosmosPortal>> PORTAL_ALL = PortalAll::new;

        public static final Supplier<Parameter.Value.Builder<BlockType>> PORTAL_BLOCK_TYPE = () -> Parameter
                .builder(BlockType.class)
                .parser(Parser.PORTAL_BLOCK_TYPES);

        public static final Supplier<CosmosBuilder<CosmosFramePortal>> PORTAL_FRAME = PortalFrame::new;

        public static final Supplier<CosmosBuilder<CosmosPortalType>> PORTAL_TYPE_COSMOS = PortalTypeCosmos::new;

        public static final Supplier<Parameter.Value.Builder<Operands>> SCOREBOARD_OPERANDS = () -> Parameter
                .builder(Operands.class)
                .parser(Parser.SCOREBOARD_OPERANDS);

        public static final Supplier<Parameter.Value.Builder<Operands>> STANDARD_OPERAND = () -> Parameter
                .builder(Operands.class)
                .parser(Parser.STANDARD_OPERANDS);

        public static final Supplier<CosmosFirstOfBuilder> TARGETS = Targets::new;

        public static final Supplier<CosmosBuilder<Team>> TEAM_ALL = TeamAll::new;

        public static final Supplier<Parameter.FirstOfBuilder> TEXTS_ALL = () -> Parameter
                .firstOfBuilder(Parameter.formattingCodeText().setKey(CosmosKeys.TEXT_AMPERSAND).build())
                .orFirstOf(Parameter.jsonText().setKey(CosmosKeys.TEXT_JSON).build());

        public static final Supplier<CosmosBuilder<ResourceKey>> WORLD_ALL = WorldAll::new;

        public static final Supplier<CosmosBuilder<ResourceKey>> WORLD_EXPORTED = WorldExported::new;

        public static final Supplier<CosmosBuilder<ResourceKey>> WORLD_OFFLINE = WorldOffline::new;

        public static final Supplier<CosmosBuilder<ResourceKey>> WORLD_ONLINE = WorldOnline::new;

    }

    private static class Parser {

        private static final ValueParameter<NamedTextColor> COLORS = VariableValueParameters.staticChoicesBuilder(NamedTextColor.class)
                .choice(NamedTextColor.AQUA.toString(), NamedTextColor.AQUA)
                .choice(NamedTextColor.BLACK.toString(), NamedTextColor.BLACK)
                .choice(NamedTextColor.BLUE.toString(), NamedTextColor.BLUE)
                .choice(NamedTextColor.DARK_AQUA.toString(), NamedTextColor.DARK_AQUA)
                .choice(NamedTextColor.DARK_BLUE.toString(), NamedTextColor.DARK_BLUE)
                .choice(NamedTextColor.DARK_GRAY.toString(), NamedTextColor.DARK_GRAY)
                .choice(NamedTextColor.DARK_GREEN.toString(), NamedTextColor.DARK_GREEN)
                .choice(NamedTextColor.DARK_PURPLE.toString(), NamedTextColor.DARK_PURPLE)
                .choice(NamedTextColor.DARK_RED.toString(), NamedTextColor.DARK_RED)
                .choice(NamedTextColor.GOLD.toString(), NamedTextColor.GOLD)
                .choice(NamedTextColor.GRAY.toString(), NamedTextColor.GRAY)
                .choice(NamedTextColor.GREEN.toString(), NamedTextColor.GREEN)
                .choice(NamedTextColor.LIGHT_PURPLE.toString(), NamedTextColor.LIGHT_PURPLE)
                .choice(NamedTextColor.RED.toString(), NamedTextColor.RED)
                .choice("reset", NamedTextColor.WHITE)
                .choice(NamedTextColor.WHITE.toString(), NamedTextColor.WHITE)
                .choice(NamedTextColor.YELLOW.toString(), NamedTextColor.YELLOW)
                .build();

        private static final ValueParameter<BlockType> PORTAL_BLOCK_TYPES = VariableValueParameters.staticChoicesBuilder(BlockType.class)
                .choice(BlockTypes.LAVA.location().getFormatted(), BlockTypes.LAVA.get())
                .choice(BlockTypes.VOID_AIR.location().getFormatted(), BlockTypes.VOID_AIR.get())
                .choice(BlockTypes.WATER.location().getFormatted(), BlockTypes.WATER.get())
                .build();

        private static final ValueParameter<Operands> SCOREBOARD_OPERANDS = VariableValueParameters.staticChoicesBuilder(Operands.class)
                .choice(Operands.PLUS.getOperand(), Operands.PLUS)
                .choice(Operands.MINUS.getOperand(), Operands.MINUS)
                .choice(Operands.TIMES.getOperand(), Operands.TIMES)
                .choice(Operands.DIVIDE.getOperand(), Operands.DIVIDE)
                .choice(Operands.MODULUS.getOperand(), Operands.MODULUS)
                .choice(Operands.ASSIGN.getOperand(), Operands.ASSIGN)
                .choice(Operands.MIN.getOperand(), Operands.MIN)
                .choice(Operands.MAX.getOperand(), Operands.MAX)
                .choice(Operands.SWAPS.getOperand(), Operands.SWAPS)
                .build();

        private static final ValueParameter<Operands> STANDARD_OPERANDS = VariableValueParameters.staticChoicesBuilder(Operands.class)
                .choice(Operands.PLUS.getOperand(), Operands.PLUS)
                .choice(Operands.MINUS.getOperand(), Operands.MINUS)
                .choice(Operands.TIMES.getOperand(), Operands.TIMES)
                .choice(Operands.DIVIDE.getOperand(), Operands.DIVIDE)
                .build();

        private static final ValueParameter<ChronoUnit> TIME_UNIT = VariableValueParameters.staticChoicesBuilder(ChronoUnit.class)
                .choice("milliseconds", ChronoUnit.MILLIS)
                .choice("seconds", ChronoUnit.SECONDS)
                .choice("minutes", ChronoUnit.MINUTES)
                .choice("hours", ChronoUnit.HOURS)
                .choice("days", ChronoUnit.DAYS)
                .choice("weeks", ChronoUnit.WEEKS)
                .choice("months", ChronoUnit.MONTHS)
                .choice("years", ChronoUnit.YEARS)
                .build();

    }

}
