package ebj.yujinkun.quotes.ui.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.repository.QuoteRepository;
import ebj.yujinkun.quotes.util.Injection;

public class QuoteEditViewModel extends AndroidViewModel {

    private QuoteRepository quoteRepository;

    public QuoteEditViewModel(@NonNull Application application) {
        super(application);
        quoteRepository = Injection.provideQuoteRepository(application);
    }

    public void insert(Quote quote) {
        quoteRepository.insert(quote);
    }
}
