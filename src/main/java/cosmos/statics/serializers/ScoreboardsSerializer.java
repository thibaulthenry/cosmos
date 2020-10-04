package cosmos.statics.serializers;

import cosmos.constants.Units;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.finders.FinderRegistry;
import cosmos.statics.finders.FinderScoreboard;
import cosmos.statics.handlers.Validator;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.scoreboard.CollisionRule;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScoreboardsSerializer {

    private static final String OBJECTIVES = "Objectives";
    private static final String OBJECTIVE_CRITERIA_NAME = "CriteriaName";
    private static final String OBJECTIVE_DISPLAY_NAME = "DisplayName";
    private static final String OBJECTIVE_NAME = "Name";
    private static final String OBJECTIVE_RENDER_TYPE = "RenderType";

    private static final String SCORES = "PlayerScores";
    private static final String SCORE_LOCKED = "Locked";
    private static final String SCORE_AMOUNT = "Score";
    private static final String SCORE_TARGET_NAME = "Name";
    private static final String SCORE_OBJECTIVE = "Objective";

    private static final String TEAMS = "Teams";
    private static final String TEAM_ALLOW_FRIENDLY_FIRE = "AllowFriendlyFire";
    private static final String TEAM_SEE_FRIENDLY_INVISIBLES = "SeeFriendlyInvisibles";
    private static final String TEAM_COLLISION_RULE = "CollisionRule";
    private static final String TEAM_DEATH_MESSAGE_VISIBILITY = "DeathMessageVisibility";
    private static final String TEAM_DISPLAY_NAME = "DisplayName";
    private static final String TEAM_NAME = "Name";
    private static final String TEAM_NAME_TAG_VISIBILITY = "NameTagVisibility";
    private static final String TEAM_PREFIX = "Prefix";
    private static final String TEAM_SUFFIX = "Suffix";
    private static final String TEAM_COLOR = "TeamColor";
    private static final String TEAM_PLAYERS = "Players";

    private static final String DISPLAY_SLOTS = "DisplaySlots";

    public static void serialize(Path path, Scoreboard scoreboard) {
        DataContainer dataContainer = DataContainer.createNew(DataView.SafetyMode.ALL_DATA_CLONED);

        serializeObjectives(dataContainer, scoreboard);
        serializeScores(dataContainer, scoreboard);
        serializeTeams(dataContainer, scoreboard);
        serializeDisplaySlots(dataContainer, scoreboard);

        if (dataContainer.isEmpty()) {
            return;
        }

        FinderFile.writeToFile(dataContainer, path);
    }

    private static void serializeObjectives(DataContainer dataContainer, Scoreboard scoreboard) {
        List<Map<String, Object>> objectivesData = scoreboard.getObjectives()
                .stream()
                .map(objective -> {
                    Map<String, Object> objectiveData = new HashMap<>();

                    objectiveData.put(OBJECTIVE_CRITERIA_NAME, objective.getCriterion().getName());
                    objectiveData.put(OBJECTIVE_DISPLAY_NAME, getTextJSON(objective.getDisplayName()));
                    objectiveData.put(OBJECTIVE_NAME, objective.getName());
                    objectiveData.put(OBJECTIVE_RENDER_TYPE, objective.getDisplayMode().getName());

                    return objectiveData;
                })
                .collect(Collectors.toList());

        dataContainer.set(DataQuery.of(OBJECTIVES), objectivesData);
    }

    private static void serializeScores(DataContainer dataContainer, Scoreboard scoreboard) {
        List<Map<String, Object>> playersData = scoreboard.getScores()
                .stream()
                .sorted(Comparator.comparing(score -> score.getName().toPlain()))
                .map(score -> score.getObjectives().stream().map(objective -> {
                    Map<String, Object> scoreData = new HashMap<>();

                    scoreData.put(SCORE_LOCKED, false); // TODO SpongePowered/SpongeAPI Issue #2230
                    scoreData.put(SCORE_AMOUNT, score.getScore());
                    scoreData.put(SCORE_TARGET_NAME, getTextJSON(score.getName()));
                    scoreData.put(SCORE_OBJECTIVE, objective.getName());

                    return scoreData;
                }).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        dataContainer.set(DataQuery.of(SCORES), playersData);
    }

    private static void serializeTeams(DataContainer dataContainer, Scoreboard scoreboard) {
        List<Map<String, Object>> teamsData = scoreboard.getTeams()
                .stream()
                .map(team -> {
                    Map<String, Object> teamData = new HashMap<>();

                    teamData.put(TEAM_ALLOW_FRIENDLY_FIRE, team.allowFriendlyFire());
                    teamData.put(TEAM_SEE_FRIENDLY_INVISIBLES, team.canSeeFriendlyInvisibles());
                    teamData.put(TEAM_COLLISION_RULE, team.getCollisionRule().getId());
                    teamData.put(TEAM_DEATH_MESSAGE_VISIBILITY, team.getDeathMessageVisibility().getId());
                    teamData.put(TEAM_DISPLAY_NAME, getTextJSON(team.getDisplayName()));
                    teamData.put(TEAM_NAME, team.getName());
                    teamData.put(TEAM_NAME_TAG_VISIBILITY, team.getNameTagVisibility().getId());
                    teamData.put(TEAM_PREFIX, getTextJSON(team.getPrefix()));
                    teamData.put(TEAM_SUFFIX, getTextJSON(team.getSuffix()));
                    teamData.put(TEAM_COLOR, team.getColor().getName());
                    teamData.put(TEAM_PLAYERS, team.getMembers().stream().map(Text::toPlain).collect(Collectors.toList()));

                    return teamData;
                })
                .collect(Collectors.toList());

        dataContainer.set(DataQuery.of(TEAMS), teamsData);
    }

    private static void serializeDisplaySlots(DataContainer dataContainer, Scoreboard scoreboard) {
        Map<String, String> displaySlotsData = new HashMap<>();

        Sponge.getRegistry().getAllOf(DisplaySlot.class).forEach(displaySlot ->
                scoreboard.getObjective(displaySlot)
                        .map(Objective::getName)
                        .ifPresent(objectiveName -> displaySlotsData.putIfAbsent(displaySlot.getId(), objectiveName))
        );

        dataContainer.set(DataQuery.of(DISPLAY_SLOTS), displaySlotsData);
    }

    public static Optional<Scoreboard> deserialize(Path path) {
        DataContainer dataContainer = FinderFile.readFromFile(path).orElse(DataContainer.createNew());

        if (dataContainer.isEmpty()) {
            return Optional.empty();
        }

        Scoreboard scoreboard = Scoreboard.builder().build();

        deserializeObjectives(dataContainer, scoreboard);
        deserializeScores(dataContainer, scoreboard);
        deserializeTeams(dataContainer, scoreboard);
        deserializeDisplaySlots(dataContainer, scoreboard);

        return Optional.of(scoreboard);
    }

    private static void deserializeObjectives(DataContainer dataContainer, Scoreboard scoreboard) {
        Optional<List<DataView>> optionalObjectivesView = dataContainer.getViewList(DataQuery.of(OBJECTIVES));

        if (!optionalObjectivesView.isPresent()) {
            return;
        }

        optionalObjectivesView.get().forEach(objectiveView -> {
            Optional<Criterion> optionalCriterion = objectiveView.getString(DataQuery.of(OBJECTIVE_CRITERIA_NAME))
                    .flatMap(criteriaName -> FinderRegistry.findByIdOrShortIdOrName(criteriaName, FinderScoreboard.getAllCriteria()));

            Optional<String> optionalName = objectiveView.getString(DataQuery.of(OBJECTIVE_NAME))
                    .filter(name -> !Validator.doesOverflowMaxLength(name, Units.NAME_MAX_LENGTH));

            Optional<ObjectiveDisplayMode> optionalDisplayMode = objectiveView.getString(DataQuery.of(OBJECTIVE_RENDER_TYPE))
                    .flatMap(displayModeName -> FinderRegistry.findByIdOrShortIdOrName(displayModeName, ObjectiveDisplayMode.class));

            Optional<Text> optionalDisplayName = objectiveView.getString(DataQuery.of(OBJECTIVE_DISPLAY_NAME))
                    .map(TextSerializers.JSON::deserialize)
                    .filter(displayName -> !Validator.doesOverflowMaxLength(displayName, Units.DISPLAY_NAME_MAX_LENGTH));

            if (!optionalCriterion.isPresent() || !optionalDisplayName.isPresent()
                    || !optionalName.isPresent() || !optionalDisplayMode.isPresent()) {
                return;
            }

            scoreboard.addObjective(Objective.builder()
                    .criterion(optionalCriterion.get())
                    .displayName(optionalDisplayName.get())
                    .name(optionalName.get())
                    .objectiveDisplayMode(optionalDisplayMode.get())
                    .build()
            );
        });
    }

    private static void deserializeScores(DataContainer dataContainer, Scoreboard scoreboard) {
        Optional<List<DataView>> optionalScoresView = dataContainer.getViewList(DataQuery.of(SCORES));

        if (!optionalScoresView.isPresent()) {
            return;
        }

        optionalScoresView.get().forEach(scoreView -> {
            Optional<Objective> optionalObjective = scoreView.getString(DataQuery.of(SCORE_OBJECTIVE))
                    .flatMap(scoreboard::getObjective);
            Optional<Text> optionalTargetName = scoreView.getString(DataQuery.of(SCORE_TARGET_NAME))
                    .map(TextSerializers.JSON::deserialize)
                    .filter(name -> !Validator.doesOverflowMaxLength(name, Units.PLAYER_NAME_MAX_LENGTH));
            Optional<Integer> optionalScoreAmount = scoreView.getInt(DataQuery.of(SCORE_AMOUNT));

            if (!optionalObjective.isPresent() || !optionalTargetName.isPresent() || !optionalScoreAmount.isPresent()) {
                return;
            }

            Text targetName = optionalTargetName.get();

            if (targetName.toPlain().length() > 40) {
                return;
            }

            optionalObjective.get().getOrCreateScore(targetName).setScore(optionalScoreAmount.get());
        });
    }

    private static void deserializeTeams(DataContainer dataContainer, Scoreboard scoreboard) {
        Optional<List<DataView>> optionalTeamsView = dataContainer.getViewList(DataQuery.of(TEAMS));

        if (!optionalTeamsView.isPresent()) {
            return;
        }

        optionalTeamsView.get().forEach(teamView -> {
            Team.Builder teamBuilder = Team.builder();

            Optional<Text> optionalDisplayName = teamView.getString(DataQuery.of(TEAM_DISPLAY_NAME))
                    .map(TextSerializers.JSON::deserialize)
                    .filter(displayName -> !Validator.doesOverflowMaxLength(displayName, Units.DISPLAY_NAME_MAX_LENGTH));
            Optional<String> optionalName = teamView.getString(DataQuery.of(TEAM_NAME))
                    .filter(name -> !Validator.doesOverflowMaxLength(name, Units.NAME_MAX_LENGTH));

            if (!optionalName.isPresent() || !optionalDisplayName.isPresent()) {
                return;
            }

            teamView.getBoolean(DataQuery.of(TEAM_ALLOW_FRIENDLY_FIRE))
                    .ifPresent(teamBuilder::allowFriendlyFire);

            teamView.getBoolean(DataQuery.of(TEAM_SEE_FRIENDLY_INVISIBLES))
                    .ifPresent(teamBuilder::canSeeFriendlyInvisibles);

            teamView.getString(DataQuery.of(TEAM_COLLISION_RULE))
                    .flatMap(collisionRuleId -> FinderRegistry.findByIdOrShortIdOrName(collisionRuleId, CollisionRule.class))
                    .ifPresent(teamBuilder::collisionRule);

            teamView.getString(DataQuery.of(TEAM_DEATH_MESSAGE_VISIBILITY))
                    .flatMap(visibilityId -> FinderRegistry.findByIdOrShortIdOrName(visibilityId, Visibility.class))
                    .ifPresent(teamBuilder::deathTextVisibility);

            teamBuilder.displayName(optionalDisplayName.get());

            teamBuilder.name(optionalName.get());

            teamView.getString(DataQuery.of(TEAM_NAME_TAG_VISIBILITY))
                    .flatMap(visibilityId -> FinderRegistry.findByIdOrShortIdOrName(visibilityId, Visibility.class))
                    .ifPresent(teamBuilder::nameTagVisibility);

            teamView.getString(DataQuery.of(TEAM_PREFIX))
                    .map(TextSerializers.JSON::deserialize)
                    .ifPresent(teamBuilder::prefix);

            teamView.getString(DataQuery.of(TEAM_SUFFIX))
                    .map(TextSerializers.JSON::deserialize)
                    .ifPresent(teamBuilder::suffix);

            teamView.getString(DataQuery.of(TEAM_COLOR))
                    .flatMap(textColorName -> FinderRegistry.findByIdOrShortIdOrName(textColorName, TextColor.class))
                    .ifPresent(teamBuilder::color);

            teamView.getList(DataQuery.of(TEAM_PLAYERS))
                    .map(playerNames -> playerNames.stream().map(Text::of).collect(Collectors.toSet()))
                    .ifPresent(teamBuilder::members);

            scoreboard.registerTeam(teamBuilder.build());
        });
    }

    private static void deserializeDisplaySlots(DataContainer dataContainer, Scoreboard scoreboard) {
        Optional<? extends Map<?, ?>> optionalDisplaySlotsData = dataContainer.getMap(DataQuery.of(DISPLAY_SLOTS));

        if (!optionalDisplaySlotsData.isPresent()) {
            return;
        }

        optionalDisplaySlotsData.get().forEach((key, value) -> {
            if (!(key instanceof String) || !(value instanceof String)) {
                return;
            }

            Optional<Objective> optionalObjective = scoreboard.getObjective((String) value);
            Optional<DisplaySlot> optionalDisplaySlot = FinderRegistry.findByIdOrShortIdOrName((String) key, DisplaySlot.class);

            if (!optionalObjective.isPresent() || !optionalDisplaySlot.isPresent()) {
                return;
            }

            scoreboard.updateDisplaySlot(optionalObjective.get(), optionalDisplaySlot.get());
        });
    }

    private static Object getTextJSON(Text text) {
        return text.toContainer().get(DataQuery.of("JSON")).orElse(text.toPlain());
    }

}
