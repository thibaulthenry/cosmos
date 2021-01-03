package cosmos.registries.data.builder;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.data.builder.impl.*;
import cosmos.registries.data.serializable.impl.*;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.DataSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class DataBuilderRegistry implements CosmosRegistry<Class<? extends DataSerializable>, DataBuilder<? extends DataSerializable>> {

    private final Map<Class<? extends DataSerializable>, DataBuilder<? extends DataSerializable>> dataBuilderMap = new HashMap<>();

    @Inject
    public DataBuilderRegistry(final Injector injector) {
        this.dataBuilderMap.put(AdvancementProgressData.class, injector.getInstance(AdvancementProgressDataBuilder.class));
        this.dataBuilderMap.put(AdvancementTreeData.class, injector.getInstance(AdvancementTreeDataBuilder.class));
        this.dataBuilderMap.put(CriterionProgressData.class, injector.getInstance(CriterionProgressDataBuilder.class));
        this.dataBuilderMap.put(DisplaySlotData.class, injector.getInstance(DisplaySlotDataBuilder.class));
        this.dataBuilderMap.put(ExtendedInventoryData.class, injector.getInstance(ExtendedInventoryDataBuilder.class));
        this.dataBuilderMap.put(InventoryData.class, injector.getInstance(InventoryDataBuilder.class));
        this.dataBuilderMap.put(InventorySlotData.class, injector.getInstance(InventorySlotDataBuilder.class));
        this.dataBuilderMap.put(ObjectiveData.class, injector.getInstance(ObjectiveDataBuilder.class));
        this.dataBuilderMap.put(ScoreboardData.class, injector.getInstance(ScoreboardDataBuilder.class));
        this.dataBuilderMap.put(ScoreCriterionProgressData.class, injector.getInstance(ScoreCriterionProgressDataBuilder.class));
        this.dataBuilderMap.put(ScoreData.class, injector.getInstance(ScoreDataBuilder.class));
        this.dataBuilderMap.put(TeamData.class, injector.getInstance(TeamDataBuilder.class));
    }

    public Set<Map.Entry<Class<? extends DataSerializable>, DataBuilder<? extends DataSerializable>>> entries() {
        return this.dataBuilderMap.entrySet();
    }

    @Override
    public DataBuilder<? extends DataSerializable> get(final Class<? extends DataSerializable> key) {
        return this.dataBuilderMap.get(key);
    }

    @Override
    public boolean has(final Class<? extends DataSerializable> key) {
        return this.dataBuilderMap.containsKey(key);
    }
}
