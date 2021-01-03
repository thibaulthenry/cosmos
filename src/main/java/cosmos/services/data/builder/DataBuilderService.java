package cosmos.services.data.builder;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.data.builder.impl.DataBuilderServiceImpl;
import org.spongepowered.api.data.persistence.DataSerializable;

@ImplementedBy(DataBuilderServiceImpl.class)
public interface DataBuilderService extends CosmosService {

    <T extends DataSerializable> void registerAll();
}
