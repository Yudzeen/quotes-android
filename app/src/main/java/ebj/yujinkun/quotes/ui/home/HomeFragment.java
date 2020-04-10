package ebj.yujinkun.quotes.ui.home;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ebj.yujinkun.quotes.MainViewModel;
import ebj.yujinkun.quotes.R;
import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.model.Result;
import ebj.yujinkun.quotes.ui.adapter.QuotesAdapter;
import ebj.yujinkun.quotes.ui.common.FadeItemSwipeCallback;
import ebj.yujinkun.quotes.util.KeyConstants;
import ebj.yujinkun.quotes.util.NetworkUtil;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private boolean fromNetworkLost = false;

    private View root;
    private ProgressBar progressBar;
    private TextView connectionLabel;
    private Snackbar refreshSnackBar;

    private MainViewModel mainViewModel;
    private QuotesAdapter quotesAdapter;
    private SwipeRefreshLayout swipeContainer;

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            Log.d(TAG, "Network available.");
            onNetworkAvailable();
        }

        @Override
        public void onLost(@NonNull Network network) {
            Log.d(TAG, "Network lost.");
            onNetworkLost();
        }
    };

    private Observer<Result<List<Quote>>> observer = new Observer<Result<List<Quote>>>() {
        @Override
        public void onChanged(Result<List<Quote>> result) {
            Log.d(TAG, "Result received. " + result.toString());
            if (result.getStatus() == Result.Status.IN_PROGRESS) {
                connectionLabel.setVisibility(View.GONE);
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
                    if (!NetworkUtil.isConnected(requireContext())) {
                        onNetworkLost();
                    } else {
                        Toast.makeText(getContext(), R.string.error_occured, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    public HomeFragment() {}    // required public constructor

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        mainViewModel =
                ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_home_to_quote_edit_navigation);
            }
        });

        progressBar = root.findViewById(R.id.progress_bar);
        connectionLabel = root.findViewById(R.id.connection_label);
        refreshSnackBar = Snackbar.make(root, R.string.swipe_down_to_refresh, Snackbar.LENGTH_INDEFINITE);

        final RecyclerView recyclerView = root.findViewById(R.id.quotes_list);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new FadeItemSwipeCallback(new FadeItemSwipeCallback.Listener() {
            @Override
            public void onItemSwiped(final int position) {
                final Quote quote = quotesAdapter.get(position);
                mainViewModel.delete(quote);
            }
        }));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipeContainer = root.findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                if (refreshSnackBar.isShown()) {
                    refreshSnackBar.dismiss();
                }
                mainViewModel.sync();
            }
        });

        quotesAdapter = new QuotesAdapter();
        quotesAdapter.setOnItemClickListener(new QuotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Quote quote) {
                Bundle args = new Bundle();
                args.putParcelable(KeyConstants.QUOTE_KEY, quote);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_home_to_quote_edit_navigation, args);
            }
        });
        recyclerView.setAdapter(quotesAdapter);

        mainViewModel.getQuotes().observe(this, observer);

        setHasOptionsMenu(true);

        NetworkUtil.registerNetworkListener(requireContext(), networkCallback);

        root.requestFocus();

        return root;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        NetworkUtil.unregisterNetworkListener(requireContext(), networkCallback);
        mainViewModel.getQuotes().removeObserver(observer);
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    public void onNetworkAvailable() {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fromNetworkLost) {
                    fromNetworkLost = false;
                    connectionLabel.setBackgroundColor(Color.GREEN);
                    connectionLabel.setText(getString(R.string.network_connected));
                    connectionLabel.setTextColor(Color.DKGRAY);
                    refreshSnackBar.show();
                }
            }
        });
    }

    public void onNetworkLost() {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fromNetworkLost = true;
                connectionLabel.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                connectionLabel.setText(getString(R.string.no_network_connection));
                connectionLabel.setTextColor(Color.WHITE);
                connectionLabel.setVisibility(View.VISIBLE);
                refreshSnackBar.dismiss();
            }
        });
    }

}