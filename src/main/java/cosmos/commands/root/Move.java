package cosmos.commands.root;

import com.flowpowered.math.vector.Vector3d;
import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.SelectorArguments;
import cosmos.statics.arguments.WorldArguments;
import cosmos.statics.arguments.WorldPropertiesArguments;
import cosmos.statics.finders.FinderWorld;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO SpongePowered/SpongeAPI Issue #2253

public class Move extends AbstractCommand {

    public Move() {
        super(
                Arguments.limitCompleteElement(
                        SelectorArguments.entityOrSource(ArgKeys.TARGETS.t)
                ),
                GenericArguments.optionalWeak(
                        Arguments.limitCompleteElement(
                                GenericArguments.firstParsing(
                                        WorldArguments.loadedChoices(ArgKeys.LOADED_WORLD),
                                        WorldPropertiesArguments.unloadedChoices(ArgKeys.UNLOADED_WORLD, false)
                                )
                        )
                ),
                GenericArguments.optionalWeak(
                        GenericArguments.vector3d(ArgKeys.POSITION_XYZ.t)
                ),
                GenericArguments.optionalWeak(
                        GenericArguments.vector3d(ArgKeys.ROTATION.t)
                ),
                Arguments.baseFlag(ArgKeys.SAFE_ONLY)
        );
    }

    static Text move(Entity target, Location<World> location, @Nullable Vector3d rotation,
                     Function<String, Text> successFunction, Function<String, Text> errorFunction, boolean safeOnly) {
        if (rotation != null) {
            target.setRotation(rotation);
        }

        if (safeOnly) {
            return target.setLocationSafely(location) ?
                    successFunction.apply("safely") :
                    errorFunction.apply("safely");
        }

        if (target.setLocation(location)) {
            return successFunction.apply("dangerously");
        }

        if (target.transferToWorld(location.getExtent(), location.getPosition())) {
            return successFunction.apply("drastically");
        }

        return errorFunction.apply("drastically");
    }

    static Text move(Entity target, WorldProperties worldProperties, Vector3d position, @Nullable Vector3d rotation,
                     Function<String, Text> successFunction, Function<String, Text> errorFunction) {
        if (rotation != null) {
            target.setRotation(rotation);
        }

        if (target.transferToWorld(worldProperties.getUniqueId(), position)) {
            return successFunction.apply("drastically");
        }

        return errorFunction.apply("drastically");
    }

    static boolean isSelf(MessageReceiver src, Identifiable target) {
        if (!(src instanceof Identifiable)) {
            return false;
        }

        if (target != null) {
            return ((Identifiable) src).getUniqueId().equals(target.getUniqueId());
        }

        return false;
    }

    static boolean mustNotify(MessageReceiver src, Identifiable target) {
        if (!(src instanceof Identifiable) || !(target instanceof MessageReceiver)) {
            return false;
        }

        return !isSelf(src, target);
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties unloadedWorld = args.<WorldProperties>getOne(ArgKeys.UNLOADED_WORLD.t).orElse(null);
        boolean isLoaded = unloadedWorld == null;
        World world = isLoaded ? FinderWorld.getGivenWorldOrElse(src, args, ArgKeys.LOADED_WORLD)
                .orElseThrow(Outputs.INVALID_LOADED_UNLOADED_WORLD_CHOICE.asSupplier()) : null;

        Optional<Vector3d> optionalPosition = args.getOne(ArgKeys.POSITION_XYZ.t);

        Location<World> location = isLoaded ? optionalPosition.map(world::getLocation).orElse(world.getSpawnLocation()) : null;
        Vector3d position = isLoaded ? location.getPosition() : optionalPosition.orElse(unloadedWorld.getSpawnPosition().toDouble());
        String worldName = isLoaded ? world.getName() : unloadedWorld.getWorldName();

        if (!isLoaded) {
            Load.load(worldName, false);
        }

        Collection<Text> contents = args.<Entity>getAll(ArgKeys.TARGETS.t)
                .stream()
                .map(target -> {
                    boolean notify = mustNotify(src, target);
                    boolean self = isSelf(src, target);

//                    if (!self && !src.hasPermission(getPermission() + ".others")) {
//                        return Outputs.TRANSFERRING_OTHER.asText(target);
//                    }

                    String targetName = target instanceof Tamer ? ((Tamer) target).getName() : target.getUniqueId().toString();

                    Function<String, Text> successFunction = (mutableText) -> {
                        if (notify) {
                            ((MessageReceiver) target).sendMessage(Outputs.TRANSFER_TO_LOCATION.asText("You", "have", mutableText, worldName, position));
                        }

                        return Outputs.TRANSFER_TO_LOCATION.asText(self ? "You" : targetName, self ? "has" : "have", mutableText, worldName, position);
                    };

                    Function<String, Text> errorFunction = (mutableText) -> Outputs.TRANSFERRING_TO_LOCATION.asText(self ? "You" : targetName, mutableText, worldName, position);

                    if (isLoaded) {
                        return move(target, location, args.<Vector3d>getOne(ArgKeys.ROTATION.t).orElse(null), successFunction, errorFunction, args.hasAny(ArgKeys.SAFE_ONLY.t));
                    }

                    return move(target, unloadedWorld, position, args.<Vector3d>getOne(ArgKeys.ROTATION.t).orElse(null), successFunction, errorFunction);
                })
                .collect(Collectors.toList());

        sendPaginatedOutput(src, Outputs.SHOW_ENTITIES_TRANSPORT.asText(contents.size()), contents, true);
    }
}
