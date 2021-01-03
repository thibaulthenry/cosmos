package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.perworld.impl.PerWorldProviderImpl;

@ImplementedBy(PerWorldProviderImpl.class)
public interface PerWorldProvider extends CosmosService {

    AdvancementsService advancements();

    ExperiencesService experiences();

    HealthsService healths();

    HungersService hungers();

    InventoriesService inventories();

    ScoreboardsService scoreboards();

}
