package cosmos.services.listener;

import com.google.inject.ImplementedBy;
import cosmos.registries.listener.Listener;
import cosmos.services.CosmosService;
import cosmos.services.listener.impl.ListenerServiceImpl;

@ImplementedBy(ListenerServiceImpl.class)
public interface ListenerService extends CosmosService {

    void cancelSaveTaskIfNot();

    String format(Class<? extends Listener> listenerClass);

    void submitSaveTaskIfNot();

}
