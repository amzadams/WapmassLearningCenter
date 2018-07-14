package wapmass.wapmasslearningcenter.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import wapmass.wapmasslearningcenter.R;

public class RegisterActivity  extends AppCompatActivity {

    private Button register;
    private SharedPreferences sharedPreferences;
    private EditText email, password;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private RelativeLayout layout;
    private ProgressBar mProgressBar;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = findViewById(R.id.regLayout);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.btnRegister);
        textInputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        mAuth = FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.progressBar);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(servicesOK()) {
                    validateAndRegister(v);
                }
            }
        });

        setupToolbar();

        hideSoftKeyboard();
    }

    private void addLoginData(){
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("stuName", "student");
        editor.putString("stuPass", "student");
        editor.apply();

    }

    public boolean validateAndRegister(View v){
        if(validateUsername() && validatePassword()){
            createNewAccount(v);
            return true;
        }else{
            return false;
        }

    }

    private boolean validateUsername() {
        if(email.getText().toString().isEmpty()){
            textInputLayoutEmail.setError("Email cannot be blank");
            return false;
        }else{
            textInputLayoutEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        if(password.getText().toString().isEmpty()){
            textInputLayoutPassword.setError("Password cannot be blank");
            return false;
        }else{
            textInputLayoutPassword.setErrorEnabled(false);
            return true;
        }
    }
    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WapMass Learning Center");
        toolbar.setSubtitle("Register");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    private void createNewAccount(final View view) {
        showDialog();
        String em = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        if (!TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd)) {
            mAuth.createUserWithEmailAndPassword(em, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult != null) {
                                hideDialog();
                                sendVerificationEmail();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideDialog();
                    Snackbar snackbar = Snackbar.make(view,e.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                        }
                    });
                    snackbar.show();
                }
            });

        }
    }

    public boolean servicesOK(){

        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(RegisterActivity.this);

        if(isAvailable == ConnectionResult.SUCCESS){
            //everything is ok and the user can make mapping requests
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)){
            //an error occured, but it's resolvable
            Toast.makeText(this, "Can't connect to mapping services", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Can't connect to mapping services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * sends an email verification link to the user
     */
    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sent Verification Email! Get Verify before login", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage()+ "! Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}

