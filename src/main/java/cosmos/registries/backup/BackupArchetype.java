package cosmos.registries.backup;

import cosmos.Cosmos;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerWorld;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Optional;

public class BackupArchetype {

    private final ResourceKey worldKey;
    private final LocalDateTime creationDateTime;
    private final String creationDateTimeFormatted;
    private String tag;

    private BackupArchetype(final ResourceKey worldKey) throws IOException {
        if (Cosmos.getServices().world().isOnline(worldKey)) {
            throw new IOException(); //todo
        }

        final LocalDateTime creationDateTime = LocalDateTime.now();
        this.creationDateTimeFormatted = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(creationDateTime);
        this.worldKey = worldKey;
        //this.uuid = Sponge.getServer().getWorldManager().
        this.creationDateTime = creationDateTime;
    }

    private BackupArchetype(final Path backupDirectory) throws IOException, DateTimeParseException {
        final String[] parts = backupDirectory.getFileName().toString().split("_");

        if (parts.length < 3) {
            throw new IOException("Invalid backup folder name");
        }

        this.worldKey = ResourceKey.resolve(parts[0]);
        this.creationDateTime = LocalDateTime.parse(parts[1] + parts[2], DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.creationDateTimeFormatted = parts[1] + "_" + parts[2];

        if (parts.length > 3) {
            this.tag = parts[3];
        }
    }

    public static Optional<BackupArchetype> fromKey(final ResourceKey worldKey) { // todo move to backup service
        try {
            return Optional.of(new BackupArchetype(worldKey));
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An expected error occurred while reading backup data", e);
            return Optional.empty();
        }
    }

    public static Optional<BackupArchetype> fromDirectory(final Path backupDirectory) { // todo mvoe to backup service
        try {
            return Optional.of(new BackupArchetype(backupDirectory));
        } catch (final Exception e) {
            Cosmos.getLogger().warn("An expected error occurred while reading backup data", e);
            return Optional.empty();
        }
    }

    public String getName() {
        return this.worldKey.asString() + "_" + (this.tag == null ? this.creationDateTimeFormatted : this.tag);
    }

    public ResourceKey getWorldKey() {
        return worldKey;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public Optional<String> getTag() {
        return Optional.ofNullable(tag);
    }

    public void setTag(final String tag) {
        if (tag == null || tag.isEmpty()) {
            return;
        }

        this.tag = tag;
    }

    public TextComponent toText(final Audience src) { // todo move to formatter and add translation
        final TextComponent bulletPoint = Component.text(" • ");
        final TextComponent breakLine = Component.newline().append(Component.newline());

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Cosmos.getServices().message().getLocale(src));

        final String name = this.getName();

        final TextComponent hoverText = Component.text().append(
                Component.text(name),
                breakLine, bulletPoint,
                Component.text("World name: ", NamedTextColor.GRAY),
                Component.text(this.worldKey.getFormatted(), NamedTextColor.GREEN),
                breakLine, bulletPoint,
                Component.text("Creation date: ", NamedTextColor.GRAY),
                Component.text(dateTimeFormatter.format(this.creationDateTime), NamedTextColor.GREEN),
                breakLine, bulletPoint,
                tag == null ? Component.text("Not tagged", NamedTextColor.RED) : Component.text("Tag: ", NamedTextColor.RED).append(Component.text(this.tag, NamedTextColor.GREEN))
        ).build();

        return Component.text(name, NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(hoverText));
    }

    public Optional<ServerWorld> getLinkedWorld() {
        return Sponge.getServer().getWorldManager().world(this.worldKey);
    }

}
