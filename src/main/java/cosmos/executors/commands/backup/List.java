package cosmos.executors.commands.backup;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class List extends AbstractCommand {

    public List() {
        super(CosmosParameters.Builder.BACKUP_WORLD.get().optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final Optional<ResourceKey> optionalWorldKey = context.getOne(CosmosKeys.WORLD);

        final Collection<Component> contents = super.serviceProvider.backup().getBackups()
                .stream()
                .filter(backupArchetype -> !optionalWorldKey.isPresent() || optionalWorldKey.get().equals(backupArchetype.getWorldKey()))
                .map(backupArchetype -> super.serviceProvider.format().asText(backupArchetype, super.serviceProvider.message().getLocale(src)))
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, optionalWorldKey.isPresent() ? "success.backup.header.world" : "success.backup.header.all")
                .replace("number", contents.size())
                .replace("world", optionalWorldKey.isPresent() ? optionalWorldKey.get() : "")
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, false);
    }

}
