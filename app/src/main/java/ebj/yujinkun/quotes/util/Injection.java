package ebj.yujinkun.quotes.util;

import android.app.Application;

import ebj.yujinkun.quotes.repository.QuoteRepository;
import ebj.yujinkun.quotes.repository.QuoteRepositoryImpl;
import ebj.yujinkun.quotes.repository.db.AppRoomDatabase;
import ebj.yujinkun.quotes.repository.db.QuoteDao;
import ebj.yujinkun.quotes.repository.remote.RemoteDatabase;
import ebj.yujinkun.quotes.repository.remote.RemoteDatabaseImpl;

public class Injection {

    public static QuoteRepository provideQuoteRepository(Application application) {
        RemoteDatabase remoteDatabase = RemoteDatabaseImpl.getInstance(application);
        QuoteDao quoteDao = AppRoomDatabase.getInstance(application).quoteDao();
        return QuoteRepositoryImpl.getInstance(remoteDatabase, quoteDao);
    }

}
