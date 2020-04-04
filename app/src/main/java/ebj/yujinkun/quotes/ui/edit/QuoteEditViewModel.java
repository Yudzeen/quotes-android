package ebj.yujinkun.quotes.ui.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.repository.QuoteRepository;
import ebj.yujinkun.quotes.util.DateUtil;
import ebj.yujinkun.quotes.util.Injection;

public class QuoteEditViewModel extends AndroidViewModel {

    private QuoteRepository quoteRepository;

    private Quote originalQuote;

    public QuoteEditViewModel(@NonNull Application application) {
        super(application);
        quoteRepository = Injection.provideQuoteRepository(application);
    }

    public void setOriginalQuote(Quote originalQuote) {
        this.originalQuote = originalQuote;
    }

    public Quote getOriginalQuote() {
        return originalQuote;
    }

    public Quote getNewQuote(String content, String quotee) {
        if (originalQuote == null) {
            Quote quote = new Quote.Builder()
                    .setContent(content)
                    .setQuotee(quotee)
                    .build();
            return quote;
        } else {
            Quote quote = new Quote.Builder()
                    .from(originalQuote)
                    .setContent(content)
                    .setQuotee(quotee)
                    .setDateModified(DateUtil.getCurrentDate())
                    .build();
            return quote;
        }
    }

    public void insert(Quote quote) {
        quoteRepository.insert(quote);
    }

    public void update(Quote quote) {
        quoteRepository.update(quote);
    }

}
