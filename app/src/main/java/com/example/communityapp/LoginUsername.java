package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.communityapp.modal.UserModel;
import com.example.communityapp.utis.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUsername extends AppCompatActivity {
    EditText usernameInput;
    Button letMeInBtn;
    ProgressBar progressBar;
    String phonenumber;
    UserModel usermodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);
        usernameInput=findViewById(R.id.login_username);
        letMeInBtn=findViewById(R.id.login_enter);
        progressBar=findViewById(R.id.login_progressbar);
        phonenumber=getIntent().getExtras().getString("phone");
        getUsername();
        letMeInBtn.setOnClickListener(view -> setUsername());

    }
    void setUsername(){
        String user=usernameInput.getText().toString();
        if(user.isEmpty()||user.length()<3){
            usernameInput.setError("Username length should be at least 3 chars");
            return;
        }
        setInProgress(true);
        if(usermodel!=null){
            usermodel.setUsername(user);
        }
        else{
            usermodel=new UserModel(phonenumber,user, Timestamp.now(),FirebaseUtil.currentUserId());
        }
        FirebaseUtil.currentUserDetails().set(usermodel).addOnCompleteListener(task -> {
            setInProgress(false);
            if(task.isSuccessful()){
                Intent intent=new Intent(LoginUsername.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "FAILED!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void getUsername(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            if(task.isSuccessful()){
                usermodel=task.getResult().toObject(UserModel.class);
                if(usermodel!=null){
                    usernameInput.setText(usermodel.getUsername());
                }
            }
        });
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }
    }
}