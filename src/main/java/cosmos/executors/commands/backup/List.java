package cosmos.executors.commands.backup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.backup.BackupWorld;
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

    @Inject
    public List(final Injector injector) {
        super(injector.getInstance(BackupWorld.class).builder().optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final Optional<ResourceKey> optionalWorldName = context.getOne(CosmosKeys.WORLD_KEY);

        final Collection<Component> contents = this.serviceProvider.backup().getBackups()
                .stream()
                .filter(backupArchetype -> !optionalWorldName.isPresent()
                        || optionalWorldName.get().equals(backupArchetype.getWorldKey()))
                .map(backupArchetype -> backupArchetype.toText(src))
                .collect(Collectors.toList());

        final TextComponent title = optionalWorldName
                .map(worldKey -> this.serviceProvider.message()
                        .getMessage(src, "success.backup.list.world")
                        .replace("number", contents.size())
                        .replace("world", worldKey)
                        .green()
                        .asText()
                )
                .orElseGet(() -> this.serviceProvider.message()
                        .getMessage(src, "success.backup.list.all")
                        .replace("number", contents.size())
                        .green()
                        .asText()
                );

        this.serviceProvider.pagination().send(src, title, contents, false);
    }

}
