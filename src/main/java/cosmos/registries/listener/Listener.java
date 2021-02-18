package cosmos.registries.listener;

public interface Listener {

    boolean configurable();

    void configurable(boolean configurable);

    boolean registeredToSponge();

    void registeredToSponge(boolean registered);

}
