package ca.mcgill.ecse321.townlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;


import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentArchiveBinding;
import cz.msebera.android.httpclient.Header;

/**
 * Code for the book fragment, corresponds to layout fragment_book.xml.
 */
public class BookFragment extends ListFragment {

    private FragmentArchiveBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArchiveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // keep track of item id + name, status, and id only in separate arrays
        ArrayList<String> items = new ArrayList<>();
        ArrayList<String> statusArray = new ArrayList<>();
        ArrayList<String> idArray = new ArrayList<>();

        HttpUtils.get("/books", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] Header, JSONArray response) {

                try {
                    for (int i=0; i<response.length(); i++) {
                        int itemId = response.getJSONObject(i).getInt("id");
                        String itemName = response.getJSONObject(i).getString("name");
                        String itemStatus = response.getJSONObject(i).getString("status");

                        // items: used for displaying in ListView
                        // statusArray: used in popup when an item is clicked
                        // idArray: used for put request
                        items.add(itemId + " - " + itemName);
                        statusArray.add(itemStatus);
                        idArray.add(String.valueOf(itemId));

                        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
                        ListView listView = (ListView) binding.list;
                        listView.setAdapter(itemsAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                // inflate layout of popup window
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                                View popupView = inflater.inflate(R.layout.fragment_popup, null);

                                // popup TextView
                                TextView textView = (TextView) popupView.findViewById(R.id.popupText);
                                textView.setText(getString(R.string.popup_text, items.get(position), statusArray.get(position)));

                                // create popup window
                                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                // focusable = true allows popup to be dismissed when we tap outside the window
                                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

                                // show popup
                                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                                // Reserve Button
                                Button reserveButton = (Button) popupView.findViewById(R.id.buttonReserve);
                                if (!statusArray.get(position).equals("AVAILABLE")) reserveButton.setEnabled(false);
                                reserveButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        // Login check!
                                        if (!LoginStatus.INSTANCE.isLoggedIn()) {
                                            popupWindow.dismiss();
                                            Navigation.findNavController(view)
                                                    .navigate(R.id.LoginFragment);
                                            return;
                                        }
                                        HttpUtils.put("/books/" + idArray.get(position) + "/reserve", new RequestParams(), new JsonHttpResponseHandler(){
                                            @Override
                                            public void onSuccess(int statusCode, Header[] Header, JSONObject response) {
                                                popupWindow.dismiss();

                                                // How do I refresh???
                                                String successMessage = "Reserved! Please visit the library to checkout.";
                                                Snackbar.make(binding.getRoot(), successMessage, Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();

                                            }
                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                String errorMessage = ApiError.firstOr(ApiError.decodeError(responseString), "Unknown error, try again later");
                                                Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                        });

                                    }

                                });

                            }
                        });
                    }
                } catch (JSONException e) {
                    // ignored
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
