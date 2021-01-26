package cosmos.registries.listener;

public interface Listener {

    boolean isConfigurable();

    void setConfigurable(boolean configurable);

    boolean isRegistered();

    void setRegistered(boolean registered);

}
