package cosmos.registries.backup;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerWorld;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BackupArchetype {

    private final ZonedDateTime creationDateTime;
    private final String creationDateTimeFormatted;
    private final ResourceKey worldKey;

    private String tag;

    public BackupArchetype(final ResourceKey worldKey) {
        final ZonedDateTime creationDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        this.creationDateTime = creationDateTime;
        this.creationDateTimeFormatted = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(creationDateTime);
        this.worldKey = worldKey;
    }

    public BackupArchetype(final ZonedDateTime creationDateTime, final ResourceKey worldKey, @Nullable final String tag) {
        this.creationDateTime = creationDateTime;
        this.creationDateTimeFormatted = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(creationDateTime);
        this.worldKey = worldKey;
        this.tag = tag;
    }

    public ZonedDateTime creationDateTime() {
        return this.creationDateTime;
    }

    public Optional<ServerWorld> linkedWorld() {
        return Sponge.server().worldManager().world(this.worldKey);
    }

    public String name() {
        return this.worldKey.formatted().replaceAll(":", "_") + "_" + (this.tag == null ? this.creationDateTimeFormatted : this.tag);
    }

    public Optional<String> tag() {
        return Optional.ofNullable(this.tag);
    }

    public void tag(final String tag) {
        if (tag == null || tag.isEmpty()) {
            return;
        }

        this.tag = tag;
    }

    public ResourceKey worldKey() {
        return this.worldKey;
    }

}
