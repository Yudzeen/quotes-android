package ebj.yujinkun.quotes.util;

import android.app.Application;

import ebj.yujinkun.quotes.repository.QuoteRepository;
import ebj.yujinkun.quotes.repository.QuoteRepositoryImpl;
import ebj.yujinkun.quotes.repository.db.AppRoomDatabase;

public class Injection {

    public static QuoteRepository provideQuoteRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getInstance(application);
        return new QuoteRepositoryImpl(db.quoteDao());
    }

}
