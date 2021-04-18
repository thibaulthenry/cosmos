package cosmos.statics.serializers;

import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.advancement.Advancement;
import org.spongepowered.api.advancement.AdvancementProgress;
import org.spongepowered.api.advancement.Progressable;
import org.spongepowered.api.advancement.criteria.AdvancementCriterion;
import org.spongepowered.api.advancement.criteria.CriterionProgress;
import org.spongepowered.api.advancement.criteria.OperatorCriterion;
import org.spongepowered.api.advancement.criteria.ScoreCriterionProgress;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.gamerule.DefaultGameRules;

import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdvancementsSerializer {

    private static final String NAME = "Name";
    private static final String DATE = "Date";
    private static final String GOAL = "Goal";
    private static final String SCORE = "Score";

    public static void serialize(Path path, Player player) {
        DataContainer dataContainer = DataContainer.createNew(DataView.SafetyMode.ALL_DATA_CLONED);

        Sponge.getRegistry().getAllOf(Advancement.class)
                .forEach(advancement -> serializeAdvancement(dataContainer, player.getProgress(advancement)));

        if (dataContainer.isEmpty()) {
            return;
        }

        FinderFile.writeToFile(dataContainer, path);
    }

    public static void serializePlayerData(Path path, Path inputPath) {
        DataContainer dataContainer = DataContainer.createNew();

        DateTimeFormatter vanillaDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());

        FinderFile.readFromJsonFile(inputPath)
                .flatMap(playerDataContainer -> playerDataContainer.getMap(DataQuery.of()))
                .ifPresent(vanillaAdvancementMap -> vanillaAdvancementMap.forEach((key, value) -> {
                    if (!(key instanceof String && value instanceof Map)) {
                        return;
                    }

                    Map<String, Map<String, String>> criterionMap = new HashMap<>();
                    Object vanillaCriterionDataMap = ((Map<?, ?>) value).get("criteria");

                    if (!(vanillaCriterionDataMap instanceof Map)) {
                        return;
                    }

                    Map<String, String> criterionDataMap = new HashMap<>();

                    ((Map<?, ?>) vanillaCriterionDataMap).forEach((criterionDataKey, criterionDataValue) -> {
                        if (!(criterionDataKey instanceof String && criterionDataValue instanceof String)) {
                            return;
                        }

                        String criterionName = (String) criterionDataKey;

                        try {
                            String dateValue = dateTimeFormatter.format(vanillaDateTimeFormatter.parse((String) criterionDataValue));
                            criterionDataMap.put("Date", dateValue);
                            criterionDataMap.put("Name", criterionName);
                        } catch (Exception ignored) {
                        }

                        if (!criterionDataMap.isEmpty()) {
                            criterionMap.put(criterionName, criterionDataMap);
                        }
                    });

                    String advancementName = ((String) key).replaceAll("/", "_");

                    if (!criterionMap.isEmpty()) {
                        dataContainer.set(DataQuery.of(advancementName), criterionMap);
                    }
                }));

        if (!dataContainer.isEmpty()) {
            FinderFile.writeToFile(dataContainer, path);
        }
    }

    private static void serializeAdvancement(DataContainer dataContainer, AdvancementProgress advancementProgress) {
        Advancement advancement = advancementProgress.getAdvancement();
        AdvancementCriterion advancementCriterion = advancement.getCriterion();

        Map<String, Map<String, Object>> advancementData = getCriterionProgress(advancementProgress, advancementCriterion)
                .stream()
                .filter(AdvancementsSerializer::isCriterionProgressStarted)
                .map(AdvancementsSerializer::getCriterionData)
                .collect(Collectors.toMap(criterionData -> criterionData.name, CriterionData::serialize));

        if (!advancementData.isEmpty()) {
            dataContainer.set(DataQuery.of(advancement.getId()), advancementData);
        }
    }

    private static Collection<CriterionProgress> getCriterionProgress(AdvancementProgress advancementProgress, AdvancementCriterion rootCriterion) {
        if (!(rootCriterion instanceof OperatorCriterion)) {
            return advancementProgress.get(rootCriterion)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }

        return ((OperatorCriterion) rootCriterion).getCriteria()
                .stream()
                .map(advancementCriterion -> {
                    if (advancementCriterion instanceof OperatorCriterion) {
                        return getCriterionProgress(advancementProgress, advancementCriterion);
                    }

                    return advancementProgress.get(advancementCriterion)
                            .map(Collections::singletonList)
                            .orElse(Collections.emptyList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static CriterionData getCriterionData(CriterionProgress criterionProgress) {
        return criterionProgress instanceof ScoreCriterionProgress ?
                aggregateCriterionData((ScoreCriterionProgress) criterionProgress) :
                aggregateCriterionData(criterionProgress);
    }

    private static CriterionData aggregateCriterionData(CriterionProgress criterionProgress) {
        return new CriterionData()
                .name(criterionProgress.getCriterion().getName())
                .date(criterionProgress.get().orElse(Instant.now()));
    }

    private static ScoreCriterionData aggregateCriterionData(ScoreCriterionProgress scoreCriterionProgress) {
        return new ScoreCriterionData()
                .score(scoreCriterionProgress.getScore())
                .goal(scoreCriterionProgress.getGoal())
                .name(scoreCriterionProgress.getCriterion().getName())
                .date(scoreCriterionProgress.get().orElse(Instant.now()));
    }

    private static boolean isCriterionProgressStarted(CriterionProgress criterionProgress) {
        if (criterionProgress instanceof ScoreCriterionProgress) {
            return ((ScoreCriterionProgress) criterionProgress).getScore() > 0;
        }

        return criterionProgress.achieved();
    }

    public static void deserialize(Path path, Player player) {
        DataContainer dataContainer = FinderFile.readFromNbtFile(path).orElse(DataContainer.createNew());

        String announceAdvancements = player.getWorld()
                .getProperties()
                .getGameRule(DefaultGameRules.ANNOUNCE_ADVANCEMENTS)
                .orElse("true");

        player.getWorld().getProperties().setGameRule(DefaultGameRules.ANNOUNCE_ADVANCEMENTS, "false");

        Sponge.getRegistry().getAllOf(Advancement.class).forEach(advancement -> {
            AdvancementProgress advancementProgress = player.getProgress(advancement);

            Optional<DataView> optionalAdvancementView = dataContainer.getView(DataQuery.of(advancement.getId()));

            if (optionalAdvancementView.isPresent()) {
                reassignOperatorCriterion(optionalAdvancementView.get(), advancementProgress, advancement.getCriterion());
            } else {
                advancementProgress.revoke();
            }
        });

        player.getWorld().getProperties().setGameRule(DefaultGameRules.ANNOUNCE_ADVANCEMENTS, announceAdvancements);
    }

    private static void reassignOperatorCriterion(DataView advancementView, AdvancementProgress advancementProgress, AdvancementCriterion operatorCriterion) {
        if (!(operatorCriterion instanceof OperatorCriterion)) {
            reassignLeafCriterion(advancementView, advancementProgress, operatorCriterion);
            return;
        }

        ((OperatorCriterion) operatorCriterion).getCriteria().forEach(advancementCriterion -> {
            if (advancementCriterion instanceof OperatorCriterion) {
                reassignOperatorCriterion(advancementView, advancementProgress, advancementCriterion);
            } else {
                reassignLeafCriterion(advancementView, advancementProgress, advancementCriterion);
            }
        });
    }

    private static void reassignLeafCriterion(DataView advancementView, AdvancementProgress advancementProgress, AdvancementCriterion leafCriterion) {
        Optional<CriterionProgress> optionalCriterionProgress = advancementProgress.get(leafCriterion);

        if (!optionalCriterionProgress.isPresent()) {
            return;
        }

        CriterionProgress criterionProgress = optionalCriterionProgress.get();

        Optional<DataView> optionalCriterionView = advancementView.getView(DataQuery.of(leafCriterion.getName()));

        if (!optionalCriterionView.isPresent()) {
            criterionProgress.revoke();
            return;
        }

        DataView criterionView = optionalCriterionView.get();

        if (criterionProgress instanceof ScoreCriterionProgress) {
            criterionView.getInt(DataQuery.of(SCORE)).ifPresent(((ScoreCriterionProgress) criterionProgress)::set);
        } else {
            advancementProgress.get(leafCriterion).ifPresent(Progressable::grant);
        }
    }

    private static class CriterionData {
        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());

        String name;
        String date;

        Map<String, Object> serialize() {
            Map<String, Object> data = new HashMap<>();
            data.put(NAME, name);
            data.put(DATE, date);
            return data;
        }

        CriterionData name(String name) {
            this.name = name;
            return this;
        }

        CriterionData date(Instant instant) {
            date = DATE_TIME_FORMATTER.format(instant);
            return this;
        }
    }

    private static class ScoreCriterionData extends CriterionData {

        int goal;
        int score;

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> data = super.serialize();
            data.put(SCORE, score);
            data.put(GOAL, goal);
            return data;
        }

        @Override
        ScoreCriterionData name(String name) {
            super.name(name);
            return this;
        }

        @Override
        ScoreCriterionData date(Instant instant) {
            super.date(instant);
            return this;
        }

        ScoreCriterionData goal(int goal) {
            this.goal = goal;
            return this;
        }

        ScoreCriterionData score(int score) {
            this.score = score;
            return this;
        }
    }
}
