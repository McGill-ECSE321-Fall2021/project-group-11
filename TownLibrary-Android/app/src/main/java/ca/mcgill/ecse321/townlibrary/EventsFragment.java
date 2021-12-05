package ca.mcgill.ecse321.townlibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentEventsBinding;
import cz.msebera.android.httpclient.Header;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView list = binding.list;
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, arrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                ArrayList<String> events = ((ArrayList<String>) list.getItemAtPosition(position));
                String name = events.get(0);
                String id = events.get(1);

                name = name.substring(0, name.length() - 1).toUpperCase();
                text1.setText(name);
                text2.setText(id);
                return view;
            }
        };

        // TODO:
        HttpUtils.get("/events", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        ArrayList<String> temp = new ArrayList<>();
                        JSONObject entry = response.getJSONObject(i);
                        temp.add(entry.getString("name"));
                        temp.add(entry.getString("id"));
                        arrayList.add(i, temp);
                    } catch(JSONException e) {
                        Snackbar.make(binding.getRoot(), "Something went wrong and it's not your fault!\nFile a bug report!",
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                String errorMessage = ApiError.firstOr(ApiError.decodeError(responseString), "Unknown error, try again later");
                Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        list.setAdapter(arrayAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}