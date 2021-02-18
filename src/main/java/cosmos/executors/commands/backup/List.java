package cosmos.executors.commands.backup;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
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
        super(CosmosParameters.BACKUP_WORLD.get().optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final Optional<ResourceKey> optionalWorldKey = context.one(CosmosKeys.WORLD);

        final Collection<Component> contents = super.serviceProvider.backup().backups()
                .stream()
                .filter(backupArchetype -> !optionalWorldKey.isPresent() || optionalWorldKey.get().equals(backupArchetype.worldKey()))
                .map(backupArchetype -> super.serviceProvider.format().asText(backupArchetype, super.serviceProvider.message().getLocale(src)))
                .collect(Collectors.toList());

        final TextComponent title = super.serviceProvider.message()
                .getMessage(src, optionalWorldKey.isPresent() ? "success.backup.list.header.world" : "success.backup.list.header.all")
                .replace("number", contents.size())
                .replace("world", optionalWorldKey.isPresent() ? optionalWorldKey.get() : "")
                .gray()
                .asText();

        super.serviceProvider.pagination().send(src, title, contents, false);
    }

}
