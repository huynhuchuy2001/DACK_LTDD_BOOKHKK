package com.example.bookcave;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccountCustomer extends AppCompatActivity {

    private EditText email,password,firstname,lastname,phno,address,pincode;
    private String semail,spassword,sfirstname,slastname,sphno,saddress,spincode,userid;
    private Button createaccButton;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_customer);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        phno = findViewById(R.id.phno);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        createaccButton = findViewById(R.id.createaccButton);

        createaccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrive the strings
                semail= email.getText().toString().trim();
                spassword= password.getText().toString().trim();
                sfirstname= firstname.getText().toString().trim();
                slastname= lastname.getText().toString().trim();
                sphno= phno.getText().toString().trim();
                saddress= address.getText().toString().trim();
                spincode= pincode.getText().toString().trim();

                //Check for empty input
                if(TextUtils.isEmpty(semail)){
                    Toast.makeText(getApplicationContext(),"Vui l??ng nh???p email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(spassword)){
                    Toast.makeText(getApplicationContext(),"Xin vui l??ng nh???p m???t kh???u",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(sfirstname)){
                    Toast.makeText(getApplicationContext(),"Vui l??ng nh???p t??n c???a b???n",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(slastname)){
                    Toast.makeText(getApplicationContext(),"Vui l??ng nh???p H??? c???a b???n",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(sphno)){
                    Toast.makeText(getApplicationContext(),"Xin vui l??ng ??i???n s??? ??i???n tho???i c???a b???n",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(saddress)){
                    Toast.makeText(getApplicationContext(),"Vui l??ng nh???p ?????a ch??? c???a b???n",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(spincode)){
                    Toast.makeText(getApplicationContext(),"Vui l??ng nh???p m?? pin c???a b???n",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sphno.length()!=10){
                    phno.setError("S??? ph???i c?? 10 ch??? s???");
                    return;
                }
                if(spincode.length()!=6){
                    pincode.setError("M?? pin ph???i c?? 6 ch??? s???");
                    return;
                }

                //Save Data to Firestore
                //saveData(semail,spassword,sfirstname,slastname,sphno,saddress,spincode);
                fAuth = FirebaseAuth.getInstance();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                createaccButton.setText("??ang t???o ...");
                fAuth.createUserWithEmailAndPassword(semail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("userid", userid);
                            user.put("email", semail);
                            user.put("firstname", sfirstname);
                            user.put("lastname", slastname);
                            user.put("phno", sphno);
                            user.put("address", saddress);
                            user.put("pincode", spincode);
                            user.put("usertype", "Customer");

                            //NEW
                            db.collection("Users").document(userid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        createaccButton.setText("Create Account");
                                        Toast.makeText(CreateAccountCustomer.this,"????ng k?? th??nh c??ng! Ch??o m???ng ?????n v???i Book HKK Team", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent i = new Intent(CreateAccountCustomer.this, CustomerLogin.class);
                                        startActivity(i);
                                    } else{
                                        createaccButton.setText("Create Account");
                                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(CreateAccountCustomer.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            //OLD
                            //Users info = new Users(fullName,email,phone,userid); class with getter setter

                        }else {
                            Toast.makeText(CreateAccountCustomer.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    /*
    private void saveData(String semail,String spassword,String sfirstname,String slastname,String sphno,String saddress,String spincode){


        //New User
        Map<String, Object> user = new HashMap<>();
        user.put("first", semail);
        user.put("last", ");
        user.put("born", 1815);
    } */
}
