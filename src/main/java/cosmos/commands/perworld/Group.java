package cosmos.commands.perworld;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.ListenerRegister;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.config.Config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.util.Tuple;

import java.util.HashSet;
import java.util.Set;

public class Group extends AbstractCommand {

    public Group() {
        super(
                GenericArguments.allOf(
                        WorldNameArguments.distinctChoices(ArgKeys.WORLD)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        PerWorldCommands perWorldCommands = args.<PerWorldCommands>getOne(ArgKeys.PER_WORLD_COMMAND.t)
                .orElseThrow(Outputs.INVALID_PER_WORLD_COMMAND_CHOICE.asSupplier());

        if (!ListenerRegister.isListenerRegistered(perWorldCommands.getListenerClass())) {
            throw Outputs.PER_WORLD_DISABLED.asException(perWorldCommands.getName());
        }

        Set<String> group = new HashSet<>(args.getAll(ArgKeys.WORLD.t));

        if (group.isEmpty()) {
            throw Outputs.PER_WORLD_GROUP_EMPTY.asException();
        }

        ListenerRegister.applyScheduledSave();

        if (group.size() == 1) {
            GroupRegister.unregister(Tuple.of(perWorldCommands, group.iterator().next()));
        } else {
            group.forEach(worldName -> GroupRegister.register(Tuple.of(perWorldCommands, worldName), group));
        }

        ListenerRegister.triggerToggleListener(perWorldCommands.getListenerClass(), true);

        String groupName = perWorldCommands.getName() + "-group";

        if (!Config.saveGroup(perWorldCommands.getListenerClass(), GroupRegister.collectGroups(perWorldCommands))) {
            src.sendMessage(Outputs.SAVING_CONFIG_NODE.asText(groupName));
            return;
        }

        src.sendMessage(Outputs.SAVE_CONFIG_NODE.asText(groupName));
    }

}
