package ebj.yujinkun.quotes.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.util.DateUtil;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Quote>> quotes = new MutableLiveData<>();

    public HomeViewModel() {
        quotes.setValue(getTemporaryQuotes());
    }

    public LiveData<List<Quote>> getQuotes() {
        return quotes;
    }

    private List<Quote> getTemporaryQuotes() {
        List<Quote> quotes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Quote quote = new Quote.Builder()
                    .setId(UUID.randomUUID().toString())
                    .setContent("Content " + i)
                    .setDateCreated(DateUtil.getCurrentDate())
                    .setDateModified(DateUtil.getCurrentDate())
                    .build();
            quotes.add(quote);
        }
        return quotes;
    }
}