package cosmos.constants;

import cosmos.registries.backup.BackupArchetype;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.listener.Listener;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosPortal;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.scoreboard.CollisionRule;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.scoreboard.criteria.Criterion;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gamerule.GameRule;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.math.vector.Vector2d;
import org.spongepowered.math.vector.Vector3d;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class CosmosKeys {

    public static final Parameter.Key<Integer> AMOUNT = Parameter.key("amount", Integer.class);
    public static final Parameter.Key<Double> AMOUNT_DOUBLE = Parameter.key("amount", Double.class);
    public static final Parameter.Key<BackupArchetype> BACKUP = Parameter.key("backup", BackupArchetype.class);
    public static final Parameter.Key<BlockType> BLOCK_TYPE = Parameter.key("block-type", BlockType.class);
    public static final Parameter.Key<Integer> BLOCKS = Parameter.key("blocks", Integer.class);
    public static final Parameter.Key<Integer> CHUNKS = Parameter.key("chunks", Integer.class);
    public static final Parameter.Key<CollisionRule> COLLISION_RULE = Parameter.key("collision-rule", CollisionRule.class);
    public static final Parameter.Key<NamedTextColor> COLOR = Parameter.key("color", NamedTextColor.class);
    public static final Parameter.Key<Criterion> CRITERION = Parameter.key("criterion", Criterion.class);
    public static final Parameter.Key<Double> DIAMETER = Parameter.key("end-diameter", Double.class);
    public static final Parameter.Key<Difficulty> DIFFICULTY = Parameter.key("difficulty", Difficulty.class);
    public static final Parameter.Key<Direction> DIRECTION = Parameter.key("direction", Direction.class);
    public static final Parameter.Key<ObjectiveDisplayMode> DISPLAY_MODE = Parameter.key("display-mode", ObjectiveDisplayMode.class);
    public static final Parameter.Key<DisplaySlot> DISPLAY_SLOT = Parameter.key("display-slot", DisplaySlot.class);
    public static final Parameter.Key<Double> DISTANCE = Parameter.key("distance", Double.class);
    public static final Parameter.Key<Long> DURATION = Parameter.key("duration", Long.class);
    public static final Parameter.Key<Double> END_DIAMETER = Parameter.key("end-diameter", Double.class);
    public static final Parameter.Key<List<Entity>> ENTITIES = Parameter.key("targets", new TypeToken<List<Entity>>() {});
    public static final Parameter.Key<Entity> ENTITY_DESTINATION = Parameter.key("destination", Entity.class);
    public static final Parameter.Key<GameMode> GAME_MODE = Parameter.key("game-mode", GameMode.class);
    public static final Parameter.Key<GameRule<?>> GAME_RULE = Parameter.key("game-rule", new TypeToken<GameRule<?>>() {});
    public static final Parameter.Key<Object> GAME_RULE_VALUE = Parameter.key("value", Object.class);
    public static final Parameter.Key<Long> INTERVAL = Parameter.key("interval", Long.class);
    public static final Parameter.Key<ItemType> ITEM_TYPE = Parameter.key("item-type", ItemType.class);
    public static final Parameter.Key<List<Component>> MANY_SCORE_HOLDER = Parameter.key("score-holders", new TypeToken<List<Component>>() {});
    public static final Parameter.Key<Integer> MAX = Parameter.key("maximum", Integer.class);
    public static final Parameter.Key<ServerPlayer> MESSAGE_RECEIVER = Parameter.key("message-receiver", ServerPlayer.class);
    public static final Parameter.Key<Integer> MIN = Parameter.key("minimum", Integer.class);
    public static final Parameter.Key<String> NAME = Parameter.key("name", String.class);
    public static final Parameter.Key<Objective> OBJECTIVE = Parameter.key("objective", Objective.class);
    public static final Parameter.Key<Operands> OPERAND = Parameter.key("operand", Operands.class);
    public static final Parameter.Key<ParticleType> PARTICLE_TYPE = Parameter.key("particle-type", ParticleType.class);
    public static final Parameter.Key<Listener> PER_WORLD_FEATURE = Parameter.key("feature", Listener.class);
    public static final Parameter.Key<Double> PITCH = Parameter.key("pitch", Double.class);
    public static final Parameter.Key<Vector3d> PITCH_YAW_ROLL = Parameter.key("pitch> <yaw> <roll", Vector3d.class);
    public static final Parameter.Key<CosmosPortal> PORTAL_COSMOS = Parameter.key("portal", CosmosPortal.class);
    public static final Parameter.Key<DelayFormat> PORTAL_DELAY_FORMAT = Parameter.key("delay-timer-format", DelayFormat.class);
    public static final Parameter.Key<CosmosFramePortal> PORTAL_FRAME_COSMOS = Parameter.key("portal-frame", CosmosFramePortal.class);
    public static final Parameter.Key<PortalType> PORTAL_TYPE = Parameter.key("portal-type", PortalType.class);
    public static final Parameter.Key<CosmosPortalType> PORTAL_TYPE_COSMOS = Parameter.key("portal-type", CosmosPortalType.class);
    public static final Parameter.Key<PotionEffectType> POTION_EFFECT_TYPE = Parameter.key("potion-effect-type", PotionEffectType.class);
    public static final Parameter.Key<Integer> SCORE = Parameter.key("score", Integer.class);
    public static final Parameter.Key<Long> SEED = Parameter.key("seed", Long.class);
    public static final Parameter.Key<SoundType> SOUND_TYPE = Parameter.key("sound-type", SoundType.class);
    public static final Parameter.Key<Double> START_DIAMETER = Parameter.key("start-diameter", Double.class);
    public static final Parameter.Key<Boolean> STATE = Parameter.key("state", Boolean.class);
    public static final Parameter.Key<String> TAG = Parameter.key("tag", String.class);
    public static final Parameter.Key<Team> TEAM = Parameter.key("team", Team.class);
    public static final Parameter.Key<Component> TEXT_AMPERSAND = Parameter.key("text-ampersand", Component.class);
    public static final Parameter.Key<Component> TEXT_JSON = Parameter.key("text-json", Component.class);
    public static final Parameter.Key<Long> TICKS = Parameter.key("ticks", Long.class);
    public static final Parameter.Key<ChronoUnit> TIME_UNIT = Parameter.key("unit", ChronoUnit.class);
    public static final Parameter.Key<Visibility> VISIBILITY = Parameter.key("visibility", Visibility.class);
    public static final Parameter.Key<Double> VOLUME = Parameter.key("volume", Double.class);
    public static final Parameter.Key<String> WILDCARD = Parameter.key("wildcard", String.class);
    public static final Parameter.Key<ResourceKey> WORLD = Parameter.key("world", ResourceKey.class);
    public static final Parameter.Key<ResourceKey> WORLD_DESTINATION = Parameter.key("world-destination", ResourceKey.class);
    public static final Parameter.Key<ResourceKey> WORLD_ORIGIN = Parameter.key("world-origin", ResourceKey.class);
    public static final Parameter.Key<Vector2d> X_Z = Parameter.key("x> <z", Vector2d.class);
    public static final Parameter.Key<Vector3d> X_Y_Z = Parameter.key("x> <y> <z", Vector3d.class);

    public static final class Flag {

        public static final String SAFE_ONLY = "safe-only";
        public static final String SAVE_CONFIG = "save-config";
        public static final String WITH_TARGET_BLOCK = "with-target-block";

    }

}
