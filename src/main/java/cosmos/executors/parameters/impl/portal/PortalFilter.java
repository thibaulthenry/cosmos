package cosmos.executors.parameters.impl.portal;

import cosmos.registries.message.Message;
import cosmos.registries.portal.CosmosPortal;
import cosmos.services.ServiceProvider;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.clientcompletion.ClientCompletionType;
import org.spongepowered.api.command.parameter.managed.clientcompletion.ClientCompletionTypes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class PortalFilter implements ValueParameter<CosmosPortal> {

    private final Function<Audience, Message> errorMessage;
    private final Predicate<CosmosPortal> filter;
    private final ServiceProvider serviceProvider;

    PortalFilter(final Function<Audience, Message> errorMessage, final Predicate<CosmosPortal> filter, final ServiceProvider serviceProvider) {
        this.errorMessage = errorMessage;
        this.filter = filter;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return this.serviceProvider.registry().portal()
                .stream()
                .filter(this.filter)
                .map(CosmosPortal::getKey)
                .map(ResourceKey::getFormatted)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientCompletionType> getClientCompletionType() {
        return Collections.singletonList(ClientCompletionTypes.RESOURCE_KEY.get());
    }

    @Override
    public Optional<? extends CosmosPortal> getValue(final Parameter.Key<? super CosmosPortal> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final ResourceKey portalKey;

        try {
            portalKey = reader.parseResourceKey();
        } catch (final Exception ignored) {
            throw reader.createException(this.errorMessage.apply(context.getCause().getAudience()).red().asText());
        }

        final Optional<CosmosPortal> optionalValue = Optional.ofNullable(portalKey)
                .flatMap(this.serviceProvider.registry().portal()::find)
                .filter(this.filter);

        if (optionalValue.isPresent()) {
            return optionalValue;
        }

        throw reader.createException(this.errorMessage.apply(context.getCause().getAudience()).red().asText());
    }

}
