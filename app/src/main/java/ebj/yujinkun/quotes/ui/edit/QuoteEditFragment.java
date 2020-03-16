package ebj.yujinkun.quotes.ui.edit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ebj.yujinkun.quotes.R;
import ebj.yujinkun.quotes.model.Quote;
import ebj.yujinkun.quotes.util.KeyConstants;

public class QuoteEditFragment extends Fragment {

    private static final String TAG = QuoteEditFragment.class.getSimpleName();

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quote_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            save();
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        if (validate()) {
            String content = Objects.requireNonNull(contentLayout.getEditText()).getText().toString();
            String quotee = Objects.requireNonNull(quoteeLayout.getEditText()).getText().toString();
            quoteEditViewModel.save(content, quotee);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigateUp();
        }
    }

    private boolean validate() {
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
}
