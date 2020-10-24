package com.example.contactslg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CuAddActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    CuAdapter cuAdapter;
    ArrayList<CommunityUnit> arrayList;
    TestCuAdapter testCuAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu_add);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recCuId);
        firebaseAuth = FirebaseAuth.getInstance();
        arrayList = new ArrayList<>();

        String userEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        assert userEmail != null;
        final String brName = userEmail.substring(userEmail.indexOf(".") + 1);


        collectionReference = firebaseFirestore.collection("branchName").document(brName).collection("cuName");
//        documentReference = firebaseFirestore.collection(brName).document();

//        DocumentReference documentReference = collectionReference.document("cuName");
//
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });

        Query query = collectionReference.orderBy("cuName");

        FirestoreRecyclerOptions<CommunityUnit> options = new FirestoreRecyclerOptions.Builder<CommunityUnit>()
                .setQuery(query, CommunityUnit.class)
                .build();

        cuAdapter = new CuAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cuAdapter);

        cuAdapter.setOnItemClickListener(new CuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final DocumentSnapshot documentSnapshot, int position) {

                final CommunityUnit communityUnit = documentSnapshot.toObject(CommunityUnit.class);

                PopupMenu popupMenu = new PopupMenu(CuAddActivity.this, view);

                popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.details:

                                Intent intent = new Intent(CuAddActivity.this, CHVActivity.class);

                                String cuName = Objects.requireNonNull(communityUnit).getCuName();

                                intent.putExtra("cuNameKey", cuName);

                                startActivity(intent);

                                break;
                            case R.id.edit:

                                AlertDialog.Builder builder = new AlertDialog.Builder(CuAddActivity.this);
                                builder.setTitle("Edit " + Objects.requireNonNull(communityUnit).getCuName());

                                final EditText editText = new EditText(CuAddActivity.this);

                                editText.setText(communityUnit.getCuName());
                                editText.setSelection(editText.getText().length());

                                builder.setView(editText);

                                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                        final DocumentReference documentR = FirebaseFirestore.getInstance()
//                                                .collection("branchName")
//                                                .document(brName)
//                                                .collection("cuName")
//                                                .document(communityUnit.getCuName())
//                                                .collection("details")
//                                                .document(documentSnapshot.getId());
//
//                                        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
//
//
//                                            @Nullable
//                                            @Override
//                                            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//
//                                                DocumentSnapshot snap = transaction.get(documentR);
//
//                                                String newName = snap.getString(editText.getText().toString().trim());
//
//                                                transaction.update(documentR, "cuName", newName);
//
//                                                return null;
//                                            }
//                                        });


                                        FirebaseFirestore.getInstance().collection("branchName").document(brName)
                                                .collection("cuName").document(communityUnit.getCuName())
                                                .collection("details")
                                                .whereEqualTo("cuName", "Amagoro")
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();


                                                        DocumentReference documentR = FirebaseFirestore.getInstance()
                                                                .collection("branchName")
                                                                .document(brName)
                                                                .collection("cuName")
                                                                .document(communityUnit.getCuName())
                                                                .collection("details")
                                                                .document(documentSnapshot.getId());


                                                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();

                                                        for (DocumentSnapshot snapshot : snapshotList) {

//                                                            Map<String, Object> map = new HashMap<>();
//                                                            map.put("cuName", editText.getText().toString().trim());

//                                                            writeBatch.update(documentR, "cuName", editText.getText().toString().trim());

                                                            writeBatch.update(documentR, (String) Objects.requireNonNull(snapshot.get("cuName")), editText.getText().toString().trim());

                                                        }

                                                        writeBatch.commit();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        Toast.makeText(CuAddActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                builder.show();

                                break;
                            case R.id.delete:

                                String cuName1 = Objects.requireNonNull(communityUnit).getCuName();

                                String userEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                                assert userEmail != null;
                                final String brName = userEmail.substring(userEmail.indexOf(".") + 1);


                                DocumentReference ddRef = FirebaseFirestore.getInstance().collection("branchName")
                                        .document(brName).collection("cuName").document(documentSnapshot.getId());

                                ddRef.delete();

                                break;

                        }


                        return true;
                    }
                });

            }
        });


//        firebaseFirestore.collection("branchName").document(brName).collection("cuName")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//
//                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
//
////                            CommunityUnit communityUnit = new CommunityUnit(documentSnapshot.getString("cuName"));
//                            CommunityUnit communityUnit = new CommunityUnit(documentSnapshot.getString("cuName"));
//
//
//                            //Toast.makeText(CuAddActivity.this, "gooooooooood", Toast.LENGTH_SHORT).show();
//                            //Toast.makeText(CuAddActivity.this, documentSnapshot.getId(), Toast.LENGTH_SHORT).show();
//                            arrayList.add(communityUnit);
//
//
//                        }
//
//                        testCuAdapter = new TestCuAdapter(CuAddActivity.this, arrayList, CuAddActivity.this);
//                        recyclerView.setAdapter(testCuAdapter);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(CuAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        cuAdapter.setOnItemClickListener(new CuAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                Toast.makeText(CuAddActivity.this, "Position "+ position, Toast.LENGTH_SHORT).show();
//
//                startActivity(new Intent(CuAddActivity.this, CHVActivity.class));
//            }
//        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(CuAddActivity.this, SecondActivity.class));

            }
        });
    }

//    @Override
//    public void onItemClick(View view, final int position) {
//
//        Toast.makeText(this, "Click Works" + position, Toast.LENGTH_SHORT).show();
//
//        PopupMenu popupMenu = new PopupMenu(CuAddActivity.this, view);
//        popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
//
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                switch (item.getItemId()) {
//
//                    case R.id.details:
//
//                        Intent intent = new Intent(CuAddActivity.this, CHVActivity.class);
//
//                        String cuName = arrayList.get(position).getCuName();
//
//                        intent.putExtra("cuNameKey", cuName);
//
//                        startActivity(intent);
//
//                        break;
//
//                    case R.id.edit:
//
////                        AlertDialog.Builder builder = new AlertDialog.Builder(CuAddActivity.this);
////                        builder.setTitle("Edit " + arrayList.get(position).getCuName());
////
////                        final EditText editText = new EditText(CuAddActivity.this);
////
////                        editText.setText(arrayList.get(position).getCuName());
////                        editText.setSelection(editText.getText().length());
////

////
////
////                        builder.setView(editText);
////
////                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////
////                                String newName = editText.getText().toString().trim();
////                                String chvName = arrayList.get(position).getChvName();
////                                String chvPhone = arrayList.get(position).getChvPhone();
////
////
////                                String userEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
////
////                                assert userEmail != null;
////                                final String brName = userEmail.substring(userEmail.indexOf(".") + 1);
////
////                                CollectionReference collReference = FirebaseFirestore.getInstance().collection("branchName")
////                                .document(Objects.requireNonNull(brName)).collection("cuName").document(arrayList.get(position)
////                                .getCuName()).collection("details");
////
////                                collReference.add(new CommunityUnit(newName, chvName, chvPhone));
////
////
////                                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("branchName")
////                                .document(brName).collection("cuName").document(arrayList.get(position).getCuName());
////
////                                documentReference.set(new CommunityUnit(newName));
////
////                            }
////                        });
////                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                dialog.dismiss();
////                            }
////                        });
////
////                        builder.show();
////
////                        break;
////
//                    case R.id.delete:
//
//                        String cuName1 = arrayList.get(position).getCuName();
//
//                        String userEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
//
//                        assert userEmail != null;
//                        final String brName = userEmail.substring(userEmail.indexOf(".") + 1);
//
//
//                        DocumentReference ddRef = FirebaseFirestore.getInstance().collection("branchName")
//                        .document(brName).collection("cuName").document(cuName1);
//
//                        ddRef.delete();
//
//                        break;
//                }
//
//                return true;
//            }
//        });
//
//        popupMenu.show();
//
//    }

    @Override
    protected void onStart() {
        super.onStart();

        cuAdapter.startListening();
    }


//    @Override
//    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//        String id = documentSnapshot.getId();
//
//        CommunityUnit comUnit = documentSnapshot.toObject(CommunityUnit.class);
//
//        String commUnitName = Objects.requireNonNull(comUnit).getCuName();
//
//        Toast.makeText(this, "Position "+ position, Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onStop() {
        super.onStop();

        cuAdapter.stopListening();

    }

//    @Override
//    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//
//        Toast.makeText(this, "Position "+ position, Toast.LENGTH_SHORT).show();
//    }
}
