package cosmos.listeners;

public abstract class AbstractListener {

    private boolean registered;

    boolean isRegistered() {
        return registered;
    }

    void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
