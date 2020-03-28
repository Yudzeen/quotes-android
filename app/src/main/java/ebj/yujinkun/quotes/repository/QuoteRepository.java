package ebj.yujinkun.quotes.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.model.Result;

public interface QuoteRepository {

    LiveData<Result<List<Quote>>> getAllQuotes();
    void insert(Quote quote);
    void update(Quote quote);
    void delete(Quote quote);
    void sync();

}
