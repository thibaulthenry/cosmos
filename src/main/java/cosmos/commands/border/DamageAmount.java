package cosmos.commands.border;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class DamageAmount extends AbstractBorderCommand {

    public DamageAmount() {
        super(
                GenericArguments.optional(
                        GenericArguments.doubleNum(ArgKeys.AMOUNT.t)
                )
        );
    }

    @Override
    void runWithBorder(CommandSource src, CommandContext args, WorldProperties worldProperties, WorldBorder worldBorder) throws CommandException {
        Optional<Double> optionalAmount = args.getOne(ArgKeys.AMOUNT.t);
        double amount = worldBorder.getDamageAmount();
        String mutableText = "currently";

        if (optionalAmount.isPresent()) {
            amount = optionalAmount.get();
            mutableText = "successfully";
            worldBorder.setDamageAmount(amount);
            worldProperties.setWorldBorderDamageAmount(amount);
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(Outputs.BORDER_DAMAGE_AMOUNT.asText(worldProperties, mutableText, amount));
    }
}
