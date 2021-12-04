package ca.mcgill.ecse321.townlibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentTransactionBinding;
import cz.msebera.android.httpclient.Header;


public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView list = binding.list;
        ArrayList<Integer> arrayList = new ArrayList<>();
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, arrayList);
        HttpUtils.get("/transactions/" + LoginStatus.INSTANCE.getUserId(), new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                for (int i = 0; i < response.length(); i++){
                    try {
                        arrayList.add(response.getJSONObject(i).getInt("id"));
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
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=list.getItemAtPosition(position).toString();
                Toast.makeText(TransactionFragment.this.getActivity(),clickedItem,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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