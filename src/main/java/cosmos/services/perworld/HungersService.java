package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.perworld.impl.HungersServiceImpl;

@ImplementedBy(HungersServiceImpl.class)
public interface HungersService extends PlayerRelatedPerWorldService {


}
