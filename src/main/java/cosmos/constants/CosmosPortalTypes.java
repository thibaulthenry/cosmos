package cosmos.constants;

import cosmos.Cosmos;
import cosmos.registries.data.portal.CosmosPortalType;

import java.util.function.Supplier;

public class CosmosPortalTypes {

    public static final Supplier<CosmosPortalType> BUTTON = () -> Cosmos.services().registry().portalType().value(DataKeys.Portal.Type.BUTTON);
    public static final Supplier<CosmosPortalType> FRAME = () -> Cosmos.services().registry().portalType().value(DataKeys.Portal.Type.FRAME);
    public static final Supplier<CosmosPortalType> PRESSURE_PLATE = () -> Cosmos.services().registry().portalType().value(DataKeys.Portal.Type.PRESSURE_PLATE);
    public static final Supplier<CosmosPortalType> SIGN = () -> Cosmos.services().registry().portalType().value(DataKeys.Portal.Type.SIGN);

}
