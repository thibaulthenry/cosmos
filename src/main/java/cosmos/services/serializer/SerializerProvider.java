package cosmos.services.serializer;

import com.google.inject.ImplementedBy;
import cosmos.registries.serializer.impl.AdvancementsSerializer;
import cosmos.registries.serializer.impl.ExperiencesSerializer;
import cosmos.registries.serializer.impl.HealthsSerializer;
import cosmos.registries.serializer.impl.HungersSerializer;
import cosmos.registries.serializer.impl.InventoriesSerializer;
import cosmos.registries.serializer.impl.ScoreboardsSerializer;
import cosmos.services.CosmosService;
import cosmos.services.serializer.impl.SerializerProviderImpl;

@ImplementedBy(SerializerProviderImpl.class)
public interface SerializerProvider extends CosmosService {

    AdvancementsSerializer advancements();

    ExperiencesSerializer experiences();

    HealthsSerializer healths();

    HungersSerializer hungers();

    InventoriesSerializer inventories();

    ScoreboardsSerializer scoreboards();

}
