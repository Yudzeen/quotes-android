package ebj.yujinkun.quotes.repository.remote;

import java.util.List;

import ebj.yujinkun.quotes.model.Quote;

public interface RemoteDatabase {

    void getAllQuotes(GetAllQuotesCallback callback);
    void insert(Quote quote, InsertCallback callback);
    void update(Quote quote, UpdateCallback callback);
    void delete(Quote quote, DeleteCallback callback);

    interface GetAllQuotesCallback {
        void onSuccess(List<Quote> quotes);
        void onError(String message);
    }

    interface GenericCallback {
        void onSuccess(Quote quote);
        void onError(String message);
    }

    interface InsertCallback extends GenericCallback {}
    interface UpdateCallback extends GenericCallback {}
    interface DeleteCallback extends GenericCallback {}

}
