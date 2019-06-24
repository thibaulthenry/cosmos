package cosmos.commands.management;

import cosmos.commands.AbstractCommand;
import cosmos.utils.Permission;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public class New extends AbstractCommand {

    public New() {
        super(Permission.COMMAND_MANAGEMENT_NEW);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));

        DimensionType dimensionType = args.<DimensionType>getOne("world-dimension")
                .orElseThrow(supplyError("Please insert a valid dimension type"));

        Optional<GeneratorType> optionalGeneratorType = args.getOne("world-generator");

        Collection<WorldGeneratorModifier> worldGeneratorModifiers = args.getAll("world-modifiers");

        if (Sponge.getServer().getWorldProperties(worldName).isPresent()) {
            throw new CommandException(Text.of(worldName, " is already imported on this server"));
        }

        WorldArchetype.Builder worldArchetypeBuilder = WorldArchetype.builder()
                .dimension(dimensionType)
                .generator(optionalGeneratorType.orElse(GeneratorTypes.DEFAULT))
                .enabled(true)
                .generateSpawnOnLoad(true)
                .keepsSpawnLoaded(true)
                .loadsOnStartup(true);

        WorldArchetype worldArchetype = worldArchetypeBuilder.build(worldName + Instant.now(), worldName);

        WorldProperties worldProperties;
        try {
            worldProperties = Sponge.getServer().createWorldProperties(worldName, worldArchetype);
            worldProperties.setGeneratorModifiers(worldGeneratorModifiers);
        } catch (IOException e) {
            logError(e, "An error occurred while creating ", worldName, "'s properties");
            throw new CommandException(Text.of("An error occurred while creating ", worldName, "'s properties"), e);
        }

        if (Sponge.getServer().saveWorldProperties(worldProperties)) {
            src.sendMessage(Text.of(TextColors.GREEN, worldName, " has been created successfully"));
        } else {
            throw new CommandException(Text.of("An error occurred while saving ", worldName, "'s properties"));
        }

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.string(Text.of("world-name")),
                        //TODO make dimension optional
                        GenericArguments.dimension(Text.of("world-dimension")),
                        GenericArguments.optional(
                                GenericArguments.onlyOne(
                                        GenericArguments.catalogedElement(Text.of("world-generator"), GeneratorType.class)
                                )
                        ),
                        GenericArguments.optional(
                                GenericArguments.catalogedElement(Text.of("world-modifiers"), WorldGeneratorModifier.class)
                        )
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
