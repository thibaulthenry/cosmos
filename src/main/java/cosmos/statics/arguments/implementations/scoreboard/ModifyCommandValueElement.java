package cosmos.statics.arguments.implementations.scoreboard;

import cosmos.constants.ArgKeys;
import cosmos.constants.ModifyCommands;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.TextArguments;
import cosmos.statics.arguments.implementations.EnhancedCommandElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("NullableProblems")
public class ModifyCommandValueElement extends EnhancedCommandElement {

    private final Text modifyCommandKey;
    private final Map<ModifyCommands, EnhancedCommandElement> modifyCommandMap;
    private EnhancedCommandElement element;

    public ModifyCommandValueElement(@Nonnull ArgKeys key, @Nonnull ArgKeys modifyCommandKey) {
        super(key.t);
        this.modifyCommandKey = modifyCommandKey.t;

        modifyCommandMap = new EnumMap<>(ModifyCommands.class);
        modifyCommandMap.put(
                ModifyCommands.DISPLAY_NAME,
                TextArguments.allTexts(key)
        );
        modifyCommandMap.put(
                ModifyCommands.RENDER_TYPE,
                Arguments.enhancedCatalogedElement(key, ObjectiveDisplayMode.class)
        );

        element = modifyCommandMap.get(ModifyCommands.DISPLAY_NAME);
    }

    @Nullable
    public Text getKey() {
        return element.getKey();
    }

    @Nullable
    public String getUntranslatedKey() {
        return element.getUntranslatedKey();
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        context.<ModifyCommands>getOne(modifyCommandKey)
                .ifPresent(modifyCommand -> {
                    if (modifyCommandMap.containsKey(modifyCommand)) {
                        element = modifyCommandMap.get(modifyCommand);
                    }
                });

        element.parse(source, args, context);
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        return element.parseValue(source, args);
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return element.complete(src, args, context);
    }

    @Override
    public Text getUsage(CommandSource src) {
        return element.getUsage(src);
    }
}
