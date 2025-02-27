package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.communityapp.utis.androidutil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOTPActivity extends AppCompatActivity {
    String phoneNumber;
    Long timeoutSeconds=60L;
    EditText otpInput;
    ProgressBar progressBar;
    TextView resend_otp;
    Button enter;
    String verification_code;
    PhoneAuthProvider.ForceResendingToken forceResendingToken1;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otpactivity);
        otpInput=findViewById(R.id.login_otp);
        progressBar=findViewById(R.id.login_progressbar);
        resend_otp=findViewById(R.id.resend_otp);
        enter=findViewById(R.id.login_next);

        phoneNumber= getIntent().getExtras().getString("phone");
        sendOtp(phoneNumber,false);
        enter.setOnClickListener(view -> {
            String entered_otp=otpInput.getText().toString();
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verification_code,entered_otp);
            signIn(credential);
            setInProgress(true);
        });
        resend_otp.setOnClickListener(view -> {
            sendOtp(phoneNumber,true);
        });
    }
    void sendOtp(String phoneNumber,boolean isResend){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder=PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber).setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        setInProgress(false);
                        androidutil.showToast(getApplicationContext(),"OTP verification failed");
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verification_code=s;
                        forceResendingToken1=forceResendingToken;
                        androidutil.showToast(getApplicationContext(),"OTP sent successfully");
                        setInProgress(false);
                    }
                });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(forceResendingToken1).build());
        }
        else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            enter.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            enter.setVisibility(View.VISIBLE);
        }
    }
    void signIn(PhoneAuthCredential phoneAuthCredential){
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent=new Intent(LoginOTPActivity.this, LoginUsername.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                }
                else {
                    androidutil.showToast(getApplicationContext(),"OTP verification failed");
                }
            }
        });
    }
    void startResendTimer(){
        resend_otp.setEnabled(false);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                resend_otp.setText("Resend OTP in "+timeoutSeconds+"seconds");
                if(timeoutSeconds<=0){
                    timeoutSeconds=60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        resend_otp.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
}