package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.perworld.impl.HealthsServiceImpl;

@ImplementedBy(HealthsServiceImpl.class)
public interface HealthsService extends PlayerRelatedPerWorldService {
}
