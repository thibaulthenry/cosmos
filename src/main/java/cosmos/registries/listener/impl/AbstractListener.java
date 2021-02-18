package cosmos.registries.listener.impl;

import cosmos.registries.listener.Listener;

public abstract class AbstractListener implements Listener {

    private boolean configurable;
    private boolean registered;

    public boolean configurable() {
        return configurable;
    }

    public void configurable(boolean configurable) {
        this.configurable = configurable;
    }

    public boolean registeredToSponge() {
        return this.registered;
    }

    public void registeredToSponge(boolean registered) {
        this.registered = registered;
    }

}