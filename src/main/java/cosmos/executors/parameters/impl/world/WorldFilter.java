package cosmos.executors.parameters.impl.world;

import cosmos.services.ServiceProvider;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class WorldFilter implements ValueParameter<ResourceKey> {

    private final String errorKey;
    private final Predicate<ResourceKey> filter;
    private final ServiceProvider serviceProvider;

    WorldFilter(final String errorKey, final Predicate<ResourceKey> filter, final ServiceProvider serviceProvider) {
        this.errorKey = errorKey;
        this.filter = filter;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return Sponge.getServer().getWorldManager().worldKeys()
                .stream()
                .filter(key -> key.getFormatted().startsWith(currentInput))
                .filter(this.filter)
                .map(ResourceKey::getFormatted)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends ResourceKey> getValue(final Parameter.Key<? super ResourceKey> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        return Optional.ofNullable(reader.parseResourceKey());
//        final ResourceKey worldKey; todo
//
//        final TextComponent errorText = this.serviceProvider.message()
//                .getMessage(context.getCause().getAudience(), this.errorKey)
//                .red()
//                .asText();
//
//        try {
//            worldKey = reader.parseResourceKey();
//        } catch (final Exception ignored) {
//            throw reader.createException(errorText);
//        }
//
//        final Optional<ResourceKey> optionalValue = Optional.ofNullable(worldKey)
//                .filter(this.filter);
//
//        if (optionalValue.isPresent()) {
//            return optionalValue;
//        }
//
//        throw reader.createException(errorText);
    }
}
