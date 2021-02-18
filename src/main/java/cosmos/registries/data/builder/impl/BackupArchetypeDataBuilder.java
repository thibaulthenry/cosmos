package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.BackupArchetypeData;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

@Singleton
public class BackupArchetypeDataBuilder extends AbstractDataBuilder<BackupArchetypeData> {

    @Inject
    public BackupArchetypeDataBuilder() {
        this(BackupArchetypeData.class, 1);
    }

    protected BackupArchetypeDataBuilder(final Class<BackupArchetypeData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<BackupArchetypeData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Backup.DATE, Queries.Backup.WORLD)) {
            return Optional.empty();
        }

        final String date = container.getString(Queries.Backup.DATE)
                .orElseThrow(() -> new InvalidDataException("Missing date while building BackupArchetypeData"));

        final String tag = container.getString(Queries.Backup.TAG).orElse(null);

        final ResourceKey worldKey = container.getResourceKey(Queries.Backup.WORLD)
                .orElseThrow(() -> new InvalidDataException("Missing world level while building BackupArchetypeData"));

        return Optional.of(new BackupArchetypeData(date, worldKey, tag));
    }

}
