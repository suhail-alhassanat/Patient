package com.suhail.patient.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suhail.patient.databinding.ActivityRegisterBinding;
import com.suhail.patient.helper.Constants;
import com.suhail.patient.models.Patient;
import com.suhail.patient.models.UserCategory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    //define calender
    Calendar calendar;


    //define progress dialog
    ProgressDialog p;

    //define firebase features objects
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        noStatesBar();
        setContentView(binding.getRoot());

        //hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //initialize calender
        calendar = Calendar.getInstance();

        //initialize progress dialog
        p = new ProgressDialog(RegisterActivity.this);

        //initialize firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        //get date of birth from user
        DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                binding.txtDob.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.getTime()));
            }
        };

        binding.imgDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this, dob, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //calling add patient method on click create account button
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPatient();
            }
        });


    }

    public void addPatient() {

        p.setTitle("Create Account");
        p.setMessage("Please wait... ");
        p.setCanceledOnTouchOutside(false);
        p.show();

        String password = binding.edtPassword.getText().toString();
        String retypePassword = binding.edtRetypePassword.getText().toString();
        String address=binding.edtAddress.getText().toString();
        String name = binding.edtName.getText().toString();
        String email = binding.edtEmail.getText().toString();
        String phoneNo = binding.edtPhoneNo.getText().toString();
        String dob = binding.txtDob.getText().toString();

        if (name.equals("") || email.equals("") || phoneNo.equals("") || dob.equals("") || getGender().equals("")
        ||address.equals("")) {
            Toast.makeText(this, "kindly fill all filed", Toast.LENGTH_SHORT).show();
            p.dismiss();
        } else {

            if (!password.equals(retypePassword)) {
                Toast.makeText(this, "Retype password is incorrect", Toast.LENGTH_SHORT).show();
                p.dismiss();
             } else {
                //authentication create users
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Patient patient=new Patient(name,email,phoneNo,address,getGender(),dob);
                            firestore.collection(Constants.PATIENT_COLLECTION_NAME).document(patient.getEmail()).set(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "patient added successfully ", Toast.LENGTH_SHORT).show();
                                        addToCategoryCollection(new UserCategory(email,4));
                                        clearFailed();
                                        p.dismiss();
                                    }else
                                        Toast.makeText(RegisterActivity.this, "patient added failed", Toast.LENGTH_SHORT).show();
                                         p.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        } else
                            Toast.makeText(RegisterActivity.this, "Authentication failed ", Toast.LENGTH_SHORT).show();
                             p.dismiss();
                    }
                });
             }
        }


    }


    private void clearFailed() {
        binding.edtName.setText("");
        binding.edtEmail.setText("");
        binding.edtPhoneNo.setText("");
        binding.edtAddress.setText("");
        binding.rbFemale.setChecked(false);
        binding.rbMail.setChecked(true);
        binding.txtDob.setText("");
        binding.edtPassword.setText("");
        binding.edtRetypePassword.setText("");
    }


    public void noStatesBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public String getGender() {
        String gender;
        if (binding.rbFemale.isChecked()) {
            gender = "female";
        } else {
            gender = "male";
        }
        return gender;
    }

    private void addToCategoryCollection(UserCategory userCategory) {
        firestore.collection(Constants.CATEGORIES_COLLECTION_NAME).document(userCategory.getEmail()).set(userCategory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful()){
                 Toast.makeText(RegisterActivity.this, "patient added successfully to user category", Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(RegisterActivity.this,BottomNavigationActivity.class));
             }else {
                 Toast.makeText(RegisterActivity.this, "patient added failed to user category", Toast.LENGTH_SHORT).show();
             }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}