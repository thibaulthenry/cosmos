package cosmos.services.data.selector;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.data.selector.impl.SelectorServiceImpl;

@ImplementedBy(SelectorServiceImpl.class)
public interface SelectorService extends CosmosService {

    void registerAll();

}
