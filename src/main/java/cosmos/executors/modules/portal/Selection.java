package cosmos.executors.modules.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.portal.selection.Clear;
import cosmos.executors.commands.portal.selection.Highlight;
import cosmos.executors.commands.portal.selection.List;
import cosmos.executors.modules.AbstractModule;

@Singleton
public class Selection extends AbstractModule {

    @Inject
    Selection(final Injector injector) {
        super(
                injector.getInstance(Clear.class),
                injector.getInstance(Highlight.class),
                injector.getInstance(List.class)
        );
    }

}
