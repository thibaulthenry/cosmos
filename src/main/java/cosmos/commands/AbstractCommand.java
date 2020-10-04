package cosmos.commands;

import com.google.common.collect.Iterables;
import cosmos.Cosmos;
import cosmos.constants.Outputs;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;

public abstract class AbstractCommand implements CommandExecutor {

    private final String permission;
    private final CommandElement[] arguments;
    private int successCount;
    private boolean enableSuccessCount;

    protected AbstractCommand(CommandElement... commandElements) {
        String commandName = getClass().getSimpleName().toLowerCase();

        arguments = commandElements.clone();
        permission = getClass().getPackage().getName() + "." + commandName;

        Sponge.getServiceManager().getRegistration(PermissionService.class)
                .ifPresent(permissionService ->
                        permissionService.getProvider().newDescriptionBuilder(Cosmos.instance)
                                .id(permission)
                                .description(Text.of("Allows the user to execute the ", commandName, " command."))
                                .register()
                );
    }

    protected static PaginationList generatePaginationOutput(Text title, Iterable<Text> contents) {
        return PaginationList.builder().title(title).contents(contents).linesPerPage(10).build();
    }

    protected static void sendPaginatedOutput(CommandSource src, Text title, Iterable<Text> contents, boolean flattenSingleOutput) throws CommandException {
        if (Iterables.isEmpty(contents)) {
            throw Outputs.EMPTY_COMMAND_OUTPUT.asException();
        }

        if (flattenSingleOutput && Iterables.size(contents) == 1) {
            contents.forEach(src::sendMessage);
        } else {
            generatePaginationOutput(title, contents).sendTo(src);
        }
    }

    protected static void sendPaginatedOutput(CommandSource src, PaginationList output, boolean flattenSingleOutput) throws CommandException {
        sendPaginatedOutput(src, output.getTitle().orElse(Text.EMPTY), output.getContents(), flattenSingleOutput);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (enableSuccessCount) {
            successCount = 0;
        }

        run(src, args);

        return enableSuccessCount ? CommandResult.builder().successCount(successCount).build() : CommandResult.success();
    }

    protected abstract void run(CommandSource src, CommandContext args) throws CommandException;

    public CommandSpec getCommandSpec() {
        return CommandSpec.builder().arguments(arguments).permission(permission).executor(this).build();
    }

    protected final void enableSuccessCount() {
        enableSuccessCount = true;
    }

    protected void addSuccess() {
        addSuccess(1);
    }

    protected void addSuccess(int successAmount) {
        successCount += successAmount;
    }

    protected String getPermission() {
        return permission;
    }
}
