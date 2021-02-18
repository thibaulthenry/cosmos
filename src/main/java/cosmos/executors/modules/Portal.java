package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.portal.Create;
import cosmos.executors.commands.portal.Delete;
import cosmos.executors.commands.portal.Highlight;
import cosmos.executors.commands.portal.Information;
import cosmos.executors.commands.portal.Link;
import cosmos.executors.commands.portal.List;
import cosmos.executors.commands.portal.Tools;
import cosmos.executors.commands.portal.Unselect;
import cosmos.executors.modules.portal.Modify;

@Singleton
class Portal extends AbstractModule {

    @Inject
    Portal(final Injector injector) {
        super(
                injector.getInstance(Create.class),
                injector.getInstance(Delete.class),
                injector.getInstance(Highlight.class),
                injector.getInstance(Information.class),
                injector.getInstance(Link.class),
                injector.getInstance(List.class),
                injector.getInstance(Modify.class),
                injector.getInstance(Tools.class),
                injector.getInstance(Unselect.class)
        );
    }

}
