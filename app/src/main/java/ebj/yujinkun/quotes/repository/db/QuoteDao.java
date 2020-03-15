package ebj.yujinkun.quotes.repository.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ebj.yujinkun.quotes.model.Quote;

@Dao
public interface QuoteDao {

    @Query("SELECT * from quote ORDER BY date_modified DESC")
    LiveData<List<Quote>> getAllQuotes();

    @Insert
    void insert(Quote quote);

    @Update
    void update(Quote quote);

    @Delete
    void delete(Quote quote);

}
