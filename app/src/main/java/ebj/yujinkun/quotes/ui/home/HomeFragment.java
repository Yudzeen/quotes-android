package ebj.yujinkun.quotes.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ebj.yujinkun.quotes.R;
import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.model.Result;
import ebj.yujinkun.quotes.ui.adapter.QuotesAdapter;
import ebj.yujinkun.quotes.ui.common.FadeItemSwipeCallback;
import ebj.yujinkun.quotes.util.KeyConstants;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private ProgressBar progressBar;

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

        progressBar = root.findViewById(R.id.progress_bar);

        final RecyclerView recyclerView = root.findViewById(R.id.quotes_list);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new FadeItemSwipeCallback(new FadeItemSwipeCallback.Listener() {
            @Override
            public void onItemSwiped(final int position) {
                final Quote quote = quotesAdapter.get(position);
                homeViewModel.delete(quote);
            }
        }));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        final SwipeRefreshLayout swipeContainer = root.findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeViewModel.sync();
            }
        });

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

        homeViewModel.sync();

        homeViewModel.getQuotes().observe(this, new Observer<Result<List<Quote>>>() {
            @Override
            public void onChanged(Result<List<Quote>> result) {
                Log.d(TAG, "Result received. " + result.toString());
                if (result.getStatus() == Result.Status.IN_PROGRESS) {
                    if (!swipeContainer.isRefreshing()) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    swipeContainer.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    if (result.getStatus() == Result.Status.SUCCESS) {
                        quotesAdapter.setQuotes(result.getResource());
                    } else {    // error case
                        Toast.makeText(getContext(), getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                    }
                }
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