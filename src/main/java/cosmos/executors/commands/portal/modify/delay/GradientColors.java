package cosmos.executors.commands.portal.modify.delay;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
public class GradientColors extends AbstractDelayModifyCommand {

    public GradientColors() {
        super(CosmosParameters.COLOR.get().consumeAllRemaining().optional().build());
    }

    @Override
    protected CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final Collection<? extends NamedTextColor> delayGradientColors = context.all(CosmosKeys.COLOR);

        if (delayGradientColors.size() == 0) {
            return portal.asBuilder().delayGradientColors(null).build();
        }

        super.formattedModifiedValue = Component.join(
                Component.text("-", NamedTextColor.DARK_GRAY),
                delayGradientColors.stream()
                        .map(color -> Component.text(color.toString(), color))
                        .collect(Collectors.toList())
        );

        return portal.asBuilder().delayGradientColors(delayGradientColors).build();
    }

}
