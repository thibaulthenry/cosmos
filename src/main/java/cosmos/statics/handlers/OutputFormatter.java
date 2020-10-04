package cosmos.statics.handlers;

import com.flowpowered.math.GenericMath;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector3i;
import cosmos.constants.ArgKeys;
import cosmos.constants.ModifyCommands;
import cosmos.constants.Operands;
import cosmos.constants.PerWorldCommands;
import cosmos.constants.TeamOptions;
import cosmos.statics.arguments.implementations.scoreboard.DisplaySlotChoiceElement;
import cosmos.statics.finders.FinderRegistry;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Comparator;
import java.util.Optional;

public class OutputFormatter {

    public static final String noFormatCode = "@&=-nf";

    private static final int MAX_TEXT_LENGTH = 18;

    public static Object formatOutput(Object arg) {
        if (arg instanceof String) {
            String stringArg = ((String) arg);
            if (stringArg.startsWith(noFormatCode)) {
                return stringArg.replaceAll(noFormatCode, "");
            }

            return toShortenedString((String) arg, MAX_TEXT_LENGTH);
        } else if (arg instanceof Text) {
            return toShortenedText((Text) arg);
        } else if (arg instanceof Text.Builder) {
            return ((Text.Builder) arg).build();
        } else if (arg instanceof ArgKeys) {
            return ((ArgKeys) arg).t;
        } else if (arg instanceof WorldProperties) {
            return ((WorldProperties) arg).getWorldName();
        } else if (arg instanceof Operands) {
            return ((Operands) arg).getOperand();
        } else if (arg instanceof ModifyCommands) {
            return ((ModifyCommands) arg).getText();
        } else if (arg instanceof TeamOptions) {
            return ((TeamOptions) arg).getText();
        } else if (arg instanceof PerWorldCommands) {
            return ((PerWorldCommands) arg).getName();
        } else if (arg instanceof DisplaySlot) {
            return DisplaySlotChoiceElement.toVanilla(FinderRegistry.shortenId(((DisplaySlot) arg).getName()));
        } else if (arg instanceof CatalogType) {
            return FinderRegistry.shortenId(((CatalogType) arg).getName());
        } else if (arg instanceof Objective) {
            return toHoverObjective((Objective) arg);
        } else if (arg instanceof Team) {
            return toHoverTeam((Team) arg);
        } else if (arg instanceof Vector2i) {
            return toCoordinates2i((Vector2i) arg);
        } else if (arg instanceof Vector2d) {
            return toCoordinates2d((Vector2d) arg);
        } else if (arg instanceof Vector2f) {
            return toCoordinates2f((Vector2f) arg);
        } else if (arg instanceof Vector3i) {
            return toCoordinates3i((Vector3i) arg);
        } else if (arg instanceof Vector3d) {
            return toCoordinates3d((Vector3d) arg);
        } else if (arg instanceof Vector3f) {
            return toCoordinates3f((Vector3f) arg);
        }

        return arg;
    }

    private static String toShortenedString(String arg, int remainingSpace) {
        return (arg.length() > remainingSpace) ? arg.substring(0, remainingSpace) + ".." : arg;
    }

    private static Text toShortenedText(Text text) {
        if (text.toPlain().length() <= MAX_TEXT_LENGTH) {
            return text;
        }

        if (text.getChildren().isEmpty()) {
            return toShortenedTextWithoutChildren(text, MAX_TEXT_LENGTH);
        }

        return toShortenedTextWithChildren(text, MAX_TEXT_LENGTH);
    }

    private static Text.Builder extractBuilderFromText(Text textToExtractFormat, String content) {
        Text.Builder builder = Text.builder(content)
                .style(textToExtractFormat.getStyle())
                .color(textToExtractFormat.getColor())
                .format(textToExtractFormat.getFormat());

        textToExtractFormat.getHoverAction().ifPresent(builder::onHover);
        textToExtractFormat.getClickAction().ifPresent(builder::onClick);
        textToExtractFormat.getShiftClickAction().ifPresent(builder::onShiftClick);

        return builder;
    }

    private static Text toShortenedTextWithoutChildren(Text text, int remainingSpace) {
        return extractBuilderFromText(text, toShortenedString(text.toPlainSingle(), remainingSpace)).build();
    }

    private static Text toShortenedTextWithChildren(Text text, int remainingSpace) {
        if (text.toPlainSingle().length() > remainingSpace) {
            return toShortenedTextWithoutChildren(text, remainingSpace);
        }

        Text.Builder builder = extractBuilderFromText(text, "");

        for (Text childText : text.getChildren()) {
            if (remainingSpace == 0) {
                break;
            }

            if (childText.getChildren().isEmpty()) {
                Text shortenedText = toShortenedTextWithoutChildren(childText, remainingSpace);
                remainingSpace = Math.max(0, remainingSpace - shortenedText.toPlainSingle().length());
                builder.append(shortenedText);
            } else {
                builder.append(toShortenedTextWithChildren(childText, remainingSpace));
            }
        }

        return builder.build();
    }

    private static Text toHoverObjective(Objective objective) {
        Optional<Score> optionalBestScore = objective.getScores()
                .values()
                .stream()
                .max(Comparator.comparingInt(Score::getScore));

        Text displayedAsText = Text.of(
                TextColors.GRAY, "Display as: ",
                TextColors.RESET, objective.getDisplayName()
        );

        Text displayModeText = Text.of(
                TextColors.GRAY, "Display mode: ",
                TextColors.GOLD, objective.getDisplayMode().getName()
        );

        Text criterionText = Text.of(
                TextColors.GRAY, "Criterion: ",
                TextColors.GOLD, objective.getCriterion().getName()
        );

        Text bestScoreText = optionalBestScore.map(bestScore -> Text.of(
                Text.NEW_LINE,
                Text.of(
                        TextColors.GRAY, "Best score: ",
                        TextColors.GOLD, bestScore.getScore(),
                        TextColors.GRAY, " from ",
                        TextColors.RESET, toShortenedText(bestScore.getName())
                )
        )).orElse(Text.EMPTY);

        Text registeredScoresText = Text.of(
                TextColors.GRAY, "Registered scores: ",
                TextColors.GOLD, objective.getScores().size()
        );

        Text hoverText = Text.of(
                displayedAsText,
                Text.NEW_LINE,
                displayModeText,
                Text.NEW_LINE,
                criterionText,
                Text.NEW_LINE,
                registeredScoresText,
                bestScoreText
        );

        return Text.builder()
                .append(Text.of(TextStyles.UNDERLINE, toShortenedString(objective.getName(), MAX_TEXT_LENGTH)))
                .onHover(TextActions.showText(hoverText))
                .build();
    }

    private static Text toHoverTeam(Team team) {
        Text displayedAsText = Text.of(
                TextColors.GRAY, "Display as: ",
                TextColors.RESET, team.getDisplayName()
        );

        Text registeredMembersText = Text.of(
                TextColors.GRAY, "Registered members: ",
                TextColors.GOLD, team.getMembers().size()
        );

        Text optionsText = Text.of(
                TextColors.GRAY, "Options: ",
                TextColors.GRAY, Text.NEW_LINE, "   • Collision rule - ",
                TextColors.GOLD, team.getCollisionRule().getName(),
                TextColors.GRAY, Text.NEW_LINE, "   • Color - ",
                team.getColor(), team.getColor().getName(),
                TextColors.GRAY, Text.NEW_LINE, "   • Death message visibility - ",
                TextColors.GOLD, team.getDeathMessageVisibility().getName(),
                TextColors.GRAY, Text.NEW_LINE, "   • Allow friendly fire - ",
                TextColors.GOLD, team.allowFriendlyFire(),
                TextColors.GRAY, Text.NEW_LINE, "   • Nametag visibility - ",
                TextColors.GOLD, team.getNameTagVisibility().getName(),
                TextColors.GRAY, Text.NEW_LINE, "   • Can see friendly invisibles - ",
                TextColors.GOLD, team.canSeeFriendlyInvisibles()
        );

        Text hoverText = Text.of(
                displayedAsText,
                Text.NEW_LINE,
                registeredMembersText,
                Text.NEW_LINE,
                optionsText
        );

        return Text.builder()
                .append(Text.of(TextStyles.UNDERLINE, toShortenedString(team.getName(), MAX_TEXT_LENGTH)))
                .onHover(TextActions.showText(hoverText))
                .build();
    }

    private static Text toVector2(Object x, Object z) {
        return Text.of(
                TextColors.WHITE, "<x: ", TextColors.GOLD, x,
                TextColors.WHITE, ", z: ", TextColors.GOLD, z,
                TextColors.WHITE, ">"
        );
    }

    private static Text toCoordinates2i(Vector2i vector2i) {
        return toVector2(vector2i.getX(), vector2i.getY());

    }

    private static Text toCoordinates2d(Vector2d vector2d) {
        return toVector2(
                GenericMath.round(vector2d.getX(), 2),
                GenericMath.round(vector2d.getY(), 2)
        );
    }

    private static Text toCoordinates2f(Vector2f vector2f) {
        return toVector2(
                GenericMath.round(vector2f.getX(), 2),
                GenericMath.round(vector2f.getY(), 2)
        );
    }

    private static Text toVector3(Object x, Object y, Object z) {
        return Text.of(
                TextColors.WHITE, "<x: ", TextColors.GOLD, x,
                TextColors.WHITE, ", y: ", TextColors.GOLD, y,
                TextColors.WHITE, ", z: ", TextColors.GOLD, z,
                TextColors.WHITE, ">"
        );
    }

    private static Text toCoordinates3i(Vector3i vector3i) {
        return toVector3(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    private static Text toCoordinates3d(Vector3d vector3d) {
        return toVector3(
                GenericMath.round(vector3d.getX(), 2),
                GenericMath.round(vector3d.getY(), 2),
                GenericMath.round(vector3d.getZ(), 2)
        );
    }

    private static Text toCoordinates3f(Vector3f vector3f) {
        return toVector3(
                GenericMath.round(vector3f.getX(), 2),
                GenericMath.round(vector3f.getY(), 2),
                GenericMath.round(vector3f.getZ(), 2)
        );
    }
}
