package cosmos.statics.arguments.implementations.selector;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.source.ProxySource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.SpongeApiTranslationHelper;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.EntityUniverse;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class EntityCommandElement extends ExactSelectorCommandElement {

    private final boolean returnTarget;
    private final boolean returnSource;
    @Nullable
    private final Class<? extends Entity> clazz;
    @Nullable
    private final EntityType type;

    public EntityCommandElement(Text key, boolean returnSource, boolean returnTarget, @Nullable EntityType type) {
        super(key);
        this.returnSource = returnSource;
        this.returnTarget = returnTarget;
        clazz = null;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if (!args.hasNext()) {
            if (returnSource) {
                return tryReturnSource(source, args, true);
            }
            if (returnTarget) {
                return tryReturnTarget(source, args);
            }
        }

        CommandArgs.Snapshot state = args.getSnapshot();
        try {
            Iterable<Entity> entities = (Iterable<Entity>) Objects.requireNonNull(super.parseValue(source, args));
            for (Entity entity : entities) {
                if (!checkEntity(entity)) {
                    Text name = Text.of("Unknown type");

                    if (type != null) {
                        name = Text.of(type);
                    } else if (clazz != null) {
                        name = Sponge.getRegistry().getAllOf(EntityType.class).stream()
                                .filter(t -> t.getEntityClass().equals(clazz)).findFirst()
                                .map(EntityType::getTranslation).<Text>map(Text::of)
                                .orElse(Text.of(clazz.getSimpleName()));
                    }

                    throw args.createError(Text.of("The entity is not of the required type! (", name, ")"));
                }
            }
            return entities;
        } catch (ArgumentParseException ex) {
            if (returnSource) {
                args.applySnapshot(state);
                return tryReturnSource(source, args, true);
            }
            throw ex;
        }
    }

    @Override
    protected Iterable<String> getChoices() {
        Set<String> worldEntities = Sponge.getServer().getWorlds().stream().flatMap(x -> x.getEntities().stream())
                .filter(this::checkEntity)
                .map(x -> x.getUniqueId().toString())
                .collect(Collectors.toSet());

        Collection<Player> players = Sponge.getServer().getOnlinePlayers();

        if (!players.isEmpty() && checkEntity(players.iterator().next())) {
            Set<String> setToReturn = new HashSet<>(worldEntities); // to ensure mutability
            players.forEach(x -> setToReturn.add(x.getName()));
            return setToReturn;
        }

        return worldEntities;
    }

    @Override
    protected Iterable<String> getOnCompleteChoices() {
        return Sponge.getServer().getOnlinePlayers()
                .stream()
                .filter(this::checkEntity)
                .map(User::getName)
                .collect(Collectors.toSet());
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        UUID uuid;
        try {
            uuid = UUID.fromString(choice);
        } catch (IllegalArgumentException ignored) {
            // Player could be a name
            return Sponge.getServer().getPlayer(choice)
                    .orElseThrow(() -> new IllegalArgumentException("Input value " + choice + " does not represent a valid entity"));
        }
        boolean found = false;
        for (World world : Sponge.getServer().getWorlds()) {
            Optional<Entity> ret = world.getEntity(uuid);
            if (ret.isPresent()) {
                Entity entity = ret.get();
                if (checkEntity(entity)) {
                    return entity;
                }
                found = true;
            }
        }
        if (found) {
            throw new IllegalArgumentException("Input value " + choice + " was not an entity of the required type!");
        }
        throw new IllegalArgumentException("Input value " + choice + " was not an entity");
    }

    private Entity tryReturnSource(CommandSource source, CommandArgs args, boolean check) throws ArgumentParseException {
        if (source instanceof Entity && (!check || checkEntity((Entity) source))) {
            return (Entity) source;
        }
        if (source instanceof ProxySource) {
            CommandSource proxy = ((ProxySource) source).getOriginalSource();
            if (proxy instanceof Entity && (!check || checkEntity((Entity) proxy))) {
                return (Entity) proxy;
            }
        }
        throw args.createError(SpongeApiTranslationHelper.t("No entities matched and source was not an entity!"));
    }

    private Entity tryReturnTarget(CommandSource source, CommandArgs args) throws ArgumentParseException {
        Entity entity = tryReturnSource(source, args, false);
        return entity.getWorld().getIntersectingEntities(entity, 10).stream()
                .filter(e -> !e.getEntity().equals(entity)).map(EntityUniverse.EntityHit::getEntity)
                .filter(this::checkEntity).findFirst()
                .orElseThrow(() -> args.createError(SpongeApiTranslationHelper.t("No entities matched and source was not looking at a valid entity!")));
    }

    private boolean checkEntity(Entity entity) {
        if (clazz == null && type == null) {
            return true;
        } else if (clazz == null) {
            return entity.getType().equals(type);
        } else {
            return clazz.isAssignableFrom(entity.getClass());
        }
    }

    @Override
    public Text getUsage(CommandSource src) {
        return src instanceof Entity && (returnSource || returnTarget) ? Text.of("[", getKey(), "]") : super.getUsage(src);
    }
}
