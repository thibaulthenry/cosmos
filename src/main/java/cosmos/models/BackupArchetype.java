package cosmos.models;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;

public class BackupArchetype {

    private final String worldName;
    private final String worldUUID;
    private final LocalDateTime creationDateTime;
    private final String creationDateTimeFormatted;
    private String tag;

    public BackupArchetype(WorldProperties worldProperties) {
        LocalDateTime creationDateTime = LocalDateTime.now();
        this.creationDateTimeFormatted = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(creationDateTime);
        this.worldName = worldProperties.getWorldName();
        this.worldUUID = worldProperties.getUniqueId().toString();
        this.creationDateTime = creationDateTime;
    }

    private BackupArchetype(Path backupDirectory) throws IOException, DateTimeParseException {
        String[] parts = backupDirectory.getFileName().toString().split("_");

        if (parts.length < 4) {
            throw new IOException("Invalid backup folder name");
        }

        this.worldName = parts[0];
        this.creationDateTime = LocalDateTime.parse(parts[1] + parts[2], DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.worldUUID = parts[3];
        this.creationDateTimeFormatted = parts[1] + "_" + parts[2];

        if (parts.length > 4) {
            this.tag = parts[4];
        }
    }

    public static Optional<BackupArchetype> fromDirectory(Path backupDirectory) {
        try {
            return Optional.of(new BackupArchetype(backupDirectory));
        } catch (IOException | DateTimeParseException ignored) {
            return Optional.empty();
        }
    }

    public String getName() {
        return worldName + "_" + (tag == null ? creationDateTimeFormatted : tag);
    }

    public String getWorldName() {
        return worldName;
    }

    public String getWorldUUID() {
        return worldUUID;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public Optional<String> getTag() {
        return Optional.ofNullable(tag);
    }

    public void setTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            return;
        }

        this.tag = tag;
    }


    public Text toText(Locale locale) {
        Text bulletPoint = Text.of(TextColors.WHITE, " • ");
        Text breakLine = Text.of(Text.NEW_LINE, Text.NEW_LINE);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);

        String name = getName();

        Text.Builder hoverTextBuilder = Text.builder().append(
                Text.of(
                        TextColors.WHITE, name, breakLine, bulletPoint,
                        TextColors.GRAY, "World name: ", TextColors.GREEN, worldName, breakLine, bulletPoint,
                        TextColors.GRAY, "World UUID: ", TextColors.GREEN, worldUUID, breakLine, bulletPoint,
                        TextColors.GRAY, "Creation date: ", TextColors.GREEN, dateTimeFormatter.format(creationDateTime),
                        breakLine, bulletPoint, tag == null ?
                                Text.of(TextColors.RED, "Not tagged") :
                                Text.of(TextColors.GRAY, "Tag: ", TextColors.GREEN, tag)
                )
        );

        return Text.builder()
                .append(Text.of(TextColors.GREEN, name))
                .onHover(TextActions.showText(hoverTextBuilder.build()))
                .build();
    }

    public Optional<World> getLoadedWorld() {
        return Sponge.getServer().getWorld(worldUUID);
    }

}
