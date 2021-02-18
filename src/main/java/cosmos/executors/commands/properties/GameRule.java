package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.lang.reflect.Type;
import java.util.Optional;

@Singleton
public class GameRule extends AbstractPropertiesCommand {

    public GameRule() {
        super(
                Parameter.registryElement(new TypeToken<org.spongepowered.api.world.gamerule.GameRule<?>>() {}, RegistryTypes.GAME_RULE, ResourceKey.SPONGE_NAMESPACE)
                        .key(CosmosKeys.GAME_RULE)
                        .build(),
                CosmosParameters.GAME_RULE_VALUE_ALL.get().optional().build()
                // TODO https://github.com/SpongePowered/Sponge/issues/3340
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final org.spongepowered.api.world.gamerule.GameRule<?> gameRule = context.one(CosmosKeys.GAME_RULE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.GAME_RULE));

        final Type valueType = gameRule.valueType();
        final Optional<Object> optionalInput = context.one(CosmosKeys.GAME_RULE_VALUE);
        final Object value;

        if (Boolean.class.equals(valueType) || boolean.class.equals(valueType)) {
            final org.spongepowered.api.world.gamerule.GameRule<Boolean> typedRule =
                    (org.spongepowered.api.world.gamerule.GameRule<Boolean>) gameRule;

            if (optionalInput.isPresent()) {
                value = optionalInput.get();
                properties.setGameRule(typedRule, (Boolean) value);
                super.serviceProvider.world().saveProperties(src, properties);
            } else {
                value = properties.gameRule(typedRule);
            }
        } else if (Integer.class.equals(valueType) || int.class.equals(valueType)) {
            final org.spongepowered.api.world.gamerule.GameRule<Integer> typedRule =
                    (org.spongepowered.api.world.gamerule.GameRule<Integer>) gameRule;

            if (optionalInput.isPresent()) {
                value = optionalInput.get();
                properties.setGameRule(typedRule, (Integer) value);
                super.serviceProvider.world().saveProperties(src, properties);
            } else {
                value = properties.gameRule(typedRule);
            }
        } else {
            final org.spongepowered.api.world.gamerule.GameRule<String> typedRule =
                    (org.spongepowered.api.world.gamerule.GameRule<String>) gameRule;

            if (optionalInput.isPresent()) {
                value = optionalInput.get();
                properties.setGameRule(typedRule, value.toString());
                super.serviceProvider.world().saveProperties(src, properties);
            } else {
                value = properties.gameRule(typedRule);
            }
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.game-rule.set" : "success.properties.game-rule.get")
                .replace("rule", gameRule.key(RegistryTypes.GAME_RULE))
                .replace("value", value)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
