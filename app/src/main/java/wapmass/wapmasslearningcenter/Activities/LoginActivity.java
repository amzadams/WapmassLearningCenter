package wapmass.wapmasslearningcenter.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import wapmass.wapmasslearningcenter.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button login;
    private SharedPreferences sharedPreferences;
    private EditText email, password;
    private TextView register;
    private Toolbar toolbar;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private ProgressBar mProgressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.btnLogin);
        textInputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WapMass Learning Center");
        toolbar.setSubtitle("Login");
        register = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(servicesOK())validateAndLogin(email.getText().toString(), password.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        hideSoftKeyboard();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            String email = user.getEmail();
            if(user.isEmailVerified()){
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }else{
                Toast.makeText(LoginActivity.this, "Please Make sure your email is verified ", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Get Authenticated by Registering or logining ", Toast.LENGTH_LONG).show();
        }

    }

    private void addLoginData(){
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("stuName", "student");
        editor.putString("stuPass", "student");
        editor.apply();

    }

    public boolean validateAndLogin(String email, String password){
        if(validateUsername() && validatePassword()){
            login(email, password);
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
    private void login(String email, String password) {
        showDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                if(currentUser.isEmailVerified()) {
                                    hideDialog();
                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                    finish();
                                }else{
                                    hideDialog();
                                    Toast.makeText(LoginActivity.this, "Please Make sure your email is verified ", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MainActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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


    public boolean servicesOK(){

        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);

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

}