package cosmos.executors.commands.properties;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.gamerule.GameRuleValue;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.lang.reflect.Type;
import java.util.Optional;

public class GameRule extends AbstractPropertiesCommand {

    @Inject
    public GameRule(final Injector injector) {
        super(
                CosmosParameters.GAME_RULE,
                Parameter.builder(Object.class, injector.getInstance(GameRuleValue.class))
                        .setKey(CosmosKeys.GAME_RULE_VALUE)
                        .optional()
                        .build()
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final org.spongepowered.api.world.gamerule.GameRule<?> gameRule = context.getOne(CosmosKeys.GAME_RULE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.GAME_RULE));

        final Type valueType = gameRule.getValueType();
        final Optional<Object> optionalInput = context.getOne(CosmosKeys.GAME_RULE_VALUE);
        final Object value;

        if (Boolean.class.equals(valueType) || boolean.class.equals(valueType)) {
            final org.spongepowered.api.world.gamerule.GameRule<Boolean> typedRule =
                    (org.spongepowered.api.world.gamerule.GameRule<Boolean>) gameRule;

            if (optionalInput.isPresent()) {
                value = optionalInput.get();
                properties.setGameRule(typedRule, (Boolean) value);
                super.serviceProvider.world().saveProperties(src, properties);
            } else {
                value = properties.getGameRule(typedRule);
            }
        } else if (Integer.class.equals(valueType) || int.class.equals(valueType)) {
            final org.spongepowered.api.world.gamerule.GameRule<Integer> typedRule =
                    (org.spongepowered.api.world.gamerule.GameRule<Integer>) gameRule;

            if (optionalInput.isPresent()) {
                value = optionalInput.get();
                properties.setGameRule(typedRule, (Integer) value);
                super.serviceProvider.world().saveProperties(src, properties);
            } else {
                value = properties.getGameRule(typedRule);
            }
        } else {
            final org.spongepowered.api.world.gamerule.GameRule<String> typedRule =
                    (org.spongepowered.api.world.gamerule.GameRule<String>) gameRule;

            if (optionalInput.isPresent()) {
                value = optionalInput.get();
                properties.setGameRule(typedRule, value.toString());
                super.serviceProvider.world().saveProperties(src, properties);
            } else {
                value = properties.getGameRule(typedRule);
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
