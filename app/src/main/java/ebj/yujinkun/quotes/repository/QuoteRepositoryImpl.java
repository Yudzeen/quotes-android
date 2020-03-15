package ebj.yujinkun.quotes.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.repository.db.AppRoomDatabase;
import ebj.yujinkun.quotes.repository.db.QuoteDao;

public class QuoteRepositoryImpl implements QuoteRepository {

    private static final String TAG = QuoteRepositoryImpl.class.getSimpleName();

    private QuoteDao quoteDao;

    public QuoteRepositoryImpl(QuoteDao quoteDao) {
        this.quoteDao = quoteDao;
    }

    @Override
    public LiveData<List<Quote>> getAllQuotes() {
        Log.d(TAG, "getAllQuotes");
        return quoteDao.getAllQuotes();
    }

    @Override
    public void insert(final Quote quote) {
        Log.d(TAG, "insert: " + quote);
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                quoteDao.insert(quote);
            }
        });
    }

    @Override
    public void update(final Quote quote) {
        Log.d(TAG, "update: " + quote);
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                quoteDao.update(quote);
            }
        });
    }

    @Override
    public void delete(final Quote quote) {
        Log.d(TAG, "delete: " + quote);
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                quoteDao.delete(quote);
            }
        });
    }
}
