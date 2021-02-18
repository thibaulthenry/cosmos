package cosmos.executors.parameters.portal;

import cosmos.Cosmos;
import cosmos.registries.message.Message;
import cosmos.registries.portal.CosmosPortal;
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

class PortalFilter<T extends CosmosPortal> implements ValueParameter<T> {

    private final Class<T> clazz;
    private final Function<Audience, Message> errorMessage;
    private final Predicate<T> filter;

    PortalFilter(final Class<T> clazz, final Function<Audience, Message> errorMessage, final Predicate<T> filter) {
        this.clazz = clazz;
        this.errorMessage = errorMessage;
        this.filter = filter;
    }

    @Override
    public List<ClientCompletionType> clientCompletionType() {
        return Collections.singletonList(ClientCompletionTypes.RESOURCE_KEY.get());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> complete(final CommandContext context, final String currentInput) {
        return Cosmos.services().registry().portal()
                .stream()
                .filter(portal -> this.clazz.isAssignableFrom(portal.getClass()) && this.filter.test((T) portal))
                .map(CosmosPortal::key)
                .map(ResourceKey::formatted)
                .filter(formatted -> formatted.contains(currentInput))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<? extends T> parseValue(final Parameter.Key<? super T> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final ResourceKey portalKey;

        try {
            portalKey = reader.parseResourceKey();
        } catch (final Exception ignored) {
            throw reader.createException(this.errorMessage.apply(context.cause().audience()).red().asText());
        }

        final Optional<T> optionalValue = Optional.ofNullable(portalKey)
                .flatMap(Cosmos.services().registry().portal()::find)
                .filter(portalType -> this.clazz.isAssignableFrom(portalType.getClass()))
                .map(portalType -> (T) portalType)
                .filter(this.filter);

        if (optionalValue.isPresent()) {
            return optionalValue;
        }

        throw reader.createException(this.errorMessage.apply(context.cause().audience()).red().asText());
    }

}
