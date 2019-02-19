package com.example.qrbarcodescanner.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.qrbarcodescanner.R;
import com.example.qrbarcodescanner.addapter.SaveDataAdapter;
import com.example.qrbarcodescanner.database.DatabaseHandler;
import com.example.qrbarcodescanner.dto.ScanData;

import java.util.ArrayList;

/**
 * Created by ntf-19 on 21/7/18.
 */

public class HistoryFragment extends Fragment {
    ArrayList<ScanData> scanDataArraylist;
    DatabaseHandler db;
    RecyclerView recyclerView;
    SaveDataAdapter adapter;
    private TextView dialogTV;
    private Animation animationFadeIn;
    private Animation animationFadeOut;
    AlertDialog alertDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        recyclerView = view.findViewById(R.id.recyler_view);

        scanDataArraylist = new ArrayList<>();
        Log.d("bharti", "value1" + scanDataArraylist.size());

        db = new DatabaseHandler(getActivity());
        scanDataArraylist = (ArrayList<ScanData>) db.getAllData();
        dialogTV = (TextView) view.findViewById(R.id.dialogTV);
        dialogTV.setVisibility(View.INVISIBLE);

        animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (animation == animationFadeIn) {
                    dialogTV.startAnimation(animationFadeOut);
                    dialogTV.startAnimation(animationFadeIn);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //checking internet connection
        if (scanDataArraylist.size() == 0) {
            dialogTV.setText("List is Empty");
            dialogTV.setVisibility(View.VISIBLE);
            dialogTV.startAnimation(animationFadeIn);

        } else {
/*
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
*/
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter = new SaveDataAdapter(scanDataArraylist, getActivity());

            adapter.notifyDataSetChanged();

            recyclerView.setAdapter(adapter);

        }
            return view;

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    public void showAlertDialog() {

        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
               getActivity());

        alertDialog2.setMessage("List is Empty");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        getActivity().finish();
                    }
                });
        alertDialog2.show();


    }
}
