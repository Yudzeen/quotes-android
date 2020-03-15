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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ebj.yujinkun.quotes.R;

public class QuoteEditFragment extends Fragment {

    private static final String TAG = QuoteEditFragment.class.getSimpleName();

    private TextInputLayout contentLayout;
    private TextInputLayout quoteeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quote_edit, container, false);

        setHasOptionsMenu(true);

        contentLayout = root.findViewById(R.id.content_input);
        quoteeLayout = root.findViewById(R.id.quotee_input);

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
            Toast.makeText(requireContext(), "Saved.", Toast.LENGTH_SHORT).show();
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
}
