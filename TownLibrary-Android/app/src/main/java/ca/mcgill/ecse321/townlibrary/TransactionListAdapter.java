package ca.mcgill.ecse321.townlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TransactionListAdapter extends ArrayAdapter<List<String>> {

    private final ArrayList<List<String>> transactions;
    private final int layoutResourceId;
    private final Context context;
    private final int RENEW = 1;
    private final int RETURN = 2;

    public TransactionListAdapter(Context context, int layoutResourceId, ArrayList<List<String>> transactions){
        super(context, layoutResourceId, transactions);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.transactions = transactions;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        TransactionHolder holder;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new TransactionHolder();
        holder.json = transactions.get(position);

        //Set textViews
        holder.itemName = row.findViewById(R.id.textViewTransactionItemName);
        holder.description = row.findViewById(R.id.textViewTransactionDescription);

        //Set buttons
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Renew button
        holder.renew = row.findViewById(R.id.imageButtonRenewTransaction);
        //Associate row information with the button
        holder.renew.setTag(holder.json);
        setOnClickListener(holder.renew, builder, RENEW);

        //Delete button
        holder.delete = row.findViewById(R.id.imageButtonDeleteTransaction);
        //Associate row information with the button
        holder.delete.setTag(holder.json);
        setOnClickListener(holder.delete, builder, RETURN);

        row.setTag(holder);
        setupTransaction(holder);
        return row;
    }

    /**
     * Sets the onClickListener of a button to open a confirmation dialog for the corresponding action
     * @param button Button to be set
     * @param builder Builder to construct the dialog
     * @param id Action id, either REMOVE or RETURN
     */
    private void setOnClickListener(ImageButton button,AlertDialog.Builder builder, int id) {
        String action = id == RETURN ? "remove" : "renew";
        String alert = "Do you want to " + action + " this transaction?";
        String title = id == RETURN ? "Return" : "Renewal";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage(alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClickAction(button, id);
                            }
                        })
                        .setNegativeButton("No", null);

                AlertDialog alert = builder.create();
                alert.setTitle(title);
                alert.show();
            }
        });
    }

    /**
     * Displays the information of a row onto the ListView
     * @param holder Contains information of a specific row
     */
    private void setupTransaction(TransactionHolder holder){
        // Extract fields from transaction
        String name = holder.json.get(3);

        String type = holder.json.get(1);
        type = type.substring(0, type.length()-1).toUpperCase();

        String startDate = new Date(Long.parseLong(holder.json.get(2))).toString();
        String formattedDate = startDate.substring(8,10) + " " + startDate.substring(4,7) + "," + startDate.substring(startDate.length()-5) ;
        String description = String.format("%s \n due on %s", type, formattedDate);
        // Display information onto list.
        holder.itemName.setText(name);
        holder.description.setText(description);
    }

    public static class TransactionHolder {
        // Holds information of a single transaction
        List<String> json;
        TextView itemName;
        TextView description;
        ImageButton renew;
        ImageButton delete;
    }

    /**
     * Performs HttpUtils request mapped to the "YES" of a button's confirmation dialog.
     * i.e "YES" on delete would perform delete.
     * @param button Button that has been pressed
     * @param buttonId Action requested, either RENEW or RETURN
     */
    private void onClickAction(ImageButton button, int buttonId){
        String id = (String) ((List<?>) button.getTag()).get(0);

        if (buttonId == RENEW){
            HttpUtils.put("/transactions/" + LoginStatus.INSTANCE.getUserId() + "/" + id, new RequestParams(), new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // To avoid flooding the Snackbars (cuz we all hate
                    // excessive popups), we only report the first error.
                    String errorMessage = ApiError.firstOr(ApiError.decodeError(responseString), "Unknown error, try again later");
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            HttpUtils.delete("/transactions/" + LoginStatus.INSTANCE.getUserId() + "/" + id, new RequestParams(), new TextHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    int index = -1;
                    // Find the transaction to be removed
                    for (int i = 0; i < transactions.size(); i++) {
                        if ((transactions.get(i).get(0)).equals(id)) {
                            index = i;
                            break;
                        }
                    }
                    // Dynamically update the list view
                    if (index != -1){
                        transactions.remove(index);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Item returned successfully", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // To avoid flooding the Snackbars (cuz we all hate
                    // excessive popups), we only report the first error.
                    String errorMessage = ApiError.firstOr(ApiError.decodeError(responseString), "Unknown error, try again later");
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
        notifyDataSetChanged();
    }
}
