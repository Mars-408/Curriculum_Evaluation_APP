package com.example.myapplication.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.home.HomeViewModel;
import com.google.android.material.snackbar.Snackbar;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private long exitTime = 0;

    private EditText name;
    private EditText day;
    private EditText sex;
    private EditText nation;
    private EditText address;
    private EditText number;
    private EditText qq;
    private EditText email;

    private String _name;
    private String _day;
    private String _sex;
    private String _nation;
    private String _address;
    private String _number;
    private String _qq;
    private String _email;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        _name = sp.getString("name","Null");
        _sex = sp.getString("username","Null");
        _email = sp.getString("email","Null");
        _address =sp.getString("academy","Null");
        _qq = sp.getString("role","Null");

        getID(root);



        Button btn_exit = root.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ExitApp();
            }
        });

        Button btn_quit = root.findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sp.edit()
                        .clear()
                        .commit();
                Intent i = new Intent(getActivity() , LoginActivity.class);
                startActivity(i);
            }
        });
        return root;
    }

    public void ExitApp(){
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            Snackbar.make(getView(), "再按一次关闭程序", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            exitTime = System.currentTimeMillis();
        } else
        {
            getActivity().finish();
            System.exit(0);
        }
    }



    public void getID(View root){
        name = root.findViewById(R.id.name);
        name.setFocusable(false);
        qq = root.findViewById(R.id.qq);
        qq.setFocusable(false);
        sex = root.findViewById(R.id.sex);
        sex.setFocusable(false);

        address = root.findViewById(R.id.address);
        address.setFocusable(false);

        email = root.findViewById(R.id.email);
        email.setFocusable(false);

        set();
    }
    public void set(){
        name.setText(_name);
        sex.setText(_sex);
        email.setText(_email);
        address.setText(_address);
        qq.setText(_qq);
    }
}
