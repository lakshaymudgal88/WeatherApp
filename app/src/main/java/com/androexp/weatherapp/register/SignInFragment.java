package com.androexp.weatherapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.androexp.weatherapp.MainActivity;
import com.androexp.weatherapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseUser;
import java.util.Objects;

public class SignInFragment extends Fragment {

    private TextInputEditText etUserName, etPassword;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        initUI(view);
        return view;
    }

    private void initUI(View view) {

        etUserName = view.findViewById(R.id.et_user_name);
        etPassword = view.findViewById(R.id.et_password);
        Button signInBtn = view.findViewById(R.id.sign_in_btn);
        progressBar = view.findViewById(R.id.progress_bar);

        signInBtn.setOnClickListener(view1 -> {
            String name = Objects.requireNonNull(etUserName.getText()).toString().trim();
            String pass = Objects.requireNonNull(etPassword.getText()).toString().trim();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
                signInUser(name, pass);
            } else if (TextUtils.isEmpty(name)) {
                etUserName.setError("Invalid user name!");
            } else {
                etPassword.setError("Invalid password");
            }
        });

    }

    private void signInUser(String name, String pass) {

        if(progressBar.getVisibility() == View.GONE){
            progressBar.setVisibility(View.VISIBLE);
        }
        ParseUser.logInInBackground(name, pass, (user, e) -> {
            if (user != null && e == null) {
                startActivity(new Intent(getContext(), MainActivity.class));
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                ParseUser.logOut();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if(progressBar.getVisibility() == View.VISIBLE){
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}