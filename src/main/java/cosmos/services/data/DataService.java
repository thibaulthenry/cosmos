package cosmos.services.data;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.data.impl.DataServiceImpl;
import org.spongepowered.api.data.persistence.DataSerializable;

@ImplementedBy(DataServiceImpl.class)
public interface DataService extends CosmosService {

    <T extends DataSerializable> void registerBuilders();

    void registerPortalTypes();

    void registerSelectors();

}
