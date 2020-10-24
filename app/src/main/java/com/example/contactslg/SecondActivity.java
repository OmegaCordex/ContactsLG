package com.example.contactslg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {

    TextInputEditText editAddCU;
    TextInputEditText editAddChv;
    TextInputEditText editAddPhone;
    TextInputLayout inputAddCu;
    private static final String KEY_TITLE = "TITLE";
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        editAddCU = findViewById(R.id.editAddCuId);
        editAddChv = findViewById(R.id.editAddChvId);
        editAddPhone = findViewById(R.id.editAddPhoneId);

        inputAddCu = findViewById(R.id.inputAddCuId);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void saveCUClick(View view) {

        String email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        String branchName = null;
        if (email != null) {
            branchName = email.substring(email.indexOf(".") + 1);
        }

        Toast.makeText(this, branchName, Toast.LENGTH_SHORT).show();

        String cuName = Objects.requireNonNull(editAddCU.getText()).toString().trim();
        String chvString = Objects.requireNonNull(editAddChv.getText()).toString().trim();
        String chvPhone = Objects.requireNonNull(editAddPhone.getText()).toString().trim();

        if (cuName.isEmpty()) {

            inputAddCu.setError("CU name is Mandatory");

        } else {

            inputAddCu.setErrorEnabled(false);


            CollectionReference collReference = FirebaseFirestore.getInstance().collection("branchName").document(Objects.requireNonNull(branchName)).collection("cuName").document(cuName).collection("details");

            collReference.add(new CommunityUnit(cuName, chvString, chvPhone));

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("branchName").document(branchName).collection("cuName").document(cuName);

            documentReference.set(new CommunityUnit(cuName));

//            Map<String, Object> note = new HashMap<>();
//
//            note.put(KEY_TITLE, cuName);
//
//            firebaseFirestore.collection(Objects.requireNonNull(branchName)).document(cuName).set(note)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                            Toast.makeText(SecondActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                            editAddCU.setText("");
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                    Toast.makeText(SecondActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });

        }

    }
}
