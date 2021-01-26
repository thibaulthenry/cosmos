package cosmos.executors.parameters.impl.world;

import cosmos.registries.message.Message;
import cosmos.services.ServiceProvider;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class WorldFilter implements ValueParameter<ResourceKey> {

    private final Function<Audience, Message> errorMessage;
    private final Predicate<ResourceKey> filter;

    WorldFilter(final Function<Audience, Message> errorMessage, final Predicate<ResourceKey> filter) {
        this.errorMessage = errorMessage;
        this.filter = filter;
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
    public List<ClientCompletionType> getClientCompletionType() {
        return Collections.singletonList(ClientCompletionTypes.RESOURCE_KEY.get());
    }

    @Override
    public Optional<? extends ResourceKey> getValue(final Parameter.Key<? super ResourceKey> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final ResourceKey worldKey;

        try {
            worldKey = reader.parseResourceKey();
        } catch (final Exception ignored) {
            throw reader.createException(this.errorMessage.apply(context.getCause().getAudience()).red().asText());
        }

        final Optional<ResourceKey> optionalValue = Optional.ofNullable(worldKey).filter(this.filter);

        if (optionalValue.isPresent()) {
            return optionalValue;
        }

        throw reader.createException(this.errorMessage.apply(context.getCause().getAudience()).red().asText());
    }

}
