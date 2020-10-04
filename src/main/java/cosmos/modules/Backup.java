package cosmos.modules;

import cosmos.commands.backup.Delete;
import cosmos.commands.backup.List;
import cosmos.commands.backup.Reset;
import cosmos.commands.backup.Restore;
import cosmos.commands.backup.Save;
import cosmos.commands.backup.Tag;

class Backup extends AbstractModule {

    Backup() {
        super("Backup module commands");

        addChild(new Delete());
        addChild(new List());
        addChild(new Reset());
        addChild(new Restore());
        addChild(new Save());
        addChild(new Tag());
    }
}
