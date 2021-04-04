package cosmos.modules;

import cosmos.Cosmos;
import cosmos.commands.AbstractCommand;
import cosmos.constants.Aliases;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class AbstractModule extends AbstractCommand {

    private final String permission;
    private final String description;
    private final String moduleName;

    private final ModuleSpec.Builder moduleSpecBuilder = ModuleSpec.builder();
    private CommandSpec commandSpec;

    protected AbstractModule(String description) {
        this(description, false);
    }

    protected AbstractModule(String description, boolean root) {
        String packageName = getClass().getPackage().getName();

        this.description = description;
        moduleName = getClass().getSimpleName().toLowerCase();
        permission = packageName + "." + moduleName + (root && !"root".equals(moduleName) ? ".root" : "");

        Sponge.getServiceManager().provide(PermissionService.class).ifPresent(permissionService ->
                permissionService.newDescriptionBuilder(Cosmos.instance)
                        .id(permission)
                        .description(Text.of("Allows the user to access to the ", moduleName, " module."))
                        .register()
        );
    }

    public CommandSpec getCommandSpec() {
        if (commandSpec == null) {
            ModuleSpec moduleSpec = moduleSpecBuilder
                    .fallbackExecutor(this)
                    .build();

            commandSpec = CommandSpec
                    .builder()
                    .arguments(moduleSpec.getArgs())
                    .executor(moduleSpec.getExecutor())
                    .description(Text.of(description))
                    .permission(permission)
                    .build();
        }

        return commandSpec;
    }

    @Override
    @SuppressWarnings("HardcodedFileSeparator")
    protected void run(CommandSource src, CommandContext args) {
        Text help = Text.builder("help")
                .style(TextStyles.UNDERLINE)
                .color(TextColors.DARK_GREEN)
                .onHover(TextActions.showText(Text.of("Click to explore Cosmos help")))
                .onClick(TextActions.suggestCommand("/cm help"))
                .build();

        Text title = Outputs.ADVISE_HELP.asText(moduleName, help);

        Text usage = commandSpec.getUsage(src);
        String plainUsage = usage.toPlain();
        String[] usageElements = usage.toPlain().split(" ");

        if (plainUsage.isEmpty() || usageElements.length == 0 || usageElements[usageElements.length-1].contains("<")) {
            src.sendMessage(Text.of(title, Text.NEW_LINE, TextColors.RED, "You do not have permission for any command in this module"));
            return;
        }

        usage = usage.replace("|", Text.of(TextColors.GRAY, "|"))
                .replace("<", Text.of(TextColors.GRAY, "<"))
                .replace(">", Text.of(TextColors.GRAY, ">"))
                .replace("[", Text.of(TextColors.GRAY, "["))
                .replace("]", Text.of(TextColors.GRAY, "]"));

        usage = Text.of(TextColors.GRAY, "Available module commands: ", TextColors.RESET, usage);

        src.sendMessage(Text.of(title, Text.NEW_LINE, usage));
    }

    protected final void addChild(AbstractCommand child) {
        moduleSpecBuilder.child(child.getCommandSpec(), Aliases.fromClass(child.getClass()));
    }

    protected final void addArgument(CommandElement argument) {
        moduleSpecBuilder.arguments(
                GenericArguments.optionalWeak(
                        Arguments.limitCompleteElement(argument)
                )
        );
    }

    protected final void addRequiredArgument(CommandElement argument) {
        moduleSpecBuilder.arguments(Arguments.limitCompleteElement(argument));
    }

}
