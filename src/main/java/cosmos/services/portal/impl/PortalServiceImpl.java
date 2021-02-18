package cosmos.services.portal.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.CosmosPortalTypes;
import cosmos.constants.DelayFormat;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.data.portal.CosmosPortalType;
import cosmos.registries.portal.CosmosPortal;
import cosmos.registries.portal.PortalDispatcherRegistry;
import cosmos.registries.portal.PortalRegistry;
import cosmos.registries.portal.PortalSelectionRegistry;
import cosmos.registries.portal.impl.PortalDispatcher;
import cosmos.services.message.MessageService;
import cosmos.services.portal.PortalService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.FallingBlock;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.portal.PortalType;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.*;

@Singleton
public class PortalServiceImpl implements PortalService {

    private final MessageService messageService;
    private final PortalDispatcherRegistry portalDispatcherRegistry;
    private final PortalRegistry portalRegistry;
    private final PortalSelectionRegistry portalSelectionRegistry;

    @Inject
    public PortalServiceImpl(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
        this.portalDispatcherRegistry = injector.getInstance(PortalDispatcherRegistry.class);
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.portalSelectionRegistry = injector.getInstance(PortalSelectionRegistry.class);
    }

    @Override
    public CosmosPortal create(final Audience src, final ResourceKey key, final CosmosPortalType type) throws CommandException {
        if (this.portalRegistry.has(key)) {
            throw this.messageService.getError(src, "error.portal.create.already-existing", "portal", key);
        }

        if (!(src instanceof Identifiable)) {
            throw this.messageService.getError(src, "error.missing.portal.selection.any");
        }

        final UUID uuid = ((Identifiable) src).uniqueId();
        final Optional<Set<ServerLocation>> optionalSelection = this.portalSelectionRegistry.find(uuid);

        if (!optionalSelection.isPresent() || optionalSelection.get().isEmpty()) {
            throw this.messageService.getMessage(src, "error.missing.portal.selection")
                    .clickEvent("tools", ClickEvent.suggestCommand("/cm portal tools"))
                    .hoverEvent("tools", HoverEvent.showText(this.messageService.getText(src, "error.missing.portal.selection.hover")))
                    .asError();
        }

        final Set<ServerLocation> selection = optionalSelection.get();

        final CosmosPortal portal = CosmosPortal.builder(type.portalBuilderClass())
                .delayFormat(DelayFormat.SECONDS)
                .delayShown(true)
                .key(key)
                .origins(selection)
                .particlesSpawnInterval(Ticks.single())
                .soundDelayInterval(Ticks.single())
                .trigger(type.defaultTrigger())
                .build();

        this.fill(src, portal);
        this.portalRegistry.register(key, portal);
        this.portalSelectionRegistry.unregister(uuid);

        return portal;
    }

    @Override
    public void delete(final Audience src, final ResourceKey key) throws CommandException {
        if (!this.portalRegistry.has(key)) {
            throw this.messageService.getError(src, "error.missing.portal", "portal", key);
        }

        this.portalRegistry.unregister(key)
                .orElseThrow(this.messageService.supplyError(src, "error.portal.delete", "portal", key));
    }

    @Override
    public void fill(final Audience src, final CosmosPortal portal) throws CommandException {
        this.fill(src, portal, BlockState.builder().blockType(portal.trigger()).build());
    }

    @Override
    public void fill(final Audience src, final CosmosPortal portal, final BlockState blockState) throws CommandException {
        final BlockType blockType = blockState.type();

        if (!portal.type().isAnyOfTriggers(blockType)) {
            throw this.messageService.getMessage(src, "error.invalid.portal.trigger")
                    .replace("block", blockType.key(RegistryTypes.BLOCK_TYPE))
                    .replace("type", portal.type().key(RegistryTypes.PORTAL_TYPE))
                    .asError();
        }

        Sponge.server().causeStackManager().pushCause(Cosmos.pluginContainer());
        portal.origins().forEach(location -> location.setBlock(blockState));
    }

    @Override
    public void fill(final Audience src, final CosmosPortal portal, final BlockEntity blockEntity) throws CommandException {
        this.fill(src, portal, blockEntity.block());

        portal.origins()
                .stream()
                .filter(location -> CosmosPortalTypes.SIGN.get().equals(portal.type()))
                .forEach(location -> blockEntity.get(Keys.SIGN_LINES).ifPresent(value -> location.tryOffer(Keys.SIGN_LINES, value)));
    }

    // TODO https://github.com/SpongePowered/SpongeAPI/pull/2266
    @Override
    public void highlight(final BlockType blockType, final Ticks duration, final String tag, final Collection<? extends ServerLocation> locations) {
        locations.forEach(location -> {
            final FallingBlock fallingBlock = location.createEntity(EntityTypes.FALLING_BLOCK.get());
            fallingBlock.offer(Keys.BLOCK_STATE, BlockState.builder().blockType(blockType).build());
            fallingBlock.offer(Keys.CAN_DROP_AS_ITEM, false);
            fallingBlock.offer(Keys.CAN_PLACE_AS_BLOCK, false);
            fallingBlock.offer(Keys.FALL_TIME, Ticks.of(Math.max(0L, 600 - duration.ticks())));
            fallingBlock.offer(Keys.INVULNERABLE, true);
            fallingBlock.offer(Keys.IS_GLOWING, true);
            fallingBlock.offer(Keys.IS_GRAVITY_AFFECTED, false);
            fallingBlock.offer(Keys.ON_GROUND, false);
            location.spawnEntity(fallingBlock);
        });
    }

    @Override
    public void highlight(final BlockType blockType, final Ticks duration, final String tag, final ServerLocation... locations) {
        this.highlight(blockType, duration, tag, Arrays.asList(locations));
    }

    @Override
    public void link(final Audience src, final ResourceKey worldOriginKey, final PortalType portalType, final ResourceKey worldDestinationKey) throws CommandException {
        if (worldOriginKey.equals(worldDestinationKey)) {
            throw this.messageService.getError(src, "error.portal.link.same");
        }

        final Optional<PortalDispatcher> optionalPortalDispatcher = this.portalDispatcherRegistry.find(worldOriginKey)
                .map(Optional::of)
                .orElse(this.portalDispatcherRegistry.register(worldOriginKey, new PortalDispatcher()).map(CosmosRegistryEntry::value));

        if (!optionalPortalDispatcher.isPresent()) {
            throw this.messageService.getMessage(src, "error.portal.link")
                    .replace("destination", worldDestinationKey)
                    .replace("origin", worldOriginKey)
                    .replace("type", portalType.key(RegistryTypes.PORTAL_TYPE))
                    .asError();
        }

        final PortalDispatcher portalDispatcher = optionalPortalDispatcher.get();

        if (portalDispatcher.findLink(portalType).map(worldDestinationKey::equals).orElse(false)) {
            throw this.messageService.getMessage(src, "error.portal.link.already-linked")
                    .replace("destination", worldDestinationKey)
                    .replace("origin", worldOriginKey)
                    .replace("type", portalType.key(RegistryTypes.PORTAL_TYPE))
                    .asError();
        }

        if (!portalDispatcher.addLink(portalType, worldDestinationKey)) {
            throw this.messageService.getMessage(src, "error.portal.link")
                    .replace("destination", worldDestinationKey)
                    .replace("origin", worldOriginKey)
                    .replace("type", portalType.key(RegistryTypes.PORTAL_TYPE))
                    .asError();
        }
    }

    @Override
    public void unlink(final Audience src, final ResourceKey worldOriginKey, final PortalType portalType) throws CommandException {
        final Optional<PortalDispatcher> optionalPortalDispatcher = this.portalDispatcherRegistry.find(worldOriginKey);

        if (!optionalPortalDispatcher.isPresent()) {
            throw this.messageService.getMessage(src, "error.missing.portal.link")
                    .replace("origin", worldOriginKey)
                    .replace("type", portalType.key(RegistryTypes.PORTAL_TYPE))
                    .asError();
        }

        if (!optionalPortalDispatcher.get().removeLink(portalType)) {
            throw this.messageService.getMessage(src, "error.portal.unlink")
                    .replace("origin", worldOriginKey)
                    .replace("type", portalType.key(RegistryTypes.PORTAL_TYPE))
                    .asError();
        }
    }

}
