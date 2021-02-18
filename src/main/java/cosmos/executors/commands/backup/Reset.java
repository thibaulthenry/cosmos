package cosmos.executors.commands.backup;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.AbstractCommand;
import cosmos.registries.backup.BackupArchetype;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Optional;

@Singleton
public class Reset extends AbstractCommand {

    public Reset() {
        super(CosmosParameters.BACKUP.get().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final BackupArchetype backupArchetype = context.one(CosmosKeys.BACKUP)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.backup", "param", CosmosKeys.BACKUP));

        final Optional<ServerWorld> optionalLinkedWorld = backupArchetype.linkedWorld();

        if (optionalLinkedWorld.isPresent()) {
            super.serviceProvider.world().unload(src, optionalLinkedWorld.get());
        }

        super.serviceProvider.world().restore(src, backupArchetype, true);
        super.serviceProvider.world().load(src, backupArchetype.worldKey(), true);

        super.serviceProvider.message()
                .getMessage(src, "success.backup.reset")
                .replace("backup", backupArchetype)
                .replace("world", backupArchetype.worldKey())
                .green()
                .sendTo(src);
    }

}
