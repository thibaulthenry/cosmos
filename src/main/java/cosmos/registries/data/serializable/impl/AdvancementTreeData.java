package cosmos.registries.data.serializable.impl;

import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.advancement.Advancement;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancementTreeData implements ShareableSerializable<ServerPlayer> {

    private final List<AdvancementProgressData> advancementProgressesData;

    public AdvancementTreeData(final ServerPlayer player) {
        this.advancementProgressesData = player.unlockedAdvancementTrees()
                .stream()
                .map(advancementTree -> this.extractChildren(advancementTree.rootAdvancement()))
                .flatMap(Collection::stream)
                .map(player::progress)
                .map(AdvancementProgressData::new)
                .filter(AdvancementProgressData::isAdvancementStarted)
                .collect(Collectors.toList());
    }

    public AdvancementTreeData(final List<AdvancementProgressData> advancementProgressesData) {
        this.advancementProgressesData = advancementProgressesData;
    }
    
    public AdvancementTreeData() {
        this.advancementProgressesData = Collections.emptyList();
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    public Collection<Advancement> extractChildren(final Advancement rootAdvancement) {
        final Collection<Advancement> children = rootAdvancement.children();

        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }

        return Stream.concat(
                children.stream(),
                children.stream().map(this::extractChildren).flatMap(Collection::stream)
        ).collect(Collectors.toList());
    }

    @Override
    public void share(final ServerPlayer data) {
        // TODO https://github.com/SpongePowered/SpongeAPI/issues/2299
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer dataContainer = DataContainer.createNew(DataView.SafetyMode.NO_DATA_CLONED);
        this.advancementProgressesData.forEach(data -> dataContainer.set(DataQuery.of(data.key()), data));

        return dataContainer;
    }

}
