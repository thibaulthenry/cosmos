package cosmos.constants;

import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum FormattingCodes {
    BLACK(TextColors.BLACK, "0"),
    DARK_BLUE(TextColors.DARK_BLUE, "1"),
    DARK_GREEN(TextColors.DARK_GREEN, "2"),
    DARK_AQUA(TextColors.DARK_AQUA, "3"),
    DARK_RED(TextColors.DARK_RED, "4"),
    DARK_PURPLE(TextColors.DARK_PURPLE, "5"),
    GOLD(TextColors.GOLD, "6"),
    GRAY(TextColors.GRAY, "7"),
    DARK_GRAY(TextColors.DARK_GRAY, "8"),
    BLUE(TextColors.BLUE, "9"),
    GREEN(TextColors.GREEN, "a"),
    AQUA(TextColors.AQUA, "b"),
    RED(TextColors.RED, "c"),
    LIGHT_PURPLE(TextColors.LIGHT_PURPLE, "d"),
    YELLOW(TextColors.YELLOW, "e"),
    WHITE(TextColors.WHITE, "f"),
    OBFUSCATED(TextStyles.OBFUSCATED, "k"),
    BOLD(TextStyles.BOLD, "l"),
    STRIKETHROUGH(TextStyles.STRIKETHROUGH, "m"),
    UNDERLINE(TextStyles.UNDERLINE, "n"),
    ITALIC(TextStyles.ITALIC, "o"),
    RESET(TextColors.RESET, "r");

    private static final Map<TextElement, FormattingCodes> FROM_ELEMENT = new HashMap<>();
    private static final Map<String, FormattingCodes> FROM_CODE = new HashMap<>();

    static {
        Arrays.stream(FormattingCodes.values())
                .forEach(formattingCode -> {
                    FROM_ELEMENT.putIfAbsent(formattingCode.textElement, formattingCode);
                    FROM_CODE.putIfAbsent(formattingCode.code, formattingCode);
                });
    }

    private final TextElement textElement;
    private final String code;

    FormattingCodes(TextElement textElement, String code) {
        this.textElement = textElement;
        this.code = code;
    }

    public static Optional<FormattingCodes> getFromElement(TextElement element) {
        return Optional.ofNullable(FROM_ELEMENT.get(element));
    }

    public static Optional<FormattingCodes> getFromCode(String code) {
        return Optional.ofNullable(FROM_CODE.get(code));
    }

    public TextElement getTextElement() {
        return textElement;
    }

    public String getCode() {
        return code;
    }

}
