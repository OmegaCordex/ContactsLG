package com.example.contactslg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextInputEditText editEmail, editPass;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            startActivity(new Intent(this, CuAddActivity.class));

        }

        editEmail = findViewById(R.id.editEmailId);
        editPass = findViewById(R.id.editPassId);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarAuth);

    }

    public void loginClick(View view) {

        progressBar.setVisibility(View.VISIBLE);

        String username = Objects.requireNonNull(editEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(editPass.getText()).toString().trim();

        final String branchName = username.substring(username.indexOf(".") + 1);

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(MainActivity.this, CuAddActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("branchKey", branchName);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
