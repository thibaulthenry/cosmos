package cosmos.registries.listener.impl.perworld;

import cosmos.registries.listener.ToggleListener;
import cosmos.registries.listener.impl.AbstractListener;

public abstract class AbstractPerWorldListener extends AbstractListener implements ToggleListener {

    protected AbstractPerWorldListener() {
        super.setConfigurable(true);
    }

}
