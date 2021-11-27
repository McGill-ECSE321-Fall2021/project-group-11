package ca.mcgill.ecse321.townlibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentSignupBinding;
import cz.msebera.android.httpclient.Header;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SignupFragment.this)
                        .popBackStack();
            }
        });

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // quick input validation
                final String err = validateInputOffline();
                if (err != null) {
                    Snackbar.make(binding.getRoot(), err, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                // sort of ugly way to prevent mad-persons like me spam
                // clicking the sign-up button and wondering why does it not
                // work...
                binding.buttonSignup.setEnabled(false);

                // which means we need re-enable it later on (see onFinish)

                // if quick validation seems ok, then we try to post!
                final String password = binding.textInputPassword.getText().toString();
                RequestParams params = new RequestParams();
                params.put("email", binding.textInputEmail.getText().toString());
                params.put("password", password);
                params.put("name", binding.textInputName.getText().toString());
                params.put("address", binding.textInputAddress.getText().toString());
                params.put("library", 0);
                HttpUtils.post("/online-members/" + binding.textInputUsername.getText().toString(), params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // Perform the login by setting the state of login-status
                            final int userId = response.getInt("id");
                            final String username = response.getString("username");
                            final String name = response.getString("name");

                            LoginStatus.INSTANCE.login(userId, username, password);
                            LoginStatus.INSTANCE.setPreferredDisplayName(name);

                            // Return to the fragment that requested a login.
                            // But we only enter here via login, meaning we need to pop twice!
                            final NavController navController = NavHostFragment.findNavController(SignupFragment.this);
                            navController.popBackStack();
                            navController.popBackStack();
                        } catch (JSONException ex) {
                            // This is really bad because it means the schema
                            // of the DTO is out of sync. Just Snackbar it and
                            // leave it as is.
                            Snackbar.make(binding.getRoot(), "Something went wrong and it's not your fault!\nFile a bug report!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        // Report the first error to avoid excessive popups
                        String errorMessage = ApiError.firstOr(ApiError.decodeError(responseString), "Unknown error, try again later");
                        Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onFinish() {
                        // re-enable our signup button!
                        binding.buttonSignup.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Because some inputs would let the server fail in unexpected ways, but
     * also because we should reduce the number of redundant HTTP requests,
     * we check and handle some cases that we know are definitely problematic.
     *
     * @return the issue with the present input or null if input seems valid
     */
    private String validateInputOffline() {
        if (binding.textInputUsername.getText().length() == 0)
            return "Username cannot be empty";
        if (binding.textInputPassword.getText().length() == 0)
            return "Password cannot be empty";
        if (binding.textInputName.getText().length() == 0)
            return "Name cannot be empty";
        if (binding.textInputEmail.getText().length() == 0)
            return "Email cannot be empty";
        if (binding.textInputAddress.getText().length() == 0)
            return "Address cannot be empty";

        return null;
    }
}
