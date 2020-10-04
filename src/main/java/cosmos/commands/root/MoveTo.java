package cosmos.commands.root;

import com.flowpowered.math.vector.Vector3d;
import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.SelectorArguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MoveTo extends AbstractCommand {

    public MoveTo() {
        super(
                GenericArguments.onlyOne(
                        Arguments.limitCompleteElement(
                                SelectorArguments.entity(ArgKeys.DESTINATION_TARGET.t)
                        )
                ),
                Arguments.limitCompleteElement(
                        SelectorArguments.entityOrSource(ArgKeys.TARGETS.t)
                ),
                Arguments.baseFlag(ArgKeys.SAFE_ONLY)
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        Entity destinationTarget = args.<Entity>getOne(ArgKeys.DESTINATION_TARGET.t)
                .orElseThrow(Outputs.INVALID_DESTINATION_TARGET.asSupplier());

        Location<World> location = destinationTarget.getLocation();
        Vector3d rotation = destinationTarget.getRotation();

        Collection<Text> contents = args.<Entity>getAll(ArgKeys.TARGETS.t)
                .stream()
                .map(target -> {
                    boolean notify = Move.mustNotify(src, target);
                    boolean self = Move.isSelf(src, target);
                    boolean selfDestination = Move.isSelf(src, destinationTarget);

                    String targetName = target instanceof Tamer ? ((Tamer) target).getName() : target.getUniqueId().toString();
                    String destinationTargetName = destinationTarget instanceof Tamer ?
                            ((Tamer) destinationTarget).getName() :
                            destinationTarget.getUniqueId().toString();

                    Function<String, Text> successFunction = (mutableText) -> {
                        if (notify) {
                            ((MessageReceiver) target).sendMessage(Outputs.TRANSFER_TO_PLAYER.asText(
                                    "You", "have", mutableText, destinationTargetName)
                            );
                        }

                        return Outputs.TRANSFER_TO_PLAYER.asText(
                                self ? "You" : targetName, self ? "have" : "has", mutableText,
                                selfDestination ? "yourself" : destinationTargetName
                        );
                    };

                    Function<String, Text> errorFunction = (mutableText) -> Outputs.TRANSFERRING_TO_PLAYER.asText(
                            self ? "You" : targetName, mutableText,
                            selfDestination ? "yourself" : destinationTargetName
                    );

                    return Move.move(target, location, rotation, successFunction, errorFunction, args.hasAny(ArgKeys.SAFE_ONLY.t));
                })
                .collect(Collectors.toList());

        sendPaginatedOutput(src, Outputs.SHOW_ENTITIES_TRANSPORT.asText(contents.size()), contents, true);
    }
}
