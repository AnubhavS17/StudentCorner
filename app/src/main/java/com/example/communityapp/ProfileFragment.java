package com.example.communityapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.communityapp.modal.UserModel;
import com.example.communityapp.utis.FirebaseUtil;
import com.example.communityapp.utis.androidutil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {
    ImageView profilePic;
    EditText usernameInput;
    EditText phoneInput;
    Button updateProfileBtn;
    ProgressBar progressBar;
    TextView logoutBtn;
    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data=result.getData();
                            if(data!=null && data.getData()!=null){
                                selectedImageUri=data.getData();
                                androidutil.setProfilePic(getContext(),selectedImageUri,profilePic);
                            }
                        }
                }


                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic=view.findViewById(R.id.profile_image_view);
        usernameInput=view.findViewById(R.id.profile_username);
        phoneInput=view.findViewById(R.id.profile_phone);
        updateProfileBtn=view.findViewById(R.id.profile_update_button);
        progressBar=view.findViewById(R.id.profile_progress_bar);
        logoutBtn=view.findViewById(R.id.logout_btn);

        getUserData();

        updateProfileBtn.setOnClickListener(view1 -> {
            updateBtnClick();
        });

        logoutBtn.setOnClickListener(view12 -> {
            FirebaseUtil.logout();
            Intent intent=new Intent(getContext(), SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        profilePic.setOnClickListener(view13 -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });
        return view;
    }
    void updateBtnClick(){
        String newUsername=usernameInput.getText().toString();
        if(newUsername.isEmpty()||newUsername.length()<3){
            usernameInput.setError("Username length should be at least 3 chars");
            return;
        }
        currentUserModel.setUsername(newUsername);
        setInProgress(true);
        if(selectedImageUri!=null){
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        updateToFirestore();
                    });
        }else {
            updateToFirestore();
        }

    }
    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                setInProgress(false);
                androidutil.showToast(getContext(),"Updated Successfully!");
            }
            else{
                androidutil.showToast(getContext(),"Somehow update failed!");
            }
        });
    }
    void getUserData(){
        setInProgress(true);

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Uri uri=task.getResult();
                                androidutil.setProfilePic(getContext(),uri,profilePic);
                            }
                        });



        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUserModel=task.getResult().toObject(UserModel.class);
            usernameInput.setText(currentUserModel.getUsername());
            phoneInput.setText(currentUserModel.getPhone());
        });
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            updateProfileBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            updateProfileBtn.setVisibility(View.VISIBLE);
        }
    }
}