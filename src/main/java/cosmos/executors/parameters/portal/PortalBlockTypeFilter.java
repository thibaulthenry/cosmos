package cosmos.executors.parameters.portal;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.registries.message.Message;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.clientcompletion.ClientCompletionType;
import org.spongepowered.api.command.parameter.managed.clientcompletion.ClientCompletionTypes;
import org.spongepowered.api.registry.RegistryEntry;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class PortalBlockTypeFilter implements ValueParameter<BlockType> {

    private final Function<Audience, Message> errorMessage;
    private final Predicate<BlockType> filter;

    PortalBlockTypeFilter(final Function<Audience, Message> errorMessage, final Predicate<BlockType> filter) {
        this.errorMessage = errorMessage;
        this.filter = filter;
    }

    @Override
    public List<ClientCompletionType> clientCompletionType() {
        return Collections.singletonList(ClientCompletionTypes.RESOURCE_KEY.get());
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return RegistryTypes.BLOCK_TYPE.get()
                .streamEntries()
                .filter(entry -> context.one(CosmosKeys.PORTAL_COSMOS)
                        .filter(portal -> portal.type().isAnyOfTriggers(entry.value()))
                        .isPresent()
                )
                .filter(entry -> this.filter.test(entry.value()))
                .map(RegistryEntry::key)
                .map(ResourceKey::formatted)
                .filter(formatted -> formatted.contains(currentInput))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends BlockType> parseValue(final Parameter.Key<? super BlockType> parameterKey, final ArgumentReader.Mutable reader, final CommandContext.Builder context) throws ArgumentParseException {
        final ResourceKey blockTypeKey;

        final Optional<CosmosPortal> optionalPortal = context.one(CosmosKeys.PORTAL_COSMOS);

        if (!optionalPortal.isPresent()) {
            final TextComponent errorText = Cosmos.services().message()
                    .getMessage(context.cause().audience(), "error.missing.portal.any")
                    .red()
                    .asText();
            throw reader.createException(errorText);
        }

        try {
            blockTypeKey = reader.parseResourceKey();
        } catch (final Exception ignored) {
            throw reader.createException(this.errorMessage.apply(context.cause().audience()).red().asText());
        }

        final Optional<BlockType> optionalValue = Optional.ofNullable(blockTypeKey)
                .flatMap(key -> RegistryTypes.BLOCK_TYPE.get().findValue(key))
                .filter(blockType -> context.one(CosmosKeys.PORTAL_COSMOS)
                        .filter(portal -> portal.type().isAnyOfTriggers(blockType))
                        .isPresent()
                )
                .filter(this.filter);

        if (optionalValue.isPresent()) {
            return optionalValue;
        }

        throw reader.createException(this.errorMessage.apply(context.cause().audience()).red().asText());
    }

}
