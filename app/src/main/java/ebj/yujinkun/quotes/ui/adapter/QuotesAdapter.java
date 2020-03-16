package ebj.yujinkun.quotes.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ebj.yujinkun.quotes.R;
import ebj.yujinkun.quotes.model.Quote;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuotesViewHolder> {

    private static final String TAG = QuotesAdapter.class.getSimpleName();

    private List<Quote> quotes = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quote, parent, false);
        return new QuotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesViewHolder holder, int position) {
        Quote quote = quotes.get(position);
        holder.bind(quote);
        holder.setOnClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
        notifyDataSetChanged();
    }

    public Quote delete(int position) {
        Quote quote = quotes.remove(position);
        notifyItemRemoved(position);
        return quote;
    }

    public void insert(Quote quote, int position) {
        quotes.add(position, quote);
        notifyItemInserted(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class QuotesViewHolder extends RecyclerView.ViewHolder {

        private TextView content;
        private TextView quotee;
        private Quote quote;

        QuotesViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            quotee = itemView.findViewById(R.id.quotee);
        }

        void bind(Quote quote) {
            this.quote = quote;
            content.setText(quote.getContent());
            quotee.setText(quote.getQuotee());
        }

        void setOnClickListener(final OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Item clicked: " + quote.toString());
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(quote);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Quote quote);
    }
}
