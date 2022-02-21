package cosmos.executors.commands.perworld;

import com.google.inject.Singleton;
import cosmos.constants.ConfigurationNodes;
import cosmos.constants.CosmosKeys;
import cosmos.constants.PerWorldFeatures;
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
    protected void run(final Audience src, final CommandContext context, final PerWorldFeatures feature) throws CommandException {
        final Optional<Boolean> optionalInput = context.one(CosmosKeys.STATE);
        boolean value = super.serviceProvider.listener().isRegisteredToSponge(feature.listenerClass());

        if (optionalInput.isPresent()) {
            boolean inputValue = optionalInput.get();

            if (inputValue && value) {
                throw super.serviceProvider.message()
                        .getMessage(src, "error.per-world.toggle")
                        .replace("name", feature.formatted())
                        .condition("value", true)
                        .asError();
            }

            if (!inputValue && !value) {
                throw super.serviceProvider.message()
                        .getMessage(src, "error.per-world.toggle")
                        .replace("name", feature.formatted())
                        .condition("value", false)
                        .asError();
            }

            if (inputValue) {
                super.serviceProvider.registry().listener().register(feature.listenerClass());
            } else {
                super.serviceProvider.registry().listener().unregister(feature.listenerClass());
            }

            value = inputValue;
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.per-world.toggle.set" : "success.per-world.toggle.get")
                .replace("name", feature.formatted())
                .condition("value", value)
                .green()
                .sendTo(src);

        if (!context.hasFlag(CosmosKeys.Flag.SAVE_CONFIG)) {
            return;
        }

        if (!super.serviceProvider.configuration().saveValue(value, ConfigurationNodes.PER_WORLD, feature.formatted(), ConfigurationNodes.PER_WORLD_STATE)) {
            throw super.serviceProvider.message().getError(src, "error.config.save", "node", feature.formatted());
        }

        super.serviceProvider.message()
                .getMessage(src, "success.config.save")
                .replace("node", feature.formatted())
                .gray()
                .sendTo(src);
    }

}
