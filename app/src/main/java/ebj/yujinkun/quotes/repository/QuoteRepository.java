package ebj.yujinkun.quotes.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ebj.yujinkun.quotes.model.Quote;

public interface QuoteRepository {

    LiveData<List<Quote>> getAllQuotes();
    void insert(Quote quote);
    void update(Quote quote);
    void delete(Quote quote);

}
