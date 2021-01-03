package cosmos.registries.listener.impl;

import cosmos.registries.listener.Listener;

public abstract class AbstractListener implements Listener {

    private boolean registered;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}