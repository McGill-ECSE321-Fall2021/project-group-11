package ca.mcgill.ecse321.townlibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentTransactionBinding;
import cz.msebera.android.httpclient.Header;

public class TransactionFragment extends Fragment{

    private FragmentTransactionBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set list to list of layout
        ListView list = binding.list;
        ArrayList<List<String>> transactions = new ArrayList<>();
        ArrayAdapter<List<String>> arrayAdapter = new TransactionListAdapter(this.getActivity(), R.layout.fragment_transaction_list, transactions);

        // Get a User's transactions
        HttpUtils.get("/transactions/" + LoginStatus.INSTANCE.getUserId(), new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject entry = response.getJSONObject(i);

                        // Build sudo DTO from JSONObject
                        final String id = entry.getString("id");
                        final String type = entry.getString("type");
                        final String startDate = entry.getString("startDate");

                        // Get the item associated to each transaction
                        HttpUtils.get("/" + type + "/transactions/" + id, new RequestParams(), new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    final String itemName = response.getString("name");

                                    transactions.add(Arrays.asList(id, type, startDate, itemName));
                                    arrayAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    Snackbar.make(binding.getRoot(), "Something went wrong and it's not your fault!\nFile a bug report!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                                // this happens for "ghost transactions", (which returns empty
                                // response string)
                            }
                        });
                    } catch (JSONException e) {
                        Snackbar.make(binding.getRoot(), "Something went wrong and it's not your fault!\nFile a bug report!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                String errorMessage = ApiError.firstOr(ApiError.decodeError(responseString), "Unknown error, try again later");
                Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Set listView to display information from transactions ArrayList
        list.setAdapter(arrayAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}