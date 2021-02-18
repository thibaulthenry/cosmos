package cosmos.executors.parameters.world;

import cosmos.registries.message.Message;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
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
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class WorldFilter implements ValueParameter<ResourceKey> {

    private final Function<Audience, Message> errorMessage;
    private final BiPredicate<ResourceKey, CommandContext> filter;

    WorldFilter(final Function<Audience, Message> errorMessage, final Predicate<ResourceKey> filter) {
        this.errorMessage = errorMessage;
        this.filter = (key, context) -> filter.test(key);
    }

    WorldFilter(final Function<Audience, Message> errorMessage, final BiPredicate<ResourceKey, CommandContext> filter) {
        this.errorMessage = errorMessage;
        this.filter = filter;
    }

    @Override
    public List<ClientCompletionType> clientCompletionType() {
        return Collections.singletonList(ClientCompletionTypes.RESOURCE_KEY.get());
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return Sponge.server().worldManager().worldKeys()
                .stream()
                .filter(key -> key.formatted().startsWith(currentInput))
                .filter(key -> this.filter.test(key, context))
                .map(ResourceKey::formatted)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends ResourceKey> parseValue(final Parameter.Key<? super ResourceKey> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final ResourceKey worldKey;

        try {
            worldKey = reader.parseResourceKey();
        } catch (final Exception ignored) {
            throw reader.createException(this.errorMessage.apply(context.cause().audience()).red().asText());
        }

        final Optional<ResourceKey> optionalValue = Optional.ofNullable(worldKey).filter(key -> this.filter.test(key, context));

        if (optionalValue.isPresent()) {
            return optionalValue;
        }

        throw reader.createException(this.errorMessage.apply(context.cause().audience()).red().asText());
    }

}
