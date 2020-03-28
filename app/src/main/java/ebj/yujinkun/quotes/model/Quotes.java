package ebj.yujinkun.quotes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Quotes {

    private Quotes() {}

    public static List<Quote> sortByLatestDateModified(List<Quote> quoteList) {
        List<Quote> quotes = new ArrayList<>(quoteList);
        Collections.sort(quotes, new Comparator<Quote>() {
            @Override
            public int compare(Quote q1, Quote q2) {
                return q2.getDateModified().compareTo(q1.getDateModified());
            }
        });
        return quotes;
    }

}
