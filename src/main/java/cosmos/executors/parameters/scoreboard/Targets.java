package cosmos.executors.parameters.scoreboard;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.parameters.CosmosFirstOfBuilder;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameterModifier;
import org.spongepowered.api.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Targets implements CosmosFirstOfBuilder {

    private final Parameter.FirstOfBuilder builder;

    private Parameter.Key<List<Entity>> entitiesKey;
    private Parameter.Key<Component> textAmpersandKey;
    private Parameter.Key<Component> textJsonKey;

    public Targets() {
        this.builder = Sponge.game().builderProvider().provide(Parameter.FirstOfBuilder.class);
        this.entitiesKey = CosmosKeys.ENTITIES;
        this.textAmpersandKey = CosmosKeys.TEXT_AMPERSAND;
        this.textJsonKey = CosmosKeys.TEXT_JSON;
    }

    @Override
    public Parameter build() {
        return this.builder
                .or(
                        CosmosParameters.ENTITIES.get()
                                .key(this.entitiesKey)
                                .modifier(new EntitiesModifier())
                                .build()
                )
                .or(Parameter.formattingCodeText().key(this.textAmpersandKey).build())
                .or(Parameter.jsonText().key(this.textJsonKey).build())
                .build();
    }

    public Targets entitiesKey(final Parameter.Key<List<Entity>> entitiesKey) {
        this.entitiesKey = entitiesKey;
        return this;
    }

    public Targets entitiesKey(final String entitiesKey) {
        return this.entitiesKey(Parameter.key(entitiesKey, new TypeToken<List<Entity>>() {}));
    }

    @Override
    public Targets optional() {
        this.builder.optional();
        return this;
    }

    public Targets textAmpersandKey(final Parameter.Key<Component> textAmpersandKey) {
        this.textAmpersandKey = textAmpersandKey;
        return this;
    }

    public Targets textAmpersandKey(final String textAmpersandKey) {
        return this.textAmpersandKey(Parameter.key(textAmpersandKey, TypeToken.get(Component.class)));
    }

    public Targets textJsonKey(final Parameter.Key<Component> textJsonKey) {
        this.textJsonKey = textJsonKey;
        return this;
    }

    public Targets textJsonKey(final String textJsonKey) {
        return this.textJsonKey(Parameter.key(textJsonKey, TypeToken.get(Component.class)));
    }

    private static final class EntitiesModifier implements ValueParameterModifier<List<Entity>> {

        private boolean isAllowedUnquotedString(final String displayName) {
            return displayName.matches("^[a-zA-Z0-9_\\-.+]+$");
        }

        @Override
        public Optional<? extends List<Entity>> modifyResult(final Parameter.Key<? super List<Entity>> parameterKey,
                                                             final ArgumentReader.Immutable reader, final CommandContext.Builder context,
                                                             @Nullable final List<Entity> value) throws ArgumentParseException {
            if (value == null || value.isEmpty()) {
                throw reader.createException(Component.text("No entities found"));
            }

            return Optional.of(value);
        }

        @Override
        public List<CommandCompletion> modifyCompletion(final CommandContext context, final String currentInput, final List<CommandCompletion> completions) {
            completions.addAll(this.scoreHoldersChoices(context, currentInput));

            if (currentInput == null || currentInput.isEmpty() || "*".equals(currentInput) || "'*'".startsWith(currentInput)) {
                completions.add(CommandCompletion.of("'*'"));
            }

            return completions;
        }

        private Collection<CommandCompletion> scoreHoldersChoices(final CommandContext context, final String currentInput) {
            return Cosmos.services().world().findKeyOrSource(context)
                    .map(worldKey -> Cosmos.services().scoreboard().scoreHolders(worldKey))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(displayName -> LegacyComponentSerializer.legacyAmpersand().serialize(displayName))
                    .filter(formattedName -> formattedName.startsWith(currentInput))
                    .map(formattedName -> {
                        if (this.isAllowedUnquotedString(formattedName)) {
                            return formattedName;
                        }

                        if (!formattedName.contains("'")) {
                            return "'" + formattedName + "'";
                        }

                        return "'" + formattedName.replace("'", "\\'") + "'";
                    })
                    .map(CommandCompletion::of)
                    .collect(Collectors.toList());
        }

    }

}
