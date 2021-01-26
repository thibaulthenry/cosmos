package cosmos.services.perworld;

import com.google.inject.ImplementedBy;
import cosmos.services.perworld.impl.ExperiencesServiceImpl;

@ImplementedBy(ExperiencesServiceImpl.class)
public interface ExperiencesService extends PlayerRelatedPerWorldService {
}
