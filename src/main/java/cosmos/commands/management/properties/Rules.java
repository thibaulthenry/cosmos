package cosmos.commands.management.properties;

import cosmos.commands.AbstractCommand;
import cosmos.utils.Finder;
import cosmos.utils.Permission;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class Rules extends AbstractCommand {

    public Rules() {
        super(Permission.COMMAND_MANAGEMENT_PROPERTIES_RULES);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getAvailableWorldNames(true)).keySet(),
                                key -> Finder.toMap(Finder.getAvailableWorldNames(true)).get(key)
                        ),
                        GenericArguments.flags()
                                .buildWith(GenericArguments.none())
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
