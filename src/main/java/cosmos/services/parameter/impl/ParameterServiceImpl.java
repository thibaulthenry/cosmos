package cosmos.services.parameter.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.services.message.MessageService;
import cosmos.services.parameter.ParameterService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Optional;

@Singleton
public class ParameterServiceImpl implements ParameterService {

    private final MessageService messageService;

    @Inject
    public ParameterServiceImpl(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
    }

    @Override
    public int extremum(final CommandContext context, final Parameter.Key<Integer> integerKey, final boolean negativeBound) throws CommandException {
        final Audience src = context.cause().audience();

        return this.findExtremum(context, integerKey, negativeBound)
                .orElseThrow(this.messageService.supplyError(src, "error.invalid.value", "param", integerKey));
    }

    @Override
    public Optional<Integer> findExtremum(final CommandContext context, final Parameter.Key<Integer> integerKey, final boolean negativeBound) {
        return context.one(integerKey)
                .map(Optional::of)
                .orElse(context.one(CosmosKeys.WILDCARD).map(value -> negativeBound ? Integer.MIN_VALUE : Integer.MAX_VALUE));
    }

    @Override
    public Optional<Component> findComponent(final CommandContext context) {
        return context.one(CosmosKeys.TEXT_JSON)
                .map(Optional::of)
                .orElse(context.one(CosmosKeys.TEXT_AMPERSAND));
    }

}
