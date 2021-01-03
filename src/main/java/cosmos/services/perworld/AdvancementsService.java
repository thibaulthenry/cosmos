package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.perworld.impl.AdvancementsServiceImpl;

@ImplementedBy(AdvancementsServiceImpl.class)
public interface AdvancementsService extends PlayerRelatedPerWorldService {

}
