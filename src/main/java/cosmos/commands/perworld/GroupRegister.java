package cosmos.commands.perworld;

import cosmos.Cosmos;
import cosmos.constants.PerWorldCommands;
import cosmos.statics.config.Config;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupRegister {

    private static final Map<Tuple<PerWorldCommands, String>, Set<String>> groupMap = new HashMap<>();

    public static Collection<Set<String>> collectGroups(PerWorldCommands feature) {
        final Set<String> collectedWorlds = new HashSet<>();

        return streamEntries()
                .filter(entry -> entry.getKey().getFirst().equals(feature))
                .filter(entry -> !collectedWorlds.contains(entry.getKey().getSecond()))
                .map(entry -> {
                    collectedWorlds.add(entry.getKey().getSecond());
                    return entry.getValue();
                })
                .collect(Collectors.toSet());
    }

    public static Optional<Set<String>> find(Tuple<PerWorldCommands, String> key) {
        return Optional.ofNullable(value(key));
    }

    public static Optional<Set<String>> register(Tuple<PerWorldCommands, String> key, Set<String> value) {
        return Optional.ofNullable(groupMap.computeIfAbsent(key, k -> value))
                .map(v -> {
                    v.forEach(k -> {
                        Tuple<PerWorldCommands, String> tupleK = Tuple.of(key.getFirst(), k);

                        if (!value.contains(k)) {
                            groupMap.remove(tupleK);
                        }
                    });

                    return groupMap.put(key, value);
                });
    }

    public static void registerAll() {
        if (!Config.isLoaded()) {
            Cosmos.sendConsole(TextColors.RED, "Configuration file not loaded. No per world groups loaded !");
            return;
        }

        for (final PerWorldCommands feature : PerWorldCommands.values()) {
            Config.getGroupNode(feature.getListenerClass())
                    .map(ConfigurationNode::getChildrenList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(ConfigurationNode::getChildrenList)
                    .map(nodes -> nodes.stream()
                            .map(ConfigurationNode::getString)
                            .map(worldName -> Optional.ofNullable(worldName).flatMap(Sponge.getServer()::getWorld).map(World::getName))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet()))
                    .forEach(group -> group.forEach(key -> register(Tuple.of(feature, key), group)));

            Config.saveGroup(feature.getListenerClass(), collectGroups(feature));
        }
    }

    public static Stream<Map.Entry<Tuple<PerWorldCommands, String>, Set<String>>> streamEntries() {
        return groupMap.entrySet().stream();
    }

    public static Optional<Set<String>> unregister(Tuple<PerWorldCommands, String> key) {
        return Optional.ofNullable(groupMap.remove(key))
                .map(v -> {
                    Set<String> value = new HashSet<>(v);
                    value.remove(key.getSecond());

                    v.forEach(k -> {
                        Tuple<PerWorldCommands, String> tupleK = Tuple.of(key.getFirst(), k);

                        if (!tupleK.equals(key) && groupMap.containsKey(tupleK)) {
                            if (value.size() > 1) {
                                groupMap.put(tupleK, value);
                            } else {
                                groupMap.remove(tupleK);
                            }
                        }
                    });

                    return v;
                });
    }

    public static Set<String> value(Tuple<PerWorldCommands, String> key) {
        return groupMap.get(key);
    }

}
