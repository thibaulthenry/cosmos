package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.AbstractListener;
import cosmos.listeners.ListenerRegister;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.config.Config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;

import java.util.Optional;

public class PerWorld extends AbstractCommand {

    public PerWorld() {
        super(
                Arguments.perWorldChoices(ArgKeys.PER_WORLD_COMMAND),
                GenericArguments.optionalWeak(
                        GenericArguments.bool(ArgKeys.STATE.t)
                ),
                Arguments.baseFlag(ArgKeys.SAVE_CONFIG)
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        PerWorldCommands perWorldCommands = args.<PerWorldCommands>getOne(ArgKeys.PER_WORLD_COMMAND.t)
                .orElseThrow(Outputs.INVALID_PER_WORLD_COMMAND_CHOICE.asSupplier());

        Class<? extends AbstractListener> listenerClass = perWorldCommands.getListenerClass();
        String commandName = perWorldCommands.getName();

        Optional<Boolean> optionalState = args.getOne(ArgKeys.STATE.t);
        boolean currentState = ListenerRegister.isListenerRegistered(listenerClass);
        boolean newState = currentState;
        String mutableText = "currently";

        if (optionalState.isPresent()) {
            newState = optionalState.get();
            mutableText = "successfully";
            manageListener(listenerClass, commandName, newState, currentState);
        }

        src.sendMessage(Outputs.TOGGLE_PER_WORLD_LISTENER.asText(commandName, mutableText, newState ? "activated" : "disabled"));

        if (!args.hasAny(ArgKeys.SAVE_CONFIG.t)) {
            return;
        }

        if (!Config.saveListener(listenerClass, newState)) {
            src.sendMessage(Outputs.SAVING_CONFIG_NODE.asText(commandName));
            return;
        }

        src.sendMessage(Outputs.SAVE_CONFIG_NODE.asText(commandName));
    }

    private static void manageListener(Class<? extends AbstractListener> listenerClass, String commandName,
                                       boolean newState, boolean currentState) throws CommandException {
        if (newState && currentState) {
            throw Outputs.TOGGLING_PER_WORLD_LISTENER.asException(commandName, "activated");
        }

        if (!newState && !currentState) {
            throw Outputs.TOGGLING_PER_WORLD_LISTENER.asException(commandName, "disabled");
        }

        if (newState) {
            ListenerRegister.registerListener(listenerClass);
        } else {
            ListenerRegister.unregisterListener(listenerClass);
        }
    }
}
