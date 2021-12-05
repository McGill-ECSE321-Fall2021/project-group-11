package ca.mcgill.ecse321.townlibrary;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.ListFragment;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentArchiveBinding;
import cz.msebera.android.httpclient.Header;

/**
 * Code for the book fragment, corresponds to layout fragment_book.xml.
 */
public class BookFragment extends ListFragment {

    private FragmentArchiveBinding binding;
    private String error;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArchiveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> items = new ArrayList<String>();

        HttpUtils.get("/books", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] Header, JSONArray response) {
                Log.d("response", response.toString());

                try {
                    for (int i=0; i<response.length(); i++) {
                        int itemId = response.getJSONObject(i).getInt("id");
                        String itemName = response.getJSONObject(i).getString("name");
                        String itemStatus = response.getJSONObject(i).getInt("id");
                        //Log.d("id", String.valueOf(itemId));
                        //Log.d("name", itemName);
                        items.add(String.valueOf(itemId) + " - " + itemName);

                        Log.d("items array", items.toString());
                        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
                        ListView listView = (ListView) binding.list;
                        listView.setAdapter(itemsAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("On click", items.get(position));

                                // inflate layout of popup window
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                                View popupView = inflater.inflate(R.layout.fragment_popup, null);

                                // set popup text to match clicked item
                                TextView textView = (TextView) popupView.findViewById(R.id.popupText);
                                textView.setText(getString(R.string.popup_text, items.get(position)));

                                // create popup window
                                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                // focusable = true allows popup to be dismissed when we tap outside the window
                                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

                                // show popup
                                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                            }
                        });
                    }
                } catch (JSONException e) {
                    error += e.getMessage();
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
