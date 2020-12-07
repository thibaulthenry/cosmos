package cosmos.executors.modules;

import cosmos.executors.AbstractExecutor;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AbstractModule extends AbstractExecutor {

    private final Map<List<String>, AbstractExecutor> childExecutorMap;
    private final String moduleName;
    private Parameter prefixedParameter;
    private Command.Parameterized parameterized;

    protected AbstractModule(@Nullable final Parameter prefixedParameter, final AbstractExecutor... childExecutors) {
        this(childExecutors);
        this.prefixedParameter = prefixedParameter;
    }

    protected AbstractModule(final AbstractExecutor... childExecutors) {
        this.moduleName = this.getClass().getSimpleName().toLowerCase();
        this.childExecutorMap = Arrays.stream(childExecutors)
                .collect(Collectors.toMap(executor -> Arrays.asList(executor.getClass().getSimpleName().toLowerCase()), Function.identity()));
    }

    @Override
    protected CommandResult execute(final Audience audience, final CommandContext context) {
        final TextComponent hoverHelpText = this.serviceProvider.message().getMessage(audience, "modules.help.hover").asText();

        final TextComponent helpText = this.serviceProvider.message().getMessage(audience, "modules.help")
                .replace("module", this.moduleName)
                .hoverEvent("help", HoverEvent.showText(hoverHelpText))
                .clickEvent("help", ClickEvent.suggestCommand("/cm help"))
                .successColor()
                .asText();

        final TextComponent allowedSubCommands = Component.join(
                Component.text(" | ", NamedTextColor.GRAY),
                this.getParametrized().subcommands()
                        .stream()
                        .filter(command -> command.getCommand().canExecute(context.getCause()))
                        .flatMap(command -> command.getAliases().stream())
                        .map(alias -> Component.text(alias, NamedTextColor.WHITE))
                        .collect(Collectors.toSet())
        );

        final TextComponent helpCommandsText = this.serviceProvider.message().getMessage(audience, "modules.help.commands")
                .replace("commands", allowedSubCommands)
                .defaultColor(NamedTextColor.GRAY)
                .asText();

        audience.sendMessage(Component.text().append(helpText).append(Component.newline()).append(helpCommandsText).build());

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

            commandBuilder.parameter(prefixedParameter).parameter(Parameter.firstOf(childSubcommandSet));
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

                    if (executor instanceof AbstractCommand) {
                        return commandCause.hasPermission(((AbstractCommand) executor).getPermission());
                    }

                    return false;
                });
    }
}
