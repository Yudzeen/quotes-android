package ebj.yujinkun.quotes.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ebj.yujinkun.quotes.R;
import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.ui.adapter.QuotesAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private QuotesAdapter quotesAdapter;

    public HomeFragment() {}    // required public constructor

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_home_to_quoteEditFragment);
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.quotes_list);
        quotesAdapter = new QuotesAdapter();
        quotesAdapter.setOnItemClickListener(new QuotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Quote quote) {
                Toast.makeText(getContext(), "Item clicked: " + quote.toString(), Toast.LENGTH_SHORT).show();
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

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }
}