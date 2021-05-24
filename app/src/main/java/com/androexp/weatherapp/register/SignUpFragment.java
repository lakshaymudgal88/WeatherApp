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

public class SignUpFragment extends Fragment {

    private TextInputEditText etUserName, etPassword;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initUI(view);
        return view;
    }

    private void initUI(View view) {

        etUserName = view.findViewById(R.id.et_user_name);
        etPassword = view.findViewById(R.id.et_password);
        Button signUpBtn = view.findViewById(R.id.sign_up_btn);
        progressBar = view.findViewById(R.id.progress_bar);

        signUpBtn.setOnClickListener(view1 -> {
            String name = Objects.requireNonNull(etUserName.getText()).toString().trim();
            String pass = Objects.requireNonNull(etPassword.getText()).toString().trim();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) &&
                    name.length() >= 6 && name.length() <= 16 &&
                    pass.length() >= 8 && pass.length() <= 16) {
                signUpUser(name, pass);
            } else if (TextUtils.isEmpty(name) || name.length() < 6 || name.length() > 16) {
                etUserName.setError("Password must be between 8-16!");
            } else if (TextUtils.isEmpty(pass) || pass.length() < 8 || pass.length() > 16) {
                etPassword.setError("Password must be between 8-16!");
            }
        });

    }

    private void signUpUser(String name, String pass) {

        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(pass);
        user.signUpInBackground(e -> {
            if (e == null) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                startActivity(new Intent(getContext(), MainActivity.class));
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                ParseUser.logOut();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}