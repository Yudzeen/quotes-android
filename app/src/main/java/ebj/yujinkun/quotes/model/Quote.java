package ebj.yujinkun.quotes.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

import ebj.yujinkun.quotes.util.DateUtil;

@Entity
public class Quote implements Parcelable {

    public static final String DEFAULT_QUOTEE = "Anonymous";

    @PrimaryKey
    @NonNull
    private final String id;

    @ColumnInfo
    private final String content;

    @ColumnInfo
    private final String quotee;

    @ColumnInfo(name = "date_created")
    private final String dateCreated;

    @ColumnInfo(name = "date_modified")
    private final String dateModified;

    public Quote(@NonNull String id, String content, String quotee, String dateCreated, String dateModified) {
        this.id = id;
        this.content = content;
        this.quotee = quotee;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    @NonNull
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

    protected Quote(Parcel in) {
        id = Objects.requireNonNull(in.readString());
        content = in.readString();
        quotee = in.readString();
        dateCreated = in.readString();
        dateModified = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(quotee);
        dest.writeString(dateCreated);
        dest.writeString(dateModified);
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
