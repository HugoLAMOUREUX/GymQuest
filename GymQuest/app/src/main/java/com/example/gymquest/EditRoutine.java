package com.example.gymquest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;


public class EditRoutine extends AppCompatActivity {
    private Button Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday;
    private TextView editRoutineCurrentRoutine;
    private ListView editRoutineList;
    String day="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);
        Monday=(Button) findViewById(R.id.editRoutineMonday);
        Tuesday=(Button)findViewById(R.id.editRoutineTuesday);
        Wednesday=(Button)findViewById(R.id.editRoutineWednesday);
        Thursday=(Button)findViewById(R.id.editRoutineThursday);
        Friday=(Button)findViewById(R.id.editRoutineFriday);
        Saturday=(Button)findViewById(R.id.editRoutineSaturday);
        Sunday=(Button)findViewById(R.id.editRoutineSunday);
        editRoutineCurrentRoutine=(TextView)findViewById(R.id.editRoutineCurrentRoutine);
        editRoutineList=(ListView)findViewById(R.id.editRoutineList);


        //show all the routines available
        DatabaseReference routinesRef = FirebaseDatabase.getInstance().getReference().child("routines");


        routinesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> routineTitles = new ArrayList<>(); // Create a list to store routine titles

                for (DataSnapshot routineSnapshot : snapshot.getChildren()) { // Iterate over each routine snapshot in the data snapshot
                    String title = routineSnapshot.getKey(); // Get the value of the "title" field
                    routineTitles.add(title); // Add the title to the list of routine titles
                }

                // Create a ListView adapter with the list of routine titles
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditRoutine.this, android.R.layout.simple_list_item_1, routineTitles);
                editRoutineList.setAdapter(adapter); // Set the adapter to your ListView

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error retrieving routines: " + error.getMessage());
            }
        });






        Monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRoutineForDay("monday");
                day="monday";
                //Toast.makeText(editRoutine.this, "Selected day: " + day, Toast.LENGTH_SHORT).show();

            }
        });
        Tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRoutineForDay("tuesday");

                day="tuesday";
            }
        });
        Wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRoutineForDay("wednesday");

                day="wednesday";
            }
        });
        Thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRoutineForDay("thursday");

                day="thursday";
            }
        });
        Friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRoutineForDay("friday");

                day="friday";
            }
        });
        Saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRoutineForDay("saturday");
                day="saturday";
            }
        });
        Sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRoutineForDay("sunday");

                day="sunday";
            }
        });


        editRoutineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected routine title from the ArrayAdapter
                String selectedRoutine =parent.getItemAtPosition(position).toString();
                //Toast.makeText(editRoutine.this, "Selected routine: " + selectedRoutine, Toast.LENGTH_SHORT).show();

                updateRoutineForDay(day, selectedRoutine);



            }
        });


    }
    private void displayRoutineForDay(String day) {
        // Show the user's routines
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("user-day");
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Query query = userRef.orderByChild("email").equalTo(currentUserEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String currentRoutine = (String) userSnapshot.child(day.toLowerCase()).getValue();
                    editRoutineCurrentRoutine.setText(currentRoutine);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error retrieving user-day: " + databaseError.getMessage());
            }
        });
    }

    private void updateRoutineForDay(String day, String selectedRoutine) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userDayRef = database.getReference("user-day");
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Query query = userDayRef.orderByChild("email").equalTo(currentUserEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference userDayRefToUpdate = snapshot.getRef();
                    if(!selectedRoutine.isEmpty() && !day.isEmpty() ){
                        userDayRefToUpdate.child(day).setValue(selectedRoutine);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
            }
        });
    }




}