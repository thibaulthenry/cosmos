package cosmos.executors.parameters;

import cosmos.constants.Operands;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.scoreboard.criteria.Criterion;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gamerule.GameRule;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.math.vector.Vector2d;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class CosmosParameters {

    public static final Parameter.Value<BlockType> BLOCK_TYPE = Parameter.registryElement(TypeToken.get(BlockType.class), RegistryTypes.BLOCK_TYPE)
            .setKey(CosmosKeys.BLOCK_TYPE)
            .build();

    public static final Parameter.Value<NamedTextColor> COLOR = Parameter.builder(NamedTextColor.class)
            .setKey(CosmosKeys.COLOR)
            .parser(CosmosValueParameters.COLORS)
            .build();

    public static final Parameter.Value<Criterion> CRITERION = Parameter.registryElement(TypeToken.get(Criterion.class), RegistryTypes.CRITERION)
            .setKey(CosmosKeys.CRITERION)
            .build();

    public static final Parameter.Value<Difficulty> DIFFICULTY_OPTIONAL = Parameter.registryElement(TypeToken.get(Difficulty.class), RegistryTypes.DIFFICULTY)
            .setKey(CosmosKeys.DIFFICULTY)
            .optional()
            .build();

    public static final Parameter.Value<Direction> DIRECTION = Parameter.enumValue(Direction.class)
            .setKey(CosmosKeys.DIRECTION)
            .build();

    public static final Parameter.Value<ObjectiveDisplayMode> DISPLAY_MODE = Parameter.registryElement(TypeToken.get(ObjectiveDisplayMode.class), RegistryTypes.OBJECTIVE_DISPLAY_MODE)
            .setKey(CosmosKeys.DISPLAY_MODE)
            .build();

    public static final Parameter.Value<DisplaySlot> DISPLAY_SLOT = Parameter.registryElement(TypeToken.get(DisplaySlot.class), RegistryTypes.DISPLAY_SLOT)
            .setKey(CosmosKeys.DISPLAY_SLOT)
            .build();

    private static final Parameter.Value<Long> DURATION = Parameter.longNumber().setKey(CosmosKeys.DURATION).build();

    public static final Parameter.Value<List<Entity>> ENTITY_TARGETS = Parameter.builder(new TypeToken<List<Entity>>() {})
            .setKey(CosmosKeys.ENTITY_TARGETS)
            .parser(ResourceKeyedValueParameters.MANY_ENTITIES)
            .build();

    public static final Parameter.Value<List<Entity>> ENTITY_TARGETS_OPTIONAL = Parameter.builder(new TypeToken<List<Entity>>() {})
            .setKey(CosmosKeys.ENTITY_TARGETS)
            .parser(ResourceKeyedValueParameters.MANY_ENTITIES)
            .optional()
            .build();

    public static final Parameter.Value<GameMode> GAME_MODE_OPTIONAL = Parameter.registryElement(TypeToken.get(GameMode.class), RegistryTypes.GAME_MODE)
            .setKey(CosmosKeys.GAME_MODE)
            .optional()
            .build();

    public static final Parameter.Value<GameRule<?>> GAME_RULE = Parameter.registryElement(new TypeToken<GameRule<?>>() {}, RegistryTypes.GAME_RULE)
            .setKey(CosmosKeys.GAME_RULE)
            .build(); // todo check pr

    public static final Parameter.Value<ItemType> ITEM_TYPE = Parameter.registryElement(TypeToken.get(ItemType.class), RegistryTypes.ITEM_TYPE)
            .setKey(CosmosKeys.ITEM_TYPE)
            .build();

    public static final Parameter.Value<ParticleType> PARTICLE_TYPE = Parameter.registryElement(TypeToken.get(ParticleType.class), RegistryTypes.PARTICLE_TYPE)
            .setKey(CosmosKeys.PARTICLE_TYPE)
            .build();

    public static final Parameter.Value<BlockType> PORTAL_BLOCK_TYPE = Parameter.builder(BlockType.class)
            .setKey(CosmosKeys.BLOCK_TYPE)
            .parser(CosmosValueParameters.PORTAL_BLOCK_TYPES)
            .build();

    public static final Parameter.Value<PortalType> PORTAL_TYPE = Parameter.registryElement(TypeToken.get(PortalType.class), RegistryTypes.PORTAL_TYPE)
            .setKey(CosmosKeys.PORTAL_TYPE)
            .build();

    public static final Parameter.Value<Vector2d> POSITION_2D_OPTIONAL = Parameter.builder(Vector2d.class)
            .setKey(CosmosKeys.X_Z)
            .parser(ResourceKeyedValueParameters.VECTOR2D)
            .optional()
            .build();

    public static final Parameter.Value<PotionEffectType> POTION_EFFECT_TYPE = Parameter.registryElement(TypeToken.get(PotionEffectType.class), RegistryTypes.POTION_EFFECT_TYPE)
            .setKey(CosmosKeys.POTION_EFFECT_TYPE)
            .build();

    public static final Parameter.Value<Operands> SCOREBOARD_OPERANDS = Parameter.builder(Operands.class)
            .setKey(CosmosKeys.OPERAND)
            .parser(CosmosValueParameters.SCOREBOARD_OPERANDS)
            .build();

    public static final Parameter.Value<Operands> STANDARD_OPERAND = Parameter.builder(Operands.class)
            .setKey(CosmosKeys.OPERAND)
            .parser(CosmosValueParameters.STANDARD_OPERANDS)
            .build();

    public static final Parameter.Value<Component> TEXT_AMPERSAND = Parameter.formattingCodeText().setKey(CosmosKeys.TEXT_AMPERSAND).build();

    public static final Parameter.Value<Component> TEXT_JSON = Parameter.jsonText().setKey(CosmosKeys.TEXT_JSON).build();

    public static final Parameter TEXTS_ALL = Parameter.firstOfBuilder(TEXT_AMPERSAND)
            .orFirstOf(TEXT_JSON)
            .optional()
            .build();

    public static final Parameter TEXTS_ALL_OPTIONAL = Parameter.firstOfBuilder(TEXT_AMPERSAND)
            .orFirstOf(TEXT_JSON)
            .optional()
            .build();

    private static final Parameter.Value<ChronoUnit> TIME_UNIT_OPTIONAL = Parameter.builder(ChronoUnit.class)
            .setKey(CosmosKeys.TIME_UNIT)
            .parser(CosmosValueParameters.TIME_UNIT)
            .optional()
            .build();

    public static final Parameter DURATION_WITH_TIME_UNIT = Parameter.seqBuilder(DURATION)
            .then(TIME_UNIT_OPTIONAL)
            .build();

    public static final Parameter DURATION_WITH_TIME_UNIT_OPTIONAL = Parameter.seqBuilder(DURATION)
            .then(TIME_UNIT_OPTIONAL)
            .optional()
            .build();

    public static final Parameter.Value<Visibility> VISIBILITY = Parameter.registryElement(TypeToken.get(Visibility.class), RegistryTypes.VISIBILITY)
            .setKey(CosmosKeys.VISIBILITY)
            .build();

}
