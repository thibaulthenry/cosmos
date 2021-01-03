package cosmos.services.pagination;

import com.google.inject.ImplementedBy;
import cosmos.services.CosmosService;
import cosmos.services.pagination.impl.PaginationServiceImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.service.pagination.PaginationList;

@ImplementedBy(PaginationServiceImpl.class)
public interface PaginationService extends CosmosService {

    PaginationList generate(Component title, Iterable<Component> contents);

    void send(Audience src, Component title, Iterable<Component> contents, boolean flattenSingle) throws CommandException;

    void send(Audience src, PaginationList pagination, boolean flattenSingle) throws CommandException;
}
