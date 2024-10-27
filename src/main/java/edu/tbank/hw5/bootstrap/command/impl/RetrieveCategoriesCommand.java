package edu.tbank.hw5.bootstrap.command.impl;

import edu.tbank.hw5.bootstrap.command.Command;
import edu.tbank.hw5.client.KudaGoClient;
import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.observer.DataObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RetrieveCategoriesCommand implements Command {
    private final KudaGoClient kudaGoClient;
    private final List<DataObserver<Category>> observers;

    @Override
    public void execute() {
        List<Category> categories = kudaGoClient.getAllCategories();
        if (categories != null) {
            log.info("Fetched {} categories from API", categories.size());
            observers.forEach(observer -> observer.update(categories));
        } else {
            log.warn("No categories fetched from API");
        }
    }
}
