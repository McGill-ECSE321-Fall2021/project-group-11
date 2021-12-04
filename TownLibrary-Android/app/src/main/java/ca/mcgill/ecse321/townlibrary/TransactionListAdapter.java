package ca.mcgill.ecse321.townlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionListAdapter extends ArrayAdapter<List<String>> {

    private final ArrayList<List<String>> transactions;
    private final int layoutResourceId;
    private final Context context;

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
        holder.itemName = row.findViewById(R.id.textViewTransactionItemName);
        holder.description = row.findViewById(R.id.textViewTransactionDescription);
        holder.renew = row.findViewById(R.id.imageButtonRenewTransaction);
        holder.delete = row.findViewById(R.id.imageButtonDeleteTransaction);
        holder.renew.setTag(holder.json);
        holder.delete.setTag(holder.json);
        row.setTag(holder);
        setupTransaction(holder);
        return row;
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

    public Dialog createDialog(final int dialog){
        return new AlertDialog.Builder(context.getApplicationContext())
                .setTitle("Please confirm removal")
                .setPositiveButton(R.string., new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }
    protected void handleRemoveConfirm(int dialog){

    }
}
