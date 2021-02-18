package cosmos.executors.commands.perworld;

import com.google.inject.Singleton;
import cosmos.constants.ConfigurationNodes;
import cosmos.constants.CosmosKeys;
import cosmos.registries.listener.impl.perworld.AbstractPerWorldListener;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.Flag;

import java.util.Optional;

@Singleton
public class Toggle extends AbstractPerWorldCommand {

    public Toggle() {
        super(Parameter.bool().key(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected Flag[] flags() {
        return new Flag[]{Flag.of(CosmosKeys.Flag.SAVE_CONFIG)};
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final AbstractPerWorldListener listener) throws CommandException {
        final Class<? extends AbstractPerWorldListener> listenerClass = listener.getClass();
        final String formattedName = super.serviceProvider.listener().format(listenerClass);
        final Optional<Boolean> optionalInput = context.one(CosmosKeys.STATE);
        boolean currentValue = super.serviceProvider.registry().listener().isRegisteredToSponge(listenerClass);

        if (optionalInput.isPresent()) {
            boolean inputValue = optionalInput.get();

            if (inputValue && currentValue) {
                throw super.serviceProvider.message()
                        .getMessage(src, "error.per-world.toggle")
                        .replace("name", formattedName)
                        .condition("value", true)
                        .asError();
            }

            if (!inputValue && !currentValue) {
                throw super.serviceProvider.message()
                        .getMessage(src, "error.per-world.toggle")
                        .replace("name", formattedName)
                        .condition("value", false)
                        .asError();
            }

            if (inputValue) {
                super.serviceProvider.registry().listener().register(listenerClass);
            } else {
                super.serviceProvider.registry().listener().unregister(listenerClass);
            }

            currentValue = inputValue;
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.per-world.toggle.set" : "success.per-world.toggle.get")
                .replace("name", formattedName)
                .condition("value", currentValue)
                .green()
                .sendTo(src);

        if (!context.hasFlag(CosmosKeys.Flag.SAVE_CONFIG)) {
            return;
        }

        final String rootNode = ConfigurationNodes.PER_WORLD;

        if (!super.serviceProvider.configuration().saveValue(currentValue, rootNode, rootNode, formattedName)) {
            throw super.serviceProvider.message().getError(src, "error.config.save", "node", formattedName);
        }

        super.serviceProvider.message()
                .getMessage(src, "success.config.save")
                .replace("node", formattedName)
                .gray()
                .sendTo(src);
    }

}
