package com.example.fta.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.example.fta.AdminActivity;
import com.example.fta.MenuActivity;
import com.example.fta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private View mview;
    private CircleImageView img_avatar;
    private EditText fullname, fullemail;
    private Button btnupdate, btnchangeps;
    private Uri imageUri;
    private MenuActivity menuActivity;
    private ProgressDialog progressDialog;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                if (result.getData() != null) {
                    imageUri = result.getData().getData();
                    btnupdate.setEnabled(true);
                    Glide.with(getActivity()).load(imageUri).into(img_avatar);
                }
            }else {
                Toast.makeText(getActivity(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mview = inflater.inflate(R.layout.fragment_profile, container, false);

        onControls();
        addEvents();
        setUserInfomation();
        menuActivity = (MenuActivity) getActivity();

        return mview;
    }

    private void onControls(){
        img_avatar = mview.findViewById(R.id.img_avatar);
        fullname = mview.findViewById(R.id.edtFullname);
        fullemail = mview.findViewById(R.id.edtFullEmail);
        btnupdate = mview.findViewById(R.id.btnupdate);
        btnchangeps = mview.findViewById(R.id.btnchangeps);
        progressDialog = new ProgressDialog(this.getActivity());
    }

    private void setUserInfomation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        fullname.setText(user.getDisplayName());
        fullemail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.avatar_default).into(img_avatar);
    }

    private void addEvents() {
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUploadImage();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdate();
            }
        });

        btnchangeps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickChangePS(Gravity.CENTER);
            }
        });
    }

    private void onClickUploadImage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        activityResultLauncher.launch(i);
    }

    private void onclickChangePS(int gravity) {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_changeps_user);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        if (Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }

        EditText oldps = dialog.findViewById(R.id.edtoldps);
        EditText newps = dialog.findViewById(R.id.edtnewps);
        EditText cfnewps = dialog.findViewById(R.id.edtcfnewps);
        Button done = dialog.findViewById(R.id.btndone);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OldPS = oldps.getText().toString().trim();
                String NewPS = newps.getText().toString().trim();
                String CFnewPS = cfnewps.getText().toString().trim();
                if (TextUtils.isEmpty(OldPS)){
                    Toast.makeText(getActivity(),"Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(NewPS)){
                    Toast.makeText(getActivity(),"Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(CFnewPS)){
                    Toast.makeText(getActivity(),"Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!NewPS.equals(CFnewPS)){
                    Toast.makeText(getActivity(),"Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                }else {
                    updatePassword(OldPS, NewPS, CFnewPS);
                }
            }
        });

        dialog.show();
    }

    private void updatePassword(String oldPS, String newPS, String cfnewPS) {
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPS);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPS).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onClickUpdate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        progressDialog.show();
        String edtfullname = fullname.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edtfullname)
                .setPhotoUri(imageUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            menuActivity.showUserInfomation();
                        }
                    }
                });
    }
}