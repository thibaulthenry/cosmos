package cosmos.executors.commands.perworld.feature;

import cosmos.constants.ConfigurationNodes;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.registries.listener.impl.perworld.AbstractPerWorldListener;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.Flag;

import java.util.Optional;

class AbstractPerWorldFeatureCommand extends AbstractCommand {

    private final Class<? extends AbstractPerWorldListener> listenerClass;

    private String formattedName;

    AbstractPerWorldFeatureCommand(final Class<? extends AbstractPerWorldListener> listenerClass) {
        super(Parameter.bool().setKey(CosmosKeys.STATE).optional().build());
        this.listenerClass = listenerClass;
    }

    @Override
    protected Flag[] flags() {
        return new Flag[]{Flag.of(CosmosKeys.FLAG_SAVE_CONFIG)};
    }

    private String getFormattedName() {
        if (this.formattedName == null) {
            this.formattedName = super.serviceProvider.configuration().formatListener(this.listenerClass);
        }

        return this.formattedName;
    }

    private void handleInput(final Audience src, final boolean oldState, final boolean newState) throws CommandException {
        if (newState && oldState) {
            throw super.serviceProvider.message().getMessage(src, "error.per-world")
                    .replace("name", this.getFormattedName())
                    .condition("value", true)
                    .asError();
        }

        if (!newState && !oldState) {
            throw super.serviceProvider.message().getMessage(src, "error.per-world")
                    .replace("name", this.getFormattedName())
                    .condition("value", false)
                    .asError();
        }

        if (newState) {
            super.serviceProvider.listener().register(this.listenerClass);
        } else {
            super.serviceProvider.listener().unregister(this.listenerClass);
        }
    }

    @Override
    protected final void run(final Audience src, final CommandContext context) throws CommandException {
        final Optional<Boolean> optionalInput = context.getOne(CosmosKeys.STATE);
        boolean value = super.serviceProvider.listener().isRegistered(this.listenerClass);

        if (optionalInput.isPresent()) {
            this.handleInput(src, value, optionalInput.get());
            value = optionalInput.get();
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.per-world.set" : "success.per-world.get")
                .replace("name", this.getFormattedName())
                .condition("value", value)
                .green()
                .sendTo(src);

        if (!context.hasFlag(CosmosKeys.FLAG_SAVE_CONFIG)) {
            return;
        }

        if (!super.serviceProvider.configuration().saveValue(value, ConfigurationNodes.PER_WORLD, ConfigurationNodes.PER_WORLD, this.getFormattedName())) {
            throw super.serviceProvider.message().getError(src, "error.config.save", "node", this.getFormattedName());
        }

        super.serviceProvider.message()
                .getMessage(src, "success.config.save")
                .replace("node", this.getFormattedName())
                .gray()
                .sendTo(src);
    }

}
