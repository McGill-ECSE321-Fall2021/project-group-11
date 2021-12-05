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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TransactionListAdapter extends ArrayAdapter<List<String>> {

    private ArrayList<List<String>> transactions;
    private final int layoutResourceId;
    private final Context context;
    private final int RENEW = 1;
    private final int RETURN = 2;
    private final ListView list;

    public TransactionListAdapter(Context context, int layoutResourceId, ArrayList<List<String>> transactions, ListView list){
        super(context, layoutResourceId, transactions);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.transactions = transactions;
        this.list = list;
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

        holder.renew = row.findViewById(R.id.imageButtonRenewTransaction);
        holder.renew.setTag(holder.json);
        setOnClickListener(holder.renew, builder, RENEW);

        holder.delete = row.findViewById(R.id.imageButtonDeleteTransaction);
        holder.delete.setTag(holder.json);
        setOnClickListener(holder.delete, builder, RETURN);

        row.setTag(holder);
        setupTransaction(holder);
        return row;
    }
    private void setOnClickListener(ImageButton button,AlertDialog.Builder builder, int id){

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
    private void onClickAction(ImageButton button, int buttonId){
        String id = ((List<String>) button.getTag()).get(0);

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
            HttpUtils.delete("/transactions/" + LoginStatus.INSTANCE.getUserId() + "/" + id, new RequestParams(), new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    int index = -1;
                    for (int i = 0; i < transactions.size(); i++) {
                        if (transactions.get(i).get(0) == id) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1){
                        transactions.remove(index);
                        notifyDataSetChanged();
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
