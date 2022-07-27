package com.suhail.patient.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.suhail.patient.databinding.ActivityLoginBinding;
import com.suhail.patient.helper.Constants;
import com.suhail.patient.models.Patient;

public class LoginActivity extends AppCompatActivity {
ActivityLoginBinding binding;
    //define firebase features objects
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    //define progress dialog
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        noStatesBar();
        setContentView(binding.getRoot());

        //hide action bar
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        //initialize firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //initialize progress dialog
        p = new ProgressDialog(LoginActivity.this);


        //go to register activity
        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


        //login
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            loginPatient();
            }
        });
    }




    public void noStatesBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void login() {


        p.setTitle("Create Account");
        p.setMessage("Please wait... ");
        p.setCanceledOnTouchOutside(false);
        p.show();

        String email = binding.edtEmail.getText().toString();
        String password = binding.edtPassword.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Kindly fill all failed", Toast.LENGTH_SHORT).show();
            p.dismiss();
        } else {
            firestore.collection(Constants.CATEGORIES_COLLECTION_NAME).whereEqualTo("type", 4).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                 if (value!=null&&error==null){
                     auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()){
                                 auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                     @Override
                                     public void onComplete(@NonNull Task<AuthResult> task) {
                                         if (task.isSuccessful()){
                                             startActivity(new Intent(LoginActivity.this,BottomNavigationActivity.class));
                                             p.dismiss();
                                         }else {
                                             Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                                             p.dismiss();
                                         }
                                         p.dismiss();


                                     }
                                 });
                             }
                         }
                     });

                 }else {
                     if (error!=null){
                         Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                     Toast.makeText(LoginActivity.this, "you are not patient", Toast.LENGTH_SHORT).show();
                     p.dismiss();
                 }
                }

            });

        }
        p.dismiss();
    }



    private void loginPatient(){

        p.setTitle("Create Account");
        p.setMessage("Please wait... ");
        p.setCanceledOnTouchOutside(false);
        p.show();

        String email = binding.edtEmail.getText().toString();
        String password = binding.edtPassword.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Kindly fill all failed", Toast.LENGTH_SHORT).show();
            p.dismiss();
        } else {
            firestore.collection(Constants.PATIENT_COLLECTION_NAME).document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                 Patient patient= task.getResult().toObject(Patient.class);
                 if (patient!=null){
                     auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()){
                                 startActivity(new Intent(LoginActivity.this,BottomNavigationActivity.class));
                                 p.dismiss();
                             }else
                                 Toast.makeText(LoginActivity.this, "login Failed", Toast.LENGTH_SHORT).show();
                             p.dismiss();
                         }
                     });
                 }else
                     Toast.makeText(LoginActivity.this, "you are not patient", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,BottomNavigationActivity.class));
        }
    }


}