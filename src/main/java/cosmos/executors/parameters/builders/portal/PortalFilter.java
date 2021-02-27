package cosmos.executors.parameters.builders.portal;

import cosmos.Cosmos;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.text.Component;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

class PortalFilter<T extends CosmosPortal> implements ValueParameter<T> {

    private final Class<T> clazz;
    private final Predicate<T> filter;

    PortalFilter(final Class<T> clazz, final Predicate<T> filter) {
        this.clazz = clazz;
        this.filter = filter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> complete(final CommandContext context, final String currentInput) {
        return Cosmos.getServices().registry().portal()
                .stream()
                .filter(portal -> this.clazz.isAssignableFrom(portal.getClass()) && this.filter.test((T) portal))
                .map(CosmosPortal::key)
                .map(ResourceKey::getFormatted)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientCompletionType> getClientCompletionType() {
        return Collections.singletonList(ClientCompletionTypes.RESOURCE_KEY.get());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<? extends T> getValue(Parameter.Key<? super T> parameterKey, ArgumentReader.Mutable reader, CommandContext.Builder context) throws ArgumentParseException {
        final ResourceKey portalKey;

        try {
            portalKey = reader.parseResourceKey();
        } catch (final Exception ignored) {
            throw reader.createException(Component.empty()); // todo
        }

        final Optional<T> optionalValue = Optional.ofNullable(portalKey)
                .flatMap(Cosmos.getServices().registry().portal()::find)
                .filter(portalType -> this.clazz.isAssignableFrom(portalType.getClass()))
                .map(portalType -> (T) portalType)
                .filter(this.filter);

        if (optionalValue.isPresent()) {
            return optionalValue;
        }

        throw reader.createException(Component.empty()); // todo
    }

}
