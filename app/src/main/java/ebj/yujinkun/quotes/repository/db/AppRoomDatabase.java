package ebj.yujinkun.quotes.repository.db;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ebj.yujinkun.quotes.model.Quote;

@Database(entities = {Quote.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {

    private static final String DB_NAME = "quote_db";

    private static volatile AppRoomDatabase instance;

    public abstract QuoteDao quoteDao();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppRoomDatabase getInstance(Application application) {
        if (instance == null) {
            synchronized (AppRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(application, AppRoomDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

}
