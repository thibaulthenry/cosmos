package cosmos.executors.commands.backup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.impl.backup.Backup;
import cosmos.registries.backup.BackupArchetype;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Optional;

@Singleton
public class Reset extends AbstractCommand {

    @Inject
    public Reset(final Injector injector) {
        super(injector.getInstance(Backup.class).builder().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final BackupArchetype backupArchetype = context.getOne(CosmosKeys.BACKUP)
                .orElseThrow(this.serviceProvider.message().supplyError(src, "error.invalid.choices.backup"));

        final Optional<ServerWorld> optionalLinkedWorld = backupArchetype.getLinkedWorld();

        if (optionalLinkedWorld.isPresent()) {
            this.serviceProvider.world().unload(src, optionalLinkedWorld.get());
        }

        this.serviceProvider.world().restore(src, backupArchetype, true);

        this.serviceProvider.world().load(src, backupArchetype.getWorldKey(), true);

        this.serviceProvider.message()
                .getMessage(src, "success.backup.reset")
                .replace("world", backupArchetype.getWorldKey())
                .replace("backup", backupArchetype)
                .green()
                .sendTo(src);
    }

}
