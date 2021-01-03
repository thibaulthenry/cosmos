package cosmos.registries.data.serializable.impl;

import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.advancement.Advancement;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancementTreeData implements DataSerializable, ShareableSerializable<ServerPlayer> {

    private final List<AdvancementProgressData> advancementProgressesData;

    public AdvancementTreeData(final ServerPlayer player) {
        this.advancementProgressesData = player.getUnlockedAdvancementTrees()
                .stream()
                .map(advancementTree -> this.getRecursiveChildren(advancementTree.getRootAdvancement()))
                .flatMap(Collection::stream)
                .map(player::getProgress)
                .map(AdvancementProgressData::new)
                .filter(AdvancementProgressData::isAdvancementStarted)
                .collect(Collectors.toList());
    }

    public AdvancementTreeData(final List<AdvancementProgressData> advancementProgressesData) {
        this.advancementProgressesData = advancementProgressesData;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public Collection<Advancement> getRecursiveChildren(final Advancement rootAdvancement) {
        final Collection<Advancement> children = rootAdvancement.getChildren();

        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }

        return Stream.concat(
                children.stream(),
                children.stream().map(this::getRecursiveChildren).flatMap(Collection::stream)
        ).collect(Collectors.toList());
    }

    @Override
    public void offer(final ServerPlayer data) {
        // TODO Retrieve advancements via ResourceKey
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer dataContainer = DataContainer.createNew(DataView.SafetyMode.NO_DATA_CLONED);

        this.advancementProgressesData.forEach(data -> dataContainer.set(DataQuery.of(data.getKey()), data));

        return dataContainer;
    }

}
