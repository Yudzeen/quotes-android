package ebj.yujinkun.quotes.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.model.Quotes;
import ebj.yujinkun.quotes.model.Result;
import ebj.yujinkun.quotes.repository.db.QuoteDao;
import ebj.yujinkun.quotes.repository.remote.RemoteDatabase;

public class QuoteRepositoryImpl implements QuoteRepository {

    private static final String TAG = QuoteRepositoryImpl.class.getSimpleName();

    private static QuoteRepositoryImpl instance;

    private QuoteDao quoteDao;
    private RemoteDatabase remoteDatabase;
    private MutableLiveData<Result<List<Quote>>> quotesLiveData = new MutableLiveData<>();

    private QuoteRepositoryImpl(RemoteDatabase remoteDatabase, QuoteDao quoteDao) {
        this.quoteDao = quoteDao;
        this.remoteDatabase = remoteDatabase;
        sync();
    }

    public static QuoteRepositoryImpl getInstance(RemoteDatabase remoteDatabase, QuoteDao quoteDao) {
        if (instance == null) {
            synchronized (QuoteRepositoryImpl.class) {
                if (instance == null) {
                    instance = new QuoteRepositoryImpl(remoteDatabase, quoteDao);
                }
            }
        }
        return instance;
    }

    @Override
    public LiveData<Result<List<Quote>>> getAllQuotes() {
        Log.d(TAG, "getAllQuotes");
        return quotesLiveData;
    }

    @Override
    public void insert(final Quote quote) {
        Log.d(TAG, "insert: " + quote);
        Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                .setEvent(Result.Event.INSERT)
                .setStatus(Result.Status.IN_PROGRESS)
                .build();
        quotesLiveData.postValue(event);
        remoteDatabase.insert(quote, new RemoteDatabase.InsertCallback() {
            @Override
            public void onSuccess(final Quote response) {
                sync();
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Insert failed.");
                Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                        .setEvent(Result.Event.INSERT)
                        .setStatus(Result.Status.ERROR)
                        .build();
                quotesLiveData.postValue(event);
            }
        });
    }

    @Override
    public void update(final Quote quote) {
        Log.d(TAG, "update: " + quote);
        Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                .setEvent(Result.Event.UPDATE)
                .setStatus(Result.Status.IN_PROGRESS)
                .build();
        quotesLiveData.postValue(event);
        remoteDatabase.update(quote, new RemoteDatabase.UpdateCallback() {
            @Override
            public void onSuccess(final Quote response) {
                sync();
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Update failed.");
                Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                        .setEvent(Result.Event.UPDATE)
                        .setStatus(Result.Status.ERROR)
                        .build();
                quotesLiveData.postValue(event);
            }
        });

    }

    @Override
    public void delete(final Quote quote) {
        Log.d(TAG, "delete: " + quote);
        Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                .setEvent(Result.Event.DELETE)
                .setStatus(Result.Status.IN_PROGRESS)
                .build();
        quotesLiveData.postValue(event);
        remoteDatabase.delete(quote, new RemoteDatabase.DeleteCallback() {
            @Override
            public void onSuccess(final Quote response) {
                sync();
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Delete failed.");
                Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                        .setEvent(Result.Event.DELETE)
                        .setStatus(Result.Status.ERROR)
                        .build();
                quotesLiveData.postValue(event);
            }
        });
    }

    @Override
    public void sync() {
        Log.d(TAG, "Syncing.");
        Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                .setEvent(Result.Event.GET_ALL)
                .setStatus(Result.Status.IN_PROGRESS)
                .build();
        quotesLiveData.postValue(event);

        remoteDatabase.getAllQuotes(new RemoteDatabase.GetAllQuotesCallback() {
            @Override
            public void onSuccess(final List<Quote> quotes) {
                List<Quote> sortedQuotes = Quotes.sortByLatestDateModified(quotes);
                Log.d(TAG, "Quotes: " + sortedQuotes);

                Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                        .setStatus(Result.Status.SUCCESS)
                        .setEvent(Result.Event.GET_ALL)
                        .setResource(sortedQuotes)
                        .build();
                quotesLiveData.postValue(event);
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Error retrieving quotes.");
                Result<List<Quote>> event = new Result.Builder<List<Quote>>()
                        .setStatus(Result.Status.ERROR)
                        .setEvent(Result.Event.GET_ALL)
                        .build();
                quotesLiveData.postValue(event);
            }
        });
    }

}
