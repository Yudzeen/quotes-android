package ebj.yujinkun.quotes;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.model.Result;
import ebj.yujinkun.quotes.repository.QuoteRepository;
import ebj.yujinkun.quotes.util.Injection;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private QuoteRepository quoteRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        quoteRepository = Injection.provideQuoteRepository(application);
    }

    public LiveData<Result<List<Quote>>> getQuotes() {
        return quoteRepository.getAllQuotes();
    }

    public void delete(Quote quote) {
        quoteRepository.delete(quote);
    }

    public void sync() {
        quoteRepository.sync();
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared");
        super.onCleared();
    }
}
