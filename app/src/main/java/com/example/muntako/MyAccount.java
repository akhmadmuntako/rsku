package com.example.muntako;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by akhmadmuntako on 12/12/2016.
 */
public class MyAccount extends AppCompatActivity {
    SessionManager session;
    TextView userName, emailName, notelName, roleUser, ttlName;
    ImageView iconUser;
    Button editUser,logout;
    private static final int DIALOG_ERROR_CONNECTION = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName = (TextView) findViewById(R.id.user_name);
        emailName = (TextView) findViewById(R.id.email_user);
        notelName = (TextView) findViewById(R.id.notel_user);
        ttlName = (TextView) findViewById(R.id.ttl_user);
        iconUser = (ImageView) findViewById(R.id.icon_user);
        roleUser = (TextView) findViewById(R.id.role_user);
        editUser = (Button)findViewById(R.id.edit_user);
        logout = (Button)findViewById(R.id.logout);
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            HashMap<String, String> user = session.getUserDetails();
            userName.setText(user.get(SessionManager.KEY_NAME));
            roleUser.setText(user.get(SessionManager.KEY_ACKEY));
            emailName.setText(user.get(SessionManager.KEY_EMAIL));
            notelName.setText(user.get(SessionManager.KEY_NOTEL));
            ttlName.setText(user.get(SessionManager.KEY_TTL));
            if (user.get(SessionManager.KEY_ACKEY).equalsIgnoreCase("perempuan")) {
                iconUser.setImageResource(R.drawable.account_cewe);
            }
        }
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyAccount.this,EditAccount.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!isOnline(MyAccount.this)) {
                    showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyAccount.this);
                    alertDialogBuilder.setTitle(R.string.logout_msg);
                    alertDialogBuilder.setIcon(R.drawable.logo2);
                    alertDialogBuilder.setPositiveButton(R.string.ya,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    session.logoutUser();
                                    //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                    alertDialogBuilder.setNegativeButton(R.string.tidak,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    //Showing the alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        initCollapsingToolbar();

    }
    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
            return true;
        else
            return false;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);


        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.title_activity_my_account));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DIALOG_ERROR_CONNECTION:
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
                errorDialog.setTitle("Error");
                errorDialog.setMessage(R.string.error_internet);
                errorDialog.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog errorAlert = errorDialog.create();
                return errorAlert;
        }
        return dialog;
    }
}
