package ebj.yujinkun.quotes.model;

import android.text.TextUtils;

import java.util.Objects;
import java.util.UUID;

import ebj.yujinkun.quotes.util.DateUtil;

public class Quote {

    public static final String DEFAULT_QUOTEE = "Anonymous";

    private final String id;
    private final String content;
    private final String quotee;
    private final String dateCreated;
    private final String dateModified;

    public Quote(String id, String content, String quotee, String dateCreated, String dateModified) {
        this.id = id;
        this.content = content;
        this.quotee = quotee;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getQuotee() {
        return quotee;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", quotee='" + quotee + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateModified='" + dateModified + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return Objects.equals(id, quote.id) &&
                Objects.equals(content, quote.content) &&
                Objects.equals(quotee, quote.quotee) &&
                Objects.equals(dateCreated, quote.dateCreated) &&
                Objects.equals(dateModified, quote.dateModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, quotee, dateCreated, dateModified);
    }

    public static class Builder {

        private String id;
        private String content;
        private String quotee;
        private String dateCreated;
        private String dateModified;

        public Builder from(Quote quote) {
            id = quote.getId();
            content = quote.getContent();
            quotee = quote.getQuotee();
            dateCreated = quote.getDateCreated();
            dateModified = quote.getDateModified();
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setQuotee(String quotee) {
            this.quotee = quotee;
            return this;
        }

        public Builder setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        public Builder setDateModified(String dateModified) {
            this.dateModified = dateModified;
            return this;
        }

        public Quote build() {
            if (TextUtils.isEmpty(id)) {
                id = UUID.randomUUID().toString();
            }
            if (TextUtils.isEmpty(quotee)) {
                quotee = DEFAULT_QUOTEE;
            }
            if (TextUtils.isEmpty(dateCreated)) {
                dateCreated = DateUtil.getCurrentDate();
            }
            if (TextUtils.isEmpty(dateModified)) {
                dateModified = DateUtil.getCurrentDate();
            }
            return new Quote(id, content, quotee, dateCreated, dateModified);
        }
    }
}
