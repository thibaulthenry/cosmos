package cosmos.executors.commands.portal;

import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;

@Singleton
public class Tools extends AbstractCommand {

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        if (!(src instanceof ServerPlayer)) {
            return;
        }

        final ServerPlayer player = (ServerPlayer) src;

        if (player.inventory().primary().freeCapacity() < 3) {
            throw super.serviceProvider.message().getError(src, "error.portal.tools");
        }

        final ItemStack barrier = ItemStack.builder()
                .itemType(ItemTypes.BARRIER)
                .add(Keys.CUSTOM_NAME,
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.barrier.title")
                                .asText()
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                )
                .add(Keys.LORE, Arrays.asList(
                        Component.empty(),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.barrier.lore.0")
                                .asText()
                                .decorate(TextDecoration.BOLD)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        Component.empty(),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.barrier.lore.1")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.barrier.lore.2")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                ))
                .build();

        final ItemStack debugStick = ItemStack.builder()
                .add(Keys.CUSTOM_NAME,
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.debug-stick.title")
                                .asText()
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                )
                .add(Keys.LORE, Arrays.asList(
                        Component.empty(),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.debug-stick.lore.0")
                                .asText()
                                .decorate(TextDecoration.BOLD)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        Component.empty(),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.debug-stick.lore.1")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.debug-stick.lore.2")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.debug-stick.lore.3")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.debug-stick.lore.4")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.debug-stick.lore.5")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                ))
                .itemType(ItemTypes.DEBUG_STICK)
                .build();

        final ItemStack flintAndSteel = ItemStack.builder()
                .add(Keys.CUSTOM_NAME,
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.flint-and-steel.title")
                                .asText()
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                )
                .add(Keys.LORE, Arrays.asList(
                        Component.empty(),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.flint-and-steel.lore.0")
                                .asText()
                                .decorate(TextDecoration.BOLD)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        Component.empty(),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.flint-and-steel.lore.1")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.flint-and-steel.lore.2")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                        super.serviceProvider.message()
                                .getMessage(src, "success.portal.tools.flint-and-steel.lore.3")
                                .asText()
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                ))
                .itemType(ItemTypes.FLINT_AND_STEEL)
                .build();

        player.inventory().primary().offer(barrier, debugStick, flintAndSteel);
    }

}
