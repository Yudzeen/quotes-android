package ebj.yujinkun.quotes.ui.edit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ebj.yujinkun.quotes.R;
import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.util.KeyConstants;
import ebj.yujinkun.quotes.util.NetworkUtil;
import ebj.yujinkun.quotes.util.SoftKeyboardUtils;

public class QuoteEditFragment extends Fragment {

    private static final String TAG = QuoteEditFragment.class.getSimpleName();

    private static final String CONTENT_KEY = "content_key";
    private static final String QUOTEE_KEY = "quotee_key";

    private QuoteEditViewModel quoteEditViewModel;
    private TextInputLayout contentLayout;
    private TextInputLayout quoteeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quote_edit, container, false);

        quoteEditViewModel = ViewModelProviders.of(this).get(QuoteEditViewModel.class);

        setHasOptionsMenu(true);

        parseArguments(getArguments());
        setupActionBar();

        contentLayout = root.findViewById(R.id.content_input);
        quoteeLayout = root.findViewById(R.id.quotee_input);

        Quote originalQuote = quoteEditViewModel.getOriginalQuote();
        if (originalQuote != null) {
            Objects.requireNonNull(contentLayout.getEditText()).setText(originalQuote.getContent());
            Objects.requireNonNull(quoteeLayout.getEditText()).setText(originalQuote.getQuotee());
        }

        Objects.requireNonNull(contentLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                contentLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SoftKeyboardUtils.showSoftKeyboard(requireContext(), Objects.requireNonNull(contentLayout.getEditText()));
    }

    @Override
    public void onDestroyView() {
        View root = getView();
        if (root != null) {
            SoftKeyboardUtils.hideSoftKeyboard(requireContext(), root);
        }
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CONTENT_KEY, Objects.requireNonNull(contentLayout.getEditText()).getText().toString());
        outState.putString(QUOTEE_KEY, Objects.requireNonNull(quoteeLayout.getEditText()).getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Objects.requireNonNull(contentLayout.getEditText()).setText(savedInstanceState.getString(CONTENT_KEY));
            Objects.requireNonNull(quoteeLayout.getEditText()).setText(savedInstanceState.getString(QUOTEE_KEY));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quote_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp();
                return true;
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        if (validate()) {
            String content = Objects.requireNonNull(contentLayout.getEditText()).getText().toString();
            String quotee = Objects.requireNonNull(quoteeLayout.getEditText()).getText().toString();
            Quote quote = quoteEditViewModel.getNewQuote(content, quotee);
            if (quoteEditViewModel.getOriginalQuote() == null) {
                quoteEditViewModel.insert(quote);
            } else {
                quoteEditViewModel.update(quote);
            }
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigateUp();
        }
    }

    private boolean validate() {
        if (!NetworkUtil.isConnected(requireContext())) {
            Log.d(TAG, "validate: No network connection");
            Toast.makeText(requireContext(), R.string.please_connect, Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean valid = true;

        if (TextUtils.isEmpty(Objects.requireNonNull(contentLayout.getEditText()).getText())) {
            contentLayout.setError("Content is required");
            valid = false;
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(quoteeLayout.getEditText()).getText())) {
            quoteeLayout.getEditText().setText(R.string.anonymous);
        }

        return valid;
    }

    private void parseArguments(Bundle args) {
        if (args != null) {
            Quote quote = args.getParcelable(KeyConstants.QUOTE_KEY);
            if (quote != null) {
                quoteEditViewModel.setOriginalQuote(quote);
            }
        }
    }

    private void setupActionBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
            } else {
                Log.w(TAG, "Action bar is null.");
            }
        } else {
            Log.w(TAG, "Activity is null, can't setup action bar.");
        }

    }
}
