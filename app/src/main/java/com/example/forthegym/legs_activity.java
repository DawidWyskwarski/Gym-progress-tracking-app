package com.example.forthegym;

import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class legs_activity extends AppCompatActivity {

    private ArrayList<Exercises> exercises = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView searchView;
    private exercise_RecycleViewAdapter adapter;
    private FloatingActionButton addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_legs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        recyclerView = findViewById(R.id.mRecyclerView);

        addNew = findViewById(R.id.addNew);

        String[] eq = getResources().getStringArray(R.array.types_of_equipment);

        readFile();

        //opening app for the first time
        if(exercises.isEmpty()) {
            exercises.add(new Exercises("Squat (BB)"));
            exercises.add(new Exercises("Leg Extension (M)"));
            exercises.add(new Exercises("Leg Press (M)"));
            exercises.add(new Exercises("Lunges (BB)"));
            exercises.add(new Exercises("Bulgarian Split Squat (DB)"));
            exercises.add(new Exercises("Hack Squat (M)"));
            exercises.add(new Exercises("Pistol Squat (BW)"));
            exercises.add(new Exercises("Sissy Squat (BW)"));
            exercises.add(new Exercises("Hamstring Curl (M)"));
            exercises.add(new Exercises("Good Morning (BB)"));
            exercises.add(new Exercises("RDL (BB)"));
            exercises.add(new Exercises("Hip Thrust (BB)"));
            exercises.add(new Exercises("Standing Calf Raises (M)"));
            exercises.add(new Exercises("Seated Calf Raises (M)"));
            exercises.add(new Exercises("Calf Raises on a Leg Press (M)"));

            writeFile();
        }

        adapter = new exercise_RecycleViewAdapter(exercises,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToAdd(eq);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeFile();
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeFile();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        writeFile();
    }

    private void readFile() {
        File file = new File(getFilesDir(), "legs_exercises.ser");
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Object o = input.readObject();
                    exercises.add((Exercises) o);
                } catch (EOFException | ClassNotFoundException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(){

        File file = new File(getFilesDir(), "legs_exercises.ser");

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))){

            for(Exercises o: exercises){
                output.writeObject(o);
            }


        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void filterList(String text){
        ArrayList<Exercises> filteredList = new ArrayList<>();

        for(Exercises e: exercises){
            if(e.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(e);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this,"No exercises found", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setFilteredList(filteredList);
        }
    }

    private void showDialogToAdd(String[] typesOfEquipment){

        Dialog add_window = new Dialog(this);
        add_window.setContentView(R.layout.add_exercise);
        add_window.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText name = add_window.findViewById(R.id.new_name);
        Spinner eqChoice = add_window.findViewById(R.id.eqChoice);
        Button newAdd = add_window.findViewById(R.id.newAdd);

        ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typesOfEquipment);
        choiceAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        eqChoice.setAdapter(choiceAdapter);

        add_window.show();

        newAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name.getText().toString();
                String selectedItem = eqChoice.getSelectedItem().toString();

                switch (selectedItem){
                    case "Barbell (BB)":
                        newName += " (BB)";
                        break;

                    case "Dumbbell (DB)":
                        newName += " (DB)";
                        break;

                    case "Machine (M)":
                        newName += " (M)";
                        break;

                    case "Cables (C)":
                        newName += " (C)";
                        break;

                    case "Body Weight (BW)":
                        newName += " (BW)";
                        break;
                }

                Exercises newEx = new Exercises(newName);

                exercises.add(newEx);

                add_window.dismiss();
                filterList("");
            }
        });
    }
}