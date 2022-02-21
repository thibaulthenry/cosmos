package cosmos.executors.commands.root;

import cosmos.constants.CosmosKeys;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.Identifiable;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class Test extends AbstractCommand {

    @Override
    protected void run(Audience src, CommandContext context) throws CommandException {
        java.util.List<Component> test = context.one(CosmosKeys.ENTITIES)
                .map(entities -> entities.stream().map(Identifiable::uniqueId).map(uuid -> Component.text(uuid.toString()).asComponent()).collect(Collectors.toList()))
                .map(Optional::of)
                .orElse(context.one(CosmosKeys.STATE).map(Component::text).map(Collections::singletonList))
                .map(Optional::of)
                .orElse(context.one(CosmosKeys.TEXT_AMPERSAND).map(Collections::singletonList))
                .map(Optional::of)
                .orElse(context.one(CosmosKeys.TEXT_JSON).map(Collections::singletonList))
                .orElseThrow(() -> new CommandException(Component.text("no")));

        System.out.println(test.size());
    }

}
