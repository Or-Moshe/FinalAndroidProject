package com.example.finalproject.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.Utility.Helper;
import com.example.finalproject.Utility.RegexUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupTabFragment extends Fragment {

    private TextInputEditText editTextEmail, editTextPassword;
    private MaterialButton signup_btn;
    private FirebaseAuth auth;
    private CircularProgressIndicator progressIndicator;
    private final int MIN_PASS_LEN = 6; // Minimum character limit
    private Boolean isEmailValid = false, isPassValid = false;
    private Helper helper;

    public SignupTabFragment(){
        helper = new Helper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        findViews(root);
        auth = FirebaseAuth.getInstance();
        setEditTextListeners();



        /*if(TextUtils.isEmpty(email)){
            Toast.makeText(root.getContext(), getString(R.string.enter_email), Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(root.getContext(), getString(R.string.enter_password), Toast.LENGTH_LONG).show();
        }*/

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                progressIndicator.setVisibility(View.VISIBLE);


                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(root.getContext(), getString(R.string.acc_created), Toast.LENGTH_SHORT).show();
                                    progressIndicator.setVisibility(View.GONE);

                                }else{
                                    Log.e("TAG", "signup failed: ", task.getException());
                                    Toast.makeText(root.getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        return root;
    }

    private void setEditTextListeners() {
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                isEmailValid = helper.validateEmail(editTextEmail, input, "Invalid email address");
                disableBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                isEmailValid = helper.validateEmail(editTextEmail, input, "Invalid email address");
                disableBtn();
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TAG", "onTextChanged: ");
                String input = s.toString();
                isPassValid = helper.validatePassword(editTextPassword, input, MIN_PASS_LEN, "Minimum " + MIN_PASS_LEN + " characters required");
                disableBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                isPassValid = helper.validatePassword(editTextPassword, input, MIN_PASS_LEN, "Minimum " + MIN_PASS_LEN + " characters required");
                disableBtn();
            }
        });
    }

    private void disableBtn(){
        signup_btn.setEnabled(isEmailValid && isPassValid);
    }
    private void findViews(View view){
        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        signup_btn = view.findViewById(R.id.signup_btn);
        progressIndicator = view.findViewById(R.id.progressBar);
    }
}
