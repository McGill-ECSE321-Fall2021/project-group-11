package ca.mcgill.ecse321.townlibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentLoginBinding;
import cz.msebera.android.httpclient.Header;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_SignupFragment);
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // quick input validation
                final String err = validateInputOffline();
                if (err != null) {
                    Snackbar.make(binding.getRoot(), err, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                // Again, spammer proof-ish!
                binding.buttonLogin.setEnabled(false);

                final String password = binding.textInputPassword.getText().toString();
                RequestParams params = new RequestParams();
                params.put("password", password);
                HttpUtils.post("/auth/online-members/" + binding.textInputUsername.getText().toString(), params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // Perform the login by setting the state of login-status
                            final int userId = response.getInt("id");
                            final String username = response.getString("username");
                            final String name = response.getString("name");

                            LoginStatus.INSTANCE.login(userId, username, password);
                            LoginStatus.INSTANCE.setPreferredDisplayName(name);

                            // Transition into the profile screen
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_LoginFragment_to_ProfileFragment);
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
                        // To avoid flooding the Snackbars (cuz we all hate
                        // excessive popups), we only report the first error.
                        String errorMessage = ApiError.firstOr(ApiError.decodeError(responseString), "Unknown error, try again later");
                        Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onFinish() {
                        // like promised, re-enable the button
                        binding.buttonLogin.setEnabled(true);
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
     * Check for inputs that are clearly problematic.
     *
     * @return the issue with the present input or null if input seems valid
     */
    private String validateInputOffline() {
        if (binding.textInputUsername.getText().length() == 0)
            return "Username cannot be empty";
        if (binding.textInputPassword.getText().length() == 0)
            return "Password cannot be empty";

        return null;
    }
}
