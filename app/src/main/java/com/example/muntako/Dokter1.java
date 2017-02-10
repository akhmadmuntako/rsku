package com.example.muntako;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;



/**
 * A simple {@link Fragment} subclass.
 */
public class Dokter1 extends Fragment {
    private static final int DIALOG_ERROR_CONNECTION = 0;
    String url= Config.LIST_ANTRIAN_1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView lv;
    public Dokter1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dokter1, container, false);
        lv = (ListView) view.findViewById(R.id.lv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sw);
        mSwipeRefreshLayout.setColorScheme(R.color.orange,R.color.green,R.color.blue);
        onRefesh();
        if (!isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.error_internet, Toast.LENGTH_SHORT).show();
        } else {
            lv.setAdapter(null);
            final Downloader_antrian d = new Downloader_antrian(getContext(), url, lv);
            d.execute();
        }
        return view;
    }

    public void onRefesh(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isOnline(getActivity())) {
                    Toast.makeText(getActivity(), R.string.error_internet, Toast.LENGTH_SHORT).show(); //displaying the created dialog.
                } else {
                    lv.setAdapter(null);
                    final Downloader_antrian d = new Downloader_antrian(getContext(), url, lv);
                    d.execute();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    public boolean isOnline(Activity c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
            return true;
        else
            return false;
    }

    protected Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.error_internet);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
    public class InternetConnection extends Activity {
        static final int DIALOG_ERROR_CONNECTION = 1;}



}
