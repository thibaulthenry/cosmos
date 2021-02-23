package cosmos.constants;

import cosmos.Cosmos;
import cosmos.registries.data.portal.CosmosPortalType;

import java.util.function.Supplier;

public class CosmosPortalTypes {

    public static final Supplier<CosmosPortalType> BUTTON = () -> Cosmos.getRegistries().portalType().value(DataKeys.PORTAL_TYPE_BUTTON);
    public static final Supplier<CosmosPortalType> FRAME = () -> Cosmos.getRegistries().portalType().value(DataKeys.PORTAL_TYPE_FRAME);
    public static final Supplier<CosmosPortalType> PRESSURE_PLATE = () -> Cosmos.getRegistries().portalType().value(DataKeys.PORTAL_TYPE_PRESSURE_PLATE);

}
