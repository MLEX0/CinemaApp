package com.kp.cinemaapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kp.cinemaapp.model.User;

public class RegistrationAuthorizationActivity extends AppCompatActivity {

    ConstraintLayout CLAuthToReg;
    ConstraintLayout CLAuth;
    ConstraintLayout CLReg;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database;

    DatabaseReference UserDataBase;

    private EditText textEmail;
    private EditText textPassword;

    private EditText textEmailReg;
    private EditText textPasswordReg;
    private EditText textPasswordReg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_authorization);
        Init();

        //Слушатель входа в приложение
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }

            }
        };

        //Если уже есть авторизованный пользователь, то переходим на главное окно
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
        {
            Intent auth = new Intent(RegistrationAuthorizationActivity.this, MainFrameActivity.class);
            startActivity(auth);
            finish();
        }
    }

    public void Init() {

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://cinemaapp-80774-default-rtdb.firebaseio.com/");
        UserDataBase = database.getReference(Constants.USER_KEY);


        CLAuthToReg = findViewById(R.id.clAuthOrReg);
        CLAuth = findViewById(R.id.clAuth);
        CLReg = findViewById(R.id.clRegistration);
        textEmail = findViewById(R.id.editTextTextEmailAddress);
        textPassword = findViewById(R.id.editTextTextPassword);
        textEmailReg = findViewById(R.id.editTextTextEmailAddressREG);
        textPasswordReg = findViewById(R.id.editTextTextPassWordREG);
        textPasswordReg2 = findViewById(R.id.editTextTextRepeatPassWordREG);
    }

    public void toAuthOnClick(View view){
        CLAuthToReg.setVisibility(View.GONE);
        CLAuth.setVisibility(View.VISIBLE);
        CLReg.setVisibility(View.GONE);
    }

    public void toRegOnClick(View view){
        CLAuthToReg.setVisibility(View.GONE);
        CLAuth.setVisibility(View.GONE);
        CLReg.setVisibility(View.VISIBLE);
    }

    public void skipOnClick(View view){
        Intent intent = new Intent(RegistrationAuthorizationActivity.this, MainFrameActivity.class);
        startActivity(intent);
        finish();
    }

    public void showPassOnClick(View view){
        View eyeClose = findViewById(R.id.imageViewEnterPasswordEye_close);
        View eyeOpen = findViewById(R.id.imageViewEnterPasswordEye_open);
        EditText pswBox = findViewById(R.id.editTextTextPassword);
        pswBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        eyeClose.setVisibility(View.GONE);
        eyeOpen.setVisibility(View.VISIBLE);
    }

    public void hidePassOnClick(View view){
        View eyeClose = findViewById(R.id.imageViewEnterPasswordEye_close);
        View eyeOpen = findViewById(R.id.imageViewEnterPasswordEye_open);
        EditText pswBox = findViewById(R.id.editTextTextPassword);
        pswBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eyeClose.setVisibility(View.VISIBLE);
        eyeOpen.setVisibility(View.GONE);
    }

    public void authOnClick(View view){
        String Email = textEmail.getText().toString();
        String Password = textPassword.getText().toString();

        if(Email.isEmpty()) {

            Toast.makeText(RegistrationAuthorizationActivity.this,
                    "Поле 'Email' не может быть пустым!", Toast.LENGTH_LONG).show();

        }
        else if (Password.isEmpty()) {
            Toast.makeText(RegistrationAuthorizationActivity.this,
                    "Поле 'Пароль' не может быть пустым!", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        Toast.makeText(RegistrationAuthorizationActivity.this,
                                "Email или пароль введены неверно!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegistrationAuthorizationActivity.this,
                                "Авторизация успешна!", Toast.LENGTH_LONG).show();

                        //Переход на новое окно при успешной авторизации
                        Intent intent = new Intent(RegistrationAuthorizationActivity.this, MainFrameActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    public void registrationClick(View view){
        String EmailReg = textEmailReg.getText().toString();
        String PasswordReg = textPasswordReg.getText().toString();
        String PasswordReg2 = textPasswordReg2.getText().toString();

        if(EmailReg.isEmpty()) {
            Toast.makeText(RegistrationAuthorizationActivity.this,
                    "Поле 'Email' не может быть пустым!", Toast.LENGTH_LONG).show();
        } else if (PasswordReg.isEmpty()) {
            Toast.makeText(RegistrationAuthorizationActivity.this,
                    "Поле 'Пароль' не может быть пустым!", Toast.LENGTH_LONG).show();

        } else if (!PasswordReg.equals(PasswordReg2)) {
            Toast.makeText(RegistrationAuthorizationActivity.this,
                    "Пароли не совпадают!", Toast.LENGTH_LONG).show();

        } else {

            mAuth.createUserWithEmailAndPassword(EmailReg, PasswordReg).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegistrationAuthorizationActivity.this,
                                "Регистрация провалена!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(RegistrationAuthorizationActivity.this,
                                "Регистрация успешна!", Toast.LENGTH_LONG).show();

                        mAuth.signInWithEmailAndPassword(EmailReg, PasswordReg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null)
                                    {
                                        User newUser = new User(UserDataBase.getKey(),user.getUid(), null, null, null, null, null);
                                        UserDataBase.push().setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    //Переход на новое окно при успешной регистрации
                                                    Intent intent_reg = new Intent(RegistrationAuthorizationActivity.this,MainFrameActivity.class);
                                                    startActivity(intent_reg);
                                                    finish();
                                                }
                                            }
                                        });

                                    }
                                }
                            }
                        });

                    }
                }
            });
        }
    }

}
