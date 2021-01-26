package cosmos.services.data;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.data.builder.DataBuilderService;
import cosmos.services.data.impl.DataProviderImpl;
import cosmos.services.data.portal.PortalService;
import cosmos.services.data.selector.SelectorService;
import cosmos.services.perworld.AdvancementsService;
import cosmos.services.perworld.ExperiencesService;
import cosmos.services.perworld.HealthsService;
import cosmos.services.perworld.HungersService;
import cosmos.services.perworld.InventoriesService;
import cosmos.services.perworld.ScoreboardsService;
import cosmos.services.perworld.impl.PerWorldProviderImpl;

@ImplementedBy(DataProviderImpl.class)
public interface DataProvider extends CosmosService {

    DataBuilderService dataBuilder();

    //PortalService portal();

    SelectorService selector();

}
