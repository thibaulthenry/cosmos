package cosmos.services.data.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.services.data.DataProvider;
import cosmos.services.data.builder.DataBuilderService;
import cosmos.services.data.portal.PortalService;
import cosmos.services.data.selector.SelectorService;

@Singleton
public class DataProviderImpl implements DataProvider {

    private final DataBuilderService dataBuilderService;
    //private final PortalService portalService;
    private final SelectorService selectorService;

    @Inject
    public DataProviderImpl(final Injector injector) {
        this.dataBuilderService = injector.getInstance(DataBuilderService.class);
        //this.portalService = injector.getInstance(PortalService.class);
        this.selectorService = injector.getInstance(SelectorService.class);
    }

    @Override
    public DataBuilderService dataBuilder() {
        return this.dataBuilderService;
    }

//    @Override
//    public PortalService portal() {
//        return this.portalService;
//    }

    @Override
    public SelectorService selector() {
        return this.selectorService;
    }

}
