package cosmos.services.validation;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.validation.impl.ValidationServiceImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.util.Identifiable;

@ImplementedBy(ValidationServiceImpl.class)
public interface ValidationService extends CosmosService {

    boolean doesOverflowMaxLength(String sequence, int maxLength);

    boolean doesOverflowMaxLength(Component text, int maxLength);

    boolean isSelf(Audience src, Component target);

    boolean isSelf(Audience src, Identifiable target);

    boolean isSelf(Identifiable src, Identifiable target);

}
