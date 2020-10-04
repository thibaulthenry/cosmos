package cosmos.statics.handlers;

import org.spongepowered.api.text.Text;

import java.util.Collection;

public class Validator {

    public static boolean hasAsterisk(Collection<Text> targets) {
        return targets.stream().anyMatch(target -> target.toPlain().contains("*"));
    }

    public static boolean doesOverflowMaxLength(CharSequence sequence, int maxLength) {
        return sequence.length() > maxLength;
    }

    public static boolean doesOverflowMaxLength(Text sequence, int maxLength) {
        return doesOverflowMaxLength(sequence.toPlain(), maxLength);
    }
}
