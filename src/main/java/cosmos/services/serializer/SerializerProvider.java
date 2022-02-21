package cosmos.services.serializer;

import com.google.inject.ImplementedBy;
import cosmos.registries.serializer.impl.AdvancementSerializer;
import cosmos.registries.serializer.impl.ExperienceSerializer;
import cosmos.registries.serializer.impl.HealthSerializer;
import cosmos.registries.serializer.impl.HungerSerializer;
import cosmos.registries.serializer.impl.InventorySerializer;
import cosmos.registries.serializer.impl.PortalSerializer;
import cosmos.registries.serializer.impl.ScoreboardSerializer;
import cosmos.services.CosmosService;
import cosmos.services.serializer.impl.SerializerProviderImpl;

@ImplementedBy(SerializerProviderImpl.class)
public interface SerializerProvider extends CosmosService {

    AdvancementSerializer advancement();

    ExperienceSerializer experience();

    HealthSerializer health();

    HungerSerializer hunger();

    InventorySerializer inventory();

    PortalSerializer portal();

    ScoreboardSerializer scoreboard();

}
