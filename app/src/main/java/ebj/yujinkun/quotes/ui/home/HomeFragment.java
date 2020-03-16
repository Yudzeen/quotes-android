package ebj.yujinkun.quotes.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ebj.yujinkun.quotes.R;
import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.ui.adapter.QuotesAdapter;
import ebj.yujinkun.quotes.ui.common.FadeItemSwipeCallback;
import ebj.yujinkun.quotes.util.KeyConstants;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomeViewModel homeViewModel;
    private QuotesAdapter quotesAdapter;

    public HomeFragment() {}    // required public constructor

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_home_to_quoteEditFragment);
            }
        });

        final RecyclerView recyclerView = root.findViewById(R.id.quotes_list);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new FadeItemSwipeCallback(new FadeItemSwipeCallback.Listener() {
            @Override
            public void onItemSwiped(final int position) {
                final Quote quote = quotesAdapter.delete(position);
                homeViewModel.delete(quote);
                Snackbar.make(root, "Quote deleted.", Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                homeViewModel.insert(quote);
                                quotesAdapter.insert(quote, position);
                                recyclerView.scrollToPosition(position);
                            }
                        })
                        .show();
            }
        }));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        quotesAdapter = new QuotesAdapter();
        quotesAdapter.setOnItemClickListener(new QuotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Quote quote) {
                Bundle args = new Bundle();
                args.putParcelable(KeyConstants.QUOTE_KEY, quote);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_home_to_quoteEditFragment, args);
            }
        });
        recyclerView.setAdapter(quotesAdapter);

        homeViewModel.getQuotes().observe(this, new Observer<List<Quote>>() {
            @Override
            public void onChanged(List<Quote> quotes) {
                quotesAdapter.setQuotes(quotes);
            }
        });

        setHasOptionsMenu(true);

        root.requestFocus();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }
}