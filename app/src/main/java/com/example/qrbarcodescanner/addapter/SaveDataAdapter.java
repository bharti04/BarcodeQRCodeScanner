package com.example.qrbarcodescanner.addapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qrbarcodescanner.HistoryDataActivity;
import com.example.qrbarcodescanner.R;
import com.example.qrbarcodescanner.database.DatabaseHandler;
import com.example.qrbarcodescanner.dto.ScanData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ntf-19 on 23/7/18.
 */

public class SaveDataAdapter extends RecyclerView.Adapter<SaveDataAdapter.ViewHolder> {
    private Context context;
    private List<ScanData> dataModels;
    public SaveDataAdapter(ArrayList<ScanData> dataModels, Context context) {
        this.dataModels = dataModels;
        this.context = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate layout for single row
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(SaveDataAdapter.ViewHolder holder, int position) {
        final ScanData dataModel = dataModels.get(position);
        holder.savedate.setText(dataModel.getContent());
        holder.date.setText(dataModel.getDate_time());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModels = new DatabaseHandler(context).removeByName(dataModel.getContent());
                if (dataModels.size()==0) {
                    showAlertDialog();
                }
                notifyDataSetChanged();

            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, HistoryDataActivity.class);
                in.putExtra("Code", dataModel.getContent());
                in.putExtra("type", dataModel.getDataFormat());
                context.startActivity(in);

            }
        });


            //  Log.d("DataAdapter", "deleteBtn " + itemsData!!.size)



            //    Log.d("DataAdapter", "deleteBtn " + itemsData!!.size)





    }
    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView savedate,date;
        public ImageButton delete;
        public LinearLayout linearLayout;


        public ViewHolder(final View itemView) {
            super(itemView);
            savedate= (TextView) itemView.findViewById(R.id.encodedata_tv);
            date= (TextView) itemView.findViewById(R.id.date_tv);
            delete= (ImageButton) itemView.findViewById(R.id.deleteBtn);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.textLayout);


        }
    }
    public void showAlertDialog() {

        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                context);


        alertDialog2.setMessage("List is Empty");


        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog;
                        dialog.cancel();
                    }
                });
        alertDialog2.show();

    }


}
