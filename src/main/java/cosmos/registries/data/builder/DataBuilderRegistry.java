package cosmos.registries.data.builder;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.data.builder.impl.AdvancementProgressDataBuilder;
import cosmos.registries.data.builder.impl.AdvancementTreeDataBuilder;
import cosmos.registries.data.builder.impl.BackupArchetypeDataBuilder;
import cosmos.registries.data.builder.impl.CriterionProgressDataBuilder;
import cosmos.registries.data.builder.impl.DisplaySlotDataBuilder;
import cosmos.registries.data.builder.impl.ExperienceDataBuilder;
import cosmos.registries.data.builder.impl.ExtendedInventoryDataBuilder;
import cosmos.registries.data.builder.impl.HealthDataBuilder;
import cosmos.registries.data.builder.impl.HungerDataBuilder;
import cosmos.registries.data.builder.impl.InventoryDataBuilder;
import cosmos.registries.data.builder.impl.InventorySlotDataBuilder;
import cosmos.registries.data.builder.impl.ObjectiveDataBuilder;
import cosmos.registries.data.builder.impl.ScoreCriterionProgressDataBuilder;
import cosmos.registries.data.builder.impl.ScoreDataBuilder;
import cosmos.registries.data.builder.impl.ScoreboardDataBuilder;
import cosmos.registries.data.builder.impl.TeamDataBuilder;
import cosmos.registries.data.serializable.impl.AdvancementProgressData;
import cosmos.registries.data.serializable.impl.AdvancementTreeData;
import cosmos.registries.data.serializable.impl.BackupArchetypeData;
import cosmos.registries.data.serializable.impl.CriterionProgressData;
import cosmos.registries.data.serializable.impl.DisplaySlotData;
import cosmos.registries.data.serializable.impl.ExperienceData;
import cosmos.registries.data.serializable.impl.ExtendedInventoryData;
import cosmos.registries.data.serializable.impl.HealthData;
import cosmos.registries.data.serializable.impl.HungerData;
import cosmos.registries.data.serializable.impl.InventoryData;
import cosmos.registries.data.serializable.impl.InventorySlotData;
import cosmos.registries.data.serializable.impl.ObjectiveData;
import cosmos.registries.data.serializable.impl.ScoreCriterionProgressData;
import cosmos.registries.data.serializable.impl.ScoreData;
import cosmos.registries.data.serializable.impl.ScoreboardData;
import cosmos.registries.data.serializable.impl.TeamData;
import cosmos.registries.portal.CosmosButtonPortal;
import cosmos.registries.portal.CosmosFramePortal;
import cosmos.registries.portal.CosmosPressurePlatePortal;
import cosmos.registries.portal.CosmosSignPortal;
import cosmos.registries.portal.impl.CosmosButtonPortalBuilderImpl;
import cosmos.registries.portal.impl.CosmosFramePortalBuilderImpl;
import cosmos.registries.portal.impl.CosmosPressurePlatePortalBuilderImpl;
import cosmos.registries.portal.impl.CosmosSignPortalBuilderImpl;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.DataSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class DataBuilderRegistry implements CosmosRegistry<Class<? extends DataSerializable>, DataBuilder<? extends DataSerializable>> {

    private final Map<Class<? extends DataSerializable>, DataBuilder<? extends DataSerializable>> dataBuilderMap = new HashMap<>();

    @Inject
    public DataBuilderRegistry(final Injector injector) {
        this.dataBuilderMap.put(AdvancementProgressData.class, injector.getInstance(AdvancementProgressDataBuilder.class));
        this.dataBuilderMap.put(AdvancementTreeData.class, injector.getInstance(AdvancementTreeDataBuilder.class));
        this.dataBuilderMap.put(BackupArchetypeData.class, injector.getInstance(BackupArchetypeDataBuilder.class));
        this.dataBuilderMap.put(CosmosButtonPortal.class, injector.getInstance(CosmosButtonPortalBuilderImpl.class));
        this.dataBuilderMap.put(CosmosFramePortal.class, injector.getInstance(CosmosFramePortalBuilderImpl.class));
        this.dataBuilderMap.put(CosmosPressurePlatePortal.class, injector.getInstance(CosmosPressurePlatePortalBuilderImpl.class));
        this.dataBuilderMap.put(CosmosSignPortal.class, injector.getInstance(CosmosSignPortalBuilderImpl.class));
        this.dataBuilderMap.put(CriterionProgressData.class, injector.getInstance(CriterionProgressDataBuilder.class));
        this.dataBuilderMap.put(DisplaySlotData.class, injector.getInstance(DisplaySlotDataBuilder.class));
        this.dataBuilderMap.put(ExperienceData.class, injector.getInstance(ExperienceDataBuilder.class));
        this.dataBuilderMap.put(ExtendedInventoryData.class, injector.getInstance(ExtendedInventoryDataBuilder.class));
        this.dataBuilderMap.put(HealthData.class, injector.getInstance(HealthDataBuilder.class));
        this.dataBuilderMap.put(HungerData.class, injector.getInstance(HungerDataBuilder.class));
        this.dataBuilderMap.put(InventoryData.class, injector.getInstance(InventoryDataBuilder.class));
        this.dataBuilderMap.put(InventorySlotData.class, injector.getInstance(InventorySlotDataBuilder.class));
        this.dataBuilderMap.put(ObjectiveData.class, injector.getInstance(ObjectiveDataBuilder.class));
        this.dataBuilderMap.put(ScoreboardData.class, injector.getInstance(ScoreboardDataBuilder.class));
        this.dataBuilderMap.put(ScoreCriterionProgressData.class, injector.getInstance(ScoreCriterionProgressDataBuilder.class));
        this.dataBuilderMap.put(ScoreData.class, injector.getInstance(ScoreDataBuilder.class));
        this.dataBuilderMap.put(TeamData.class, injector.getInstance(TeamDataBuilder.class));
    }

    @SuppressWarnings("unchecked")
    public <T extends DataSerializable> void registerAll() {
        this.streamEntries().forEach(entry ->
                Sponge.dataManager().registerBuilder((Class<T>) entry.key(), (DataBuilder<T>) entry.value())
        );
    }

    @Override
    public Stream<CosmosRegistryEntry<Class<? extends DataSerializable>, DataBuilder<? extends DataSerializable>>> streamEntries() {
        return this.dataBuilderMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public DataBuilder<? extends DataSerializable> value(final Class<? extends DataSerializable> key) {
        return this.dataBuilderMap.get(key);
    }

}
