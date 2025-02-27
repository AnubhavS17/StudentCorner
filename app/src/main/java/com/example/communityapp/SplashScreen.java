package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.communityapp.modal.UserModel;
import com.example.communityapp.utis.FirebaseUtil;
import com.example.communityapp.utis.androidutil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if(FirebaseUtil.isLoggedIn() && getIntent().getExtras()!=null){
            //from notification
            String userId=getIntent().getExtras().getString("userId");
            FirebaseUtil.allUserCollectionReference().document(userId).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    UserModel model=task.getResult().toObject(UserModel.class);
                    Intent mainIntent=new Intent(this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mainIntent);
                        Intent intent =new Intent(this, ChatActivity.class);
                        androidutil.passUserModelAsIntent(intent,model);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                }
            });
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(FirebaseUtil.isLoggedIn()){
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }
                    else{
                        startActivity(new Intent(SplashScreen.this, LoginAcivity.class));
                    }
                    finish();
                }
            },1000);
        }




    }
}