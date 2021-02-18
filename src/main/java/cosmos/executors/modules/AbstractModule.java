package cosmos.executors.modules;

import cosmos.executors.AbstractExecutor;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractModule extends AbstractExecutor {

    private final Map<List<String>, AbstractExecutor> childExecutorMap;
    private final String moduleName;
    private final Parameter prefixedParameter;
    private final boolean terminal;

    private Command.Parameterized parameterized;

    protected AbstractModule(@Nullable final Parameter prefixedParameter, boolean terminal, final AbstractExecutor... childExecutors) {
        this.childExecutorMap = Arrays.stream(childExecutors).collect(Collectors.toMap(AbstractExecutor::aliases, Function.identity()));
        this.moduleName = this.getClass().getName()
                .replace(AbstractModule.class.getPackage().getName() + ".", "")
                .replaceAll("\\.", "-")
                .toLowerCase(Locale.ROOT);
        this.prefixedParameter = prefixedParameter;
        this.terminal = terminal;
    }

    protected AbstractModule(@Nullable final Parameter prefixedParameter, final AbstractExecutor... childExecutors) {
        this(prefixedParameter, true, childExecutors);
    }

    protected AbstractModule(final boolean terminal, final AbstractExecutor... childExecutors) {
        this(null, terminal, childExecutors);
    }

    protected AbstractModule(final AbstractExecutor... childExecutors) {
        this(null, true, childExecutors);
    }

    @Override
    protected final CommandResult execute(final Audience src, final CommandContext context) {
        final Comparator<TextComponent> comparator = (t1, t2) -> {
            final String s1 = PlainComponentSerializer.plain().serialize(t1);
            final String s2 = PlainComponentSerializer.plain().serialize(t2);

            return s1.compareToIgnoreCase(s2);
        };

        final TextComponent allowedSubCommandsText = Component.join(
                Component.text(" | ", NamedTextColor.GRAY),
                this.childExecutorMap.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().parametrized().canExecute(context.cause()))
                        .map(entry -> entry.getKey().stream().map(Component::text).sorted(comparator).collect(Collectors.toList()))
                        .map(aliases -> Component.join(Component.text("-", NamedTextColor.DARK_GRAY), aliases))
                        .sorted(comparator)
                        .collect(Collectors.toList())
        );

        final TextComponent hoverHelpText = super.serviceProvider.message().getText(src, "modules.help.hover");

        final TextComponent helpText = super.serviceProvider.message()
                .getMessage(src, "modules.help")
                .replace("commands", allowedSubCommandsText)
                .replace("module", this.moduleName)
                .clickEvent("help", ClickEvent.suggestCommand("/cm help"))
                .hoverEvent("help", HoverEvent.showText(hoverHelpText))
                .green()
                .asText();

        src.sendMessage(helpText);

        return CommandResult.success();
    }

    public boolean hasPrefixedParameter() {
        return this.prefixedParameter != null;
    }

    private boolean hasSubPermission(final CommandCause commandCause) {
        return this.childExecutorMap.values()
                .stream()
                .anyMatch(executor -> {
                    if (executor instanceof AbstractModule) {
                        return ((AbstractModule) executor).hasSubPermission(commandCause);
                    }

                    return executor instanceof AbstractCommand && commandCause.hasPermission(((AbstractCommand) executor).permission());
                });
    }

    @Override
    public Command.Parameterized parametrized() {
        if (this.parameterized != null) {
            return this.parameterized;
        }

        final Command.Builder commandBuilder = Command.builder();

        if (this.hasPrefixedParameter()) {
            final Set<Parameter> childSubcommandSet = this.childExecutorMap
                    .entrySet()
                    .stream()
                    .filter(entry -> !entry.getKey().isEmpty())
                    .map(entry -> {
                        final String alias = entry.getKey().get(0);
                        final String[] aliases = entry.getKey().stream().skip(1).toArray(String[]::new);

                        return Parameter.subcommand(entry.getValue().parametrized(), alias, aliases);
                    })
                    .collect(Collectors.toSet());

            commandBuilder.addParameter(this.prefixedParameter).addParameter(Parameter.firstOf(childSubcommandSet));
        } else {
            final Map<List<String>, Command.Parameterized> childCommandMap = this.childExecutorMap
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().parametrized()));

            commandBuilder.addChildren(childCommandMap);
        }

        this.parameterized = commandBuilder
                .executionRequirements(this::hasSubPermission)
                .executor(this)
                .terminal(this.terminal)
                .build();

        return this.parameterized;
    }

}
