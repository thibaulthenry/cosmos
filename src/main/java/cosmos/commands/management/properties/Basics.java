package cosmos.commands.management.properties;

import com.flowpowered.math.vector.Vector3d;
import cosmos.commands.AbstractCommand;
import cosmos.utils.Finder;
import cosmos.utils.Permission;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class Basics extends AbstractCommand {

    public Basics() {
        super(Permission.COMMAND_MANAGEMENT_PROPERTIES_BASICS);
    }

    static WorldProperties getWorldProperties(String worldName) throws CommandException {
        Optional<WorldProperties> optionalWorldProperties = Sponge.getServer().getWorldProperties(worldName);

        if (!optionalWorldProperties.isPresent()) {
            throw new CommandException(Text.of("An error occurred while looking for ", worldName, "'s properties"));
        }

        return optionalWorldProperties.get();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getAvailableWorldNames(true)).keySet(),
                                key -> Finder.toMap(Finder.getAvailableWorldNames(true)).get(key)
                        ),
                        GenericArguments.flags()
                                .valueFlag(GenericArguments.bool(Text.of("enabled")), "-enabled")
                                .valueFlag(GenericArguments.bool(Text.of("load-on-startup")), "-load-on-startup")
                                .valueFlag(GenericArguments.bool(Text.of("keep-spawn-loaded")), "-keep-spawn-loaded")
                                .valueFlag(GenericArguments.bool(Text.of("generate-spawn-on-load")), "-generate-spawn-on-load")
                                .valueFlag(GenericArguments.vector3d(Text.of("spawn-position")), "-spawn-position")
                                .valueFlag(GenericArguments.catalogedElement(Text.of("generator-type"), GeneratorType.class), "-generator-type")
                                .valueFlag(GenericArguments.longNum(Text.of("seed")), "-seed")
                                .valueFlag(GenericArguments.bool(Text.of("pvp")), "-pvp")
                                .valueFlag(GenericArguments.catalogedElement(Text.of("game-mode"), GameMode.class), "-game-mode")
                                .valueFlag(GenericArguments.bool(Text.of("uses-map-features")), "-uses-map-features")
                                .valueFlag(GenericArguments.bool(Text.of("hardcore")), "-hardcore")
                                .valueFlag(GenericArguments.bool(Text.of("commands-allowed")), "-commands-allowed")
                                .valueFlag(GenericArguments.catalogedElement(Text.of("difficulty"), Difficulty.class), "-difficulty")
                                .buildWith(GenericArguments.none())
                )
                .permission(permission)
                .executor(this)
                .build();
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));

        WorldProperties worldProperties = getWorldProperties(worldName);

        args.<Boolean>getOne("enabled").ifPresent(worldProperties::setEnabled);
        args.<Boolean>getOne("load-on-startup").ifPresent(worldProperties::setLoadOnStartup);
        args.<Boolean>getOne("keep-spawn-loaded").ifPresent(worldProperties::setKeepSpawnLoaded);
        args.<Boolean>getOne("generate-spawn-on-load").ifPresent(worldProperties::setGenerateSpawnOnLoad);
        args.<Vector3d>getOne("spawn-position").ifPresent(spawnPosition ->
                worldProperties.setSpawnPosition(spawnPosition.toInt())
        );
        args.<GeneratorType>getOne("generator-type").ifPresent(worldProperties::setGeneratorType);
        args.<Long>getOne("seed").ifPresent(worldProperties::setSeed);
        args.<Boolean>getOne("pvp").ifPresent(worldProperties::setPVPEnabled);
        args.<GameMode>getOne("game-mode").ifPresent(worldProperties::setGameMode);
        args.<Boolean>getOne("uses-map-features").ifPresent(worldProperties::setMapFeaturesEnabled);
        args.<Boolean>getOne("hardcore").ifPresent(worldProperties::setHardcore);
        args.<Boolean>getOne("commands-allowed").ifPresent(worldProperties::setCommandsAllowed);
        args.<Difficulty>getOne("difficulty").ifPresent(worldProperties::setDifficulty);

        if (Sponge.getServer().saveWorldProperties(worldProperties)) {
            src.sendMessage(Text.of(TextColors.GREEN, worldName, "'s properties has been changed successfully"));
        } else {
            throw new CommandException(Text.of("An error occurred while changing ", worldName, "'s properties"));
        }

        return CommandResult.success();
    }
}
