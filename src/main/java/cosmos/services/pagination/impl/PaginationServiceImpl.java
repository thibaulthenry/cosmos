package cosmos.services.pagination.impl;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.services.message.MessageService;
import cosmos.services.pagination.PaginationService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.service.pagination.PaginationList;

@Singleton
public class PaginationServiceImpl implements PaginationService {

    private final MessageService messageService;

    @Inject
    public PaginationServiceImpl(final Injector injector) {
        this.messageService = injector.getInstance(MessageService.class);
    }

    @Override
    public PaginationList generate(final Component title, final Iterable<Component> contents) {
        return PaginationList.builder().title(title).contents(contents).linesPerPage(10).build();
    }

    @Override
    public void send(final Audience src, final Component title, final Iterable<Component> contents, final boolean flattenSingle) throws CommandException {
        if (Iterables.isEmpty(contents)) {
            throw this.messageService.getError(src, "error.empty-output");
        }

        if (flattenSingle && Iterables.size(contents) == 1) {
            contents.forEach(src::sendMessage);
        } else {
            this.generate(title, contents).sendTo(src);
        }
    }

    @Override
    public void send(final Audience src, final PaginationList pagination, final boolean flattenSingle) throws CommandException {
        this.send(src, pagination.title().orElse(Component.empty()), pagination.contents(), flattenSingle);
    }

}
