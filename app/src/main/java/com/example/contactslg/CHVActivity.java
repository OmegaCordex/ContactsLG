package com.example.contactslg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class CHVActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    CollectionReference collectionReference;
    ChvAdapter chvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h_v);

        recyclerView = findViewById(R.id.recChvId);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String cuName = getIntent().getStringExtra("cuNameKey");

        String userEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        assert userEmail != null;
        String brName = userEmail.substring(userEmail.indexOf(".") + 1);


        collectionReference = firebaseFirestore.collection("branchName").document(brName).collection("cuName").document(cuName).collection("details");

        Query query = collectionReference.orderBy("chvName");

//        Query query = collectionReference.orderBy("chvName");

        FirestoreRecyclerOptions<CommunityUnit> options = new FirestoreRecyclerOptions.Builder<CommunityUnit>()
                .setQuery(query, CommunityUnit.class)
                .build();

        chvAdapter = new ChvAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chvAdapter);

        chvAdapter.setOnItemClickListener(new ChvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final DocumentSnapshot documentSnapshot, int position) {
//
//                CommunityUnit communityUnit = documentSnapshot.toObject(CommunityUnit.class);
//
//                Toast.makeText(CHVActivity.this, communityUnit.getChvName(), Toast.LENGTH_SHORT).show();

                PopupMenu popupMenu = new PopupMenu(CHVActivity.this, view);

                popupMenu.getMenuInflater().inflate(R.menu.pop_chv_menu, popupMenu.getMenu());

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String userEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                        assert userEmail != null;
                        final String brName = userEmail.substring(userEmail.indexOf(".") + 1);

                        final CommunityUnit communityUnit = documentSnapshot.toObject(CommunityUnit.class);

                        switch (item.getItemId()) {

                            case R.id.call:

                                break;
                            case R.id.sms:

                                break;
                            case R.id.edit_chv:

                                AlertDialog.Builder builder = new AlertDialog.Builder(CHVActivity.this);
                                View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom, null);

                                final TextInputEditText editName = dialogView.findViewById(R.id.editAddChvNameId);
                                final TextInputEditText editPhone = dialogView.findViewById(R.id.editAddChvPhoneId);


                                editName.setText(Objects.requireNonNull(communityUnit).getChvName());

                                editPhone.setText(communityUnit.getChvPhone());

                                Button button = dialogView.findViewById(R.id.buttonSaveChvEditId);

                                builder.setView(dialogView);

                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCanceledOnTouchOutside(true);


                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String chv_name = Objects.requireNonNull(editName.getText()).toString().trim();
                                        String chv_phone = Objects.requireNonNull(editPhone.getText()).toString().trim();

                                        DocumentReference documentReference = FirebaseFirestore.getInstance()
                                                .collection("branchName").document(brName).collection("cuName")
                                                .document(Objects.requireNonNull(communityUnit).getCuName()).collection("details")
                                                .document(documentSnapshot.getId());

                                        CommunityUnit cUnitt = new CommunityUnit(communityUnit.getCuName(), chv_name, chv_phone);

                                        documentReference.set(cUnitt);


                                    }
                                });

                                alertDialog.show();


                                break;
                            case R.id.delete_chv:

                                DocumentReference ddRef = FirebaseFirestore.getInstance().collection("branchName").document(brName)
                                        .collection("cuName").document(Objects.requireNonNull(communityUnit).getCuName())
                                        .collection("details").document(documentSnapshot.getId());

                                ddRef.delete();

                                //ddRef.update(documentSnapshot.getId(), FieldValue.delete());


                                break;
                        }

                        return true;
                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        chvAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        chvAdapter.stopListening();
    }
}
