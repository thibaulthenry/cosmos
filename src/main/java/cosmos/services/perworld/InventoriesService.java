package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.perworld.impl.InventoriesServiceImpl;

@ImplementedBy(InventoriesServiceImpl.class)
public interface InventoriesService extends PlayerRelatedPerWorldService {
}
