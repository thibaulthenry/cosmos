package cosmos.registries.data.serializable.impl;

import cosmos.Cosmos;
import cosmos.constants.Queries;
import cosmos.registries.backup.BackupArchetype;
import cosmos.registries.data.serializable.CollectorSerializable;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BackupArchetypeData implements CollectorSerializable<BackupArchetype> {

    private final String date;
    private final String tag;
    private final ResourceKey worldKey;

    public BackupArchetypeData(final BackupArchetype backupArchetype) {
        this.date = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(backupArchetype.getCreationDateTime());
        this.tag = backupArchetype.getTag().orElse(null);
        this.worldKey = backupArchetype.getWorldKey();
    }

    public BackupArchetypeData(final String date, final ResourceKey worldKey, @Nullable final String tag) {
        this.date = date;
        this.tag = tag;
        this.worldKey = worldKey;
    }

    @Override
    public Optional<BackupArchetype> collect() {
        final ZonedDateTime creationDateTime;

        try {
            creationDateTime = ZonedDateTime.parse(this.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault()));
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An unexpected error occurred while collecting BackupArchetype data", e);
            return Optional.empty();
        }

        return Optional.of(new BackupArchetype(creationDateTime, this.worldKey, this.tag));
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer dataContainer = DataContainer.createNew()
                .set(Queries.Backup.DATE, this.date)
                .set(Queries.Backup.WORLD, this.worldKey);

        if (this.tag != null) {
            dataContainer.set(Queries.Backup.TAG, this.tag);
        }

        return dataContainer;
    }

}
