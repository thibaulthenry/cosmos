package cosmos.registries.backup;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerWorld;

import java.time.LocalDateTime;
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

    public ZonedDateTime getCreationDateTime() {
        return this.creationDateTime;
    }

    public Optional<ServerWorld> getLinkedWorld() {
        return Sponge.getServer().getWorldManager().world(this.worldKey);
    }

    public String getName() {
        return this.worldKey.getFormatted().replaceAll(":", "_") + "_" + (this.tag == null ? this.creationDateTimeFormatted : this.tag);
    }

    public Optional<String> getTag() {
        return Optional.ofNullable(this.tag);
    }

    public ResourceKey getWorldKey() {
        return this.worldKey;
    }

    public void setTag(final String tag) {
        if (tag == null || tag.isEmpty()) {
            return;
        }

        this.tag = tag;
    }

}
