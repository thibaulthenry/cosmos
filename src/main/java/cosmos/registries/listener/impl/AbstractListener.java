package cosmos.registries.listener.impl;

import cosmos.registries.listener.Listener;

public abstract class AbstractListener implements Listener {

    private boolean configurable;
    private boolean registered;

    public boolean isConfigurable() {
        return configurable;
    }

    public void setConfigurable(final boolean configurable) {
        this.configurable = configurable;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(final boolean registered) {
        this.registered = registered;
    }

}