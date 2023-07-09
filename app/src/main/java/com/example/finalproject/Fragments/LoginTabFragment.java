package com.example.finalproject.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.DataManager;
import com.example.finalproject.Models.WorkItem;
import com.example.finalproject.Views.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.Utility.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class LoginTabFragment extends Fragment {

    private FirebaseAuth auth;
    private TextInputEditText editTextEmail, editTextPassword;
    private MaterialButton login_btn;
    private ProgressBar progressIndicator;
    private final int MIN_PASS_LEN = 6; // Minimum character limit
    private Boolean isEmailValid = false, isPassValid = false;
    private Helper helper;

    public LoginTabFragment(){
        helper = new Helper();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragement, container, false);
        findViews(root);
        auth = FirebaseAuth.getInstance();

        setEditTextListeners();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressIndicator.setVisibility(View.VISIBLE);
                String email, password;

                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(root.getContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(root.getContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Map<Integer, WorkItem> workItemsMap = DataManager.getInstance().getDataFromFirebase();
                                    //Toast.makeText(root.getContext(), getString(R.string.acc_created), Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", "createUserWithEmail:success" + workItemsMap);
                                    progressIndicator.setVisibility(View.GONE);
                                    FirebaseUser user = auth.getCurrentUser();

                                    startActivity(new Intent(getContext(), MainActivity.class));
                                }else{
                                    Toast.makeText(root.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        login_btn.setEnabled(isEmailValid && isPassValid);
    }

    private void findViews(View view){
        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        login_btn = view.findViewById(R.id.login_btn);
        progressIndicator = view.findViewById(R.id.progressBar);
    }
}
