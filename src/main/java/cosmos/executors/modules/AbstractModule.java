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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AbstractModule extends AbstractExecutor {

    private final Map<List<String>, AbstractExecutor> childExecutorMap;
    private final String moduleName;
    private final Parameter prefixedParameter;
    private Command.Parameterized parameterized;

    protected AbstractModule(@Nullable final Parameter prefixedParameter, final AbstractExecutor... childExecutors) {
        this.moduleName = this.getClass().getSimpleName().toLowerCase();
        this.childExecutorMap = Arrays.stream(childExecutors).collect(Collectors.toMap(AbstractExecutor::getAliases, Function.identity()));
        this.prefixedParameter = prefixedParameter;
    }

    protected AbstractModule(final AbstractExecutor... childExecutors) {
        this(null, childExecutors);
    }

    @Override
    protected CommandResult execute(final Audience src, final CommandContext context) {
        final TextComponent hoverHelpText = this.serviceProvider.message().getMessage(src, "modules.help.hover").asText();

        final TextComponent helpText = this.serviceProvider.message().getMessage(src, "modules.help")
                .replace("module", this.moduleName)
                .hoverEvent("help", HoverEvent.showText(hoverHelpText))
                .clickEvent("help", ClickEvent.suggestCommand("/cm help"))
                .successColor()
                .asText();

        final Comparator<TextComponent> comparator = (t1, t2) -> {
            final String s1 = PlainComponentSerializer.plain().serialize(t1);
            final String s2 = PlainComponentSerializer.plain().serialize(t2);
            return s1.compareToIgnoreCase(s2);
        };

        final TextComponent allowedSubCommandsText = Component.join(
                Component.text(" | ", NamedTextColor.GRAY),
                this.childExecutorMap.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().getParametrized().canExecute(context.getCause()))
                        .map(entry -> entry.getKey().stream().map(Component::text).sorted(comparator).collect(Collectors.toList()))
                        .map(aliases -> Component.join(Component.text("-", NamedTextColor.DARK_GRAY), aliases))
                        .sorted(comparator)
                        .collect(Collectors.toList())
        );

        final TextComponent helpCommandsText = this.serviceProvider.message().getMessage(src, "modules.help.commands")
                .replace("commands", allowedSubCommandsText)
                .defaultColor(NamedTextColor.GRAY)
                .asText();

        src.sendMessage(Component.text().append(helpText).append(Component.newline()).append(helpCommandsText).build());

        return CommandResult.success();
    }

    @Override
    public Command.Parameterized getParametrized() {
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
                        return Parameter.subcommand(entry.getValue().getParametrized(), alias, aliases);
                    })
                    .collect(Collectors.toSet());

            commandBuilder.parameter(this.prefixedParameter).parameter(Parameter.firstOf(childSubcommandSet));
        } else {
            final Map<List<String>, Command.Parameterized> childCommandMap = this.childExecutorMap
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getParametrized()));

            commandBuilder.children(childCommandMap);
        }

        this.parameterized = commandBuilder
                .setExecutor(this)
                .setExecutionRequirements(this::hasSubPermission)
                .setTerminal(true)
                .build();

        return this.parameterized;
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

                    return executor instanceof AbstractCommand
                            && commandCause.hasPermission(((AbstractCommand) executor).getPermission());
                });
    }
}
