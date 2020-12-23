package cosmos.services.world;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.world.impl.WorldServiceImpl;

@ImplementedBy(WorldServiceImpl.class)
public interface WorldService extends CosmosService {
}
