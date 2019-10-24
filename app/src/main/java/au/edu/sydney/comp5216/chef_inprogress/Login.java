package au.edu.sydney.comp5216.chef_inprogress;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Login activity is started when the application is launched
 */
public class Login extends AppCompatActivity {

    public static final String TAG = "EmailPassword";

    private Button btnSignIn;
    private Button btnReg;
    private TextInputEditText txtAemail;
    private TextInputEditText txtApassword;

    private LinearLayout loginForm, registrationForm;
    private Button registerBtn, gobackBtn;
    private TextInputEditText reg_username, reg_email, reg_pw, reg_repw;

    private String login_email, login_password;
    private String reg_usernameStr, reg_emailStr, reg_passwordStr, reg_passwordConStr;

    private FirebaseAuth mAuth;
    private User newUser;

    /**
     * Create view of login page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        txtAemail = (TextInputEditText) findViewById(R.id.aEmail);
        txtApassword = (TextInputEditText) findViewById(R.id.aPassword);
        btnSignIn = (Button) findViewById(R.id.aSignIn);
        btnReg = (Button) findViewById(R.id.aSignUp);
        gobackBtn = (Button) findViewById(R.id.back_btn);

        loginForm = findViewById(R.id.login_form);
        registrationForm = findViewById(R.id.registration_form);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            /**
             * If sign in button is clicked, get text and run signin method
             * @param view
             */
            @Override
            public void onClick(View view) {
                login_email = txtAemail.getEditableText().toString();
                login_password = txtApassword.getEditableText().toString();
                signIn(login_email, login_password);
            }
        });

        // Registration view
        reg_username = findViewById(R.id.rUsername);
        reg_email = findViewById(R.id.rEmail);
        reg_pw = findViewById(R.id.rPassword);
        reg_repw = findViewById(R.id.rConfirmation);

        btnReg.setOnClickListener(new View.OnClickListener() {
            /**
             * If "create new account" button is clicked, update UI to display registration form
             * @param view
             */
            @Override
            public void onClick(View view) {
                loginForm.setVisibility(View.GONE);
                registrationForm.setVisibility(View.VISIBLE);
                gobackBtn.setVisibility(View.VISIBLE);
            }
        });

        gobackBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * If go back button is clicked, update UI to display login form
             * @param view
             */
            @Override
            public void onClick(View view) {
                loginForm.setVisibility(View.VISIBLE);
                registrationForm.setVisibility(View.GONE);
                gobackBtn.setVisibility(View.GONE);
            }
        });

        registerBtn = findViewById(R.id.rSignUp);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * If "REGISTER" button is clicked, get all input and run createAccount method
             * @param view
             */
            @Override
            public void onClick(View view) {
                reg_emailStr = reg_email.getEditableText().toString();
                reg_passwordStr = reg_pw.getEditableText().toString();
                reg_passwordConStr = reg_repw.getEditableText().toString();
                reg_usernameStr = reg_username.getEditableText().toString();

                createAccount(reg_usernameStr, reg_emailStr, reg_passwordStr, reg_passwordConStr);
            }
        });
    }

    /**
     * onStart, check if user is already in session, then update UI
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * Create new account with Firebase authentication method
     * @param username
     * @param email
     * @param password
     * @param confirmation
     */
    private void createAccount(final String username, final String email, String password, String confirmation) {
        if (!validateForm(email, password, confirmation)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    /**
                     * Triggered when creating user on Firebase task is completed
                     * @param task
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "Your account has been registered successfully!!",
                                    Toast.LENGTH_LONG).show();
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            // Create initial dummy values for user
                            ArrayList<String> inventory = new ArrayList<>();
                            inventory.add("Apple");
                            ArrayList<String> shoppinglist = new ArrayList<>();
                            shoppinglist.add("Shopping List Example");
                            ArrayList<Integer> shoppinglistCheck = new ArrayList<>();
                            shoppinglistCheck.add(0);
                            ArrayList<String> completedrecipe = new ArrayList<>();
                            completedrecipe.add("None");
                            ArrayList<String> completeddate = new ArrayList<>();
                            completeddate.add("Null");
                            ArrayList<String> favorites = new ArrayList<>();
                            favorites.add("Null");
                            newUser = new User(username, email, inventory, shoppinglist, shoppinglistCheck, completedrecipe, completeddate, favorites);

                            // Add user to Firebase realtime database
                            new FirebaseDatabaseHelper("user").addNewUser(newUser, new FirebaseDatabaseHelper.DataStatus() {
                                @Override
                                public void DataisLoaded(List<User> users, List<String> keys) {}

                                /**
                                 * Once data is inserted successfully, update UI to home page
                                 * @param addUser
                                 * @param key
                                 */
                                @Override
                                public void DataIsInserted(User addUser, String key) {
                                    Log.d("INSERT SUCCESSFULLY", "TO FIREBASE");
                                    updateUI(user);
                                }

                                @Override
                                public void DataIsUpdated() {}

                                @Override
                                public void DataIsDeleted() {}
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed. Your email has already existed.",
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Sign in method of Firebase authentication
     * @param email
     * @param password
     */
    private void signIn(final String email, String password) {
        if (!validateLoginForm(email, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    /**
                     * Triggered when sign in task is completed
                     * Run updateUI method
                     * @param task
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(Login.this, "Signed in successfully!!",
                                    Toast.LENGTH_SHORT).show();
                            final FirebaseUser fbUser = mAuth.getCurrentUser();
                            updateUI(fbUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed. Your email or password may be incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Validate registration input form
     * Check if all input are filled in, check password length
     * @param email
     * @param password
     * @param confirmPassword
     * @return
     */
    private boolean validateForm(String email, String password, String confirmPassword) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "We need your email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "You are safe. We don't share your password with anyone :)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(Login.this, "Passwords does not match. Try again", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(Login.this, "Password is too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        return valid;
    }

    /**
     * Validate login input form, check if all input are filled in
     * @param email
     * @param password
     * @return
     */
    private boolean validateLoginForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "We need your email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "You are safe. We don't share your password with anyone :)", Toast.LENGTH_SHORT).show();
            return false;
        }

        return valid;
    }

    /**
     * Update UI based on the user session
     * If user is in session, start MainActivity
     * If not, display Login form
     * @param user
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            loginForm.setVisibility(View.VISIBLE);
            registrationForm.setVisibility(View.GONE);
            gobackBtn.setVisibility(View.GONE);
        }
    }


}
