package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.backup.Delete;
import cosmos.executors.commands.backup.List;
import cosmos.executors.commands.backup.Reset;
import cosmos.executors.commands.backup.Restore;
import cosmos.executors.commands.backup.Save;
import cosmos.executors.commands.backup.Tag;

@Singleton
class Backup extends AbstractModule {

    @Inject
    Backup(final Injector injector) {
        super(
                injector.getInstance(Delete.class),
                injector.getInstance(List.class),
                injector.getInstance(Reset.class),
                injector.getInstance(Restore.class),
                injector.getInstance(Save.class),
                injector.getInstance(Tag.class)
        );
    }

}
