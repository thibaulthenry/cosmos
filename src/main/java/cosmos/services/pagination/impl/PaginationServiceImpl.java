package cosmos.services.pagination.impl;

import com.google.common.collect.Iterables;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.services.pagination.PaginationService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.service.pagination.PaginationList;


@Singleton
public class PaginationServiceImpl implements PaginationService {

    @Override
    public PaginationList generatePagination(final Component title, final Iterable<Component> contents) {
        return PaginationList.builder().title(title).contents(contents).linesPerPage(10).build();
    }

    @Override
    public void sendPagination(final Audience src, final Component title, final Iterable<Component> contents, final boolean flattenSingle) throws CommandException {
        if (Iterables.isEmpty(contents)) {
            throw Cosmos.getServices().message().getMessage(src, "error.empty-output").asException();
        }

        if (flattenSingle && Iterables.size(contents) == 1) {
            contents.forEach(src::sendMessage);
        } else {
            this.generatePagination(title, contents).sendTo(src);
        }
    }

    @Override
    public void sendPagination(final Audience src, final PaginationList pagination, final boolean flattenSingle) throws CommandException {
        this.sendPagination(src, pagination.getTitle().orElse(Component.empty()), pagination.getContents(), flattenSingle);
    }
}
