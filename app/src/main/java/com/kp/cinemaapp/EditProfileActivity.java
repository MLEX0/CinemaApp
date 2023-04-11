package com.kp.cinemaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kp.cinemaapp.model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseDatabase CinemaAppDB;
    private DatabaseReference UserDataBase;

    private ValueEventListener userValueEventListener;

    private EditText textUserName;
    private EditText textUserLastName;
    private EditText textUserPhone;
    private EditText textUserEmail;
    private CircleImageView imageProfilePicture;
    private Uri uploadUri;
    private User editUser;
    private boolean imageIsLoading;

    private StorageReference ImageProfileStorageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Init();
        AddUserDataListener();
    }

    private void Init(){
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            finish();
        }

        CinemaAppDB = FirebaseDatabase.getInstance("https://cinemaapp-80774-default-rtdb.firebaseio.com/");
        UserDataBase = CinemaAppDB.getReference(Constants.USER_KEY);

        ImageProfileStorageReference = FirebaseStorage.getInstance().getReference("UserProfilePictures");

        textUserName = findViewById(R.id.editTextEditPersonName);
        textUserLastName = findViewById(R.id.editTextEditPersonLastName);
        textUserPhone = findViewById(R.id.editTextEditPersonPhone);
        textUserEmail = findViewById(R.id.editTextEditPersonEmail);
        imageProfilePicture = findViewById(R.id.edit_profile_image);

        imageIsLoading = false;
    }

    private void AddUserDataListener(){
        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Пытаемся найти имя пользователя в БД
                for(DataSnapshot ds2 : dataSnapshot.getChildren()){
                    User user = ds2.getValue(User.class);
                    assert user != null;
                    if(mAuth.getCurrentUser() != null){
                        if(user.Uid.equals(mAuth.getCurrentUser().getUid().toString())){
                            if(user.FirstName != null){
                                textUserName.setText(user.FirstName);
                            }
                            if(user.LastName != null){
                                textUserLastName.setText(user.LastName);
                            }
                            if(user.Phone != null){
                                textUserPhone.setText(user.Phone);
                            }
                            if(user.ProfileImagePath != null){
                                Picasso.get().load(user.ProfileImagePath).into(imageProfilePicture);
                            }
                            textUserEmail.setText(mAuth.getCurrentUser().getEmail());
                            editUser = user;
                            editUser.userID = ds2.getKey();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        UserDataBase.addValueEventListener(userValueEventListener);
    }

    public void chooseImageOnClick(View view) {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null && data.getData() != null){
            if(resultCode == RESULT_OK){
                imageProfilePicture.setImageURI(data.getData());
                uploadImage();
            }
        }
    }

    public void saveChangesOnClick(View view) {
        if(imageIsLoading){
            Toast.makeText(EditProfileActivity.this,
                    "Подождите, картинка выгружается в облако",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference changePath = UserDataBase.child(editUser.userID);

        if(!textUserName.getText().toString().equals("")){
            editUser.FirstName = textUserName.getText().toString();
        }
        if(!textUserLastName.getText().toString().equals("")){
            editUser.LastName = textUserLastName.getText().toString();
        }
        if(!textUserPhone.getText().toString().equals("")){
            editUser.Phone = textUserPhone.getText().toString();
        }
        if(mAuth.getCurrentUser() != null) {
            if(!textUserEmail.getText().toString().equals("")
                    && textUserEmail.getText().toString().equals(mAuth.getCurrentUser().getEmail())){
                mAuth.getCurrentUser().updateEmail(textUserEmail.getText().toString());
            }
        }
        if(uploadUri != null){
            editUser.ProfileImagePath = uploadUri.toString();
        }

        changePath.setValue(editUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this,
                            "Данные профиля успешно изменены!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Ошибка! Данные профиля не были обновлены!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadImage(){
        imageIsLoading = true;
        if(imageProfilePicture.getDrawable() != null){
            Bitmap bitMap = ((BitmapDrawable) imageProfilePicture.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            final StorageReference imageRef =
                    ImageProfileStorageReference.child(System.currentTimeMillis()
                            + mAuth.getCurrentUser().getUid());
            UploadTask up = imageRef.putBytes(byteArrayImage);
            Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        uploadUri = task.getResult();
                        imageIsLoading = false;
                    }
                    else{
                        imageIsLoading = false;
                    }
                }
            });



        }
    }

    public void cancelOnClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        UserDataBase.removeEventListener(userValueEventListener);
        super.onDestroy();
    }
}
