package com.example.finalproj;

import android.content.Intent;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodNamesActivity extends AppCompatActivity {

    private ListView foodListView;
    private DatabaseReference mFoodRef;
    private ArrayList<String> foodNames;
    private ArrayList<String> foodIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_names);

        foodListView = findViewById(R.id.foodListView);
        mFoodRef = FirebaseDatabase.getInstance().getReference("foods");
        foodNames = new ArrayList<>();
        foodIds = new ArrayList<>();

        loadFoodItems();

        foodListView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the foodId of the selected item
            String selectedFoodId = foodIds.get(position);
            Intent intent = new Intent(FoodNamesActivity.this, FoodDetail.class);
            intent.putExtra("foodId", selectedFoodId);
            startActivity(intent);
        });
    }

    private void loadFoodItems() {
        mFoodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodNames.clear();
                foodIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String foodName = snapshot.child("foodName").getValue(String.class);
                    String foodId = snapshot.getKey(); // Firebase key used as unique ID
                    foodNames.add(foodName);
                    foodIds.add(foodId);
                }

                // Use a simple ArrayAdapter to populate the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FoodNamesActivity.this, android.R.layout.simple_list_item_1, foodNames);
                foodListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FoodNamesActivity.this, "Error loading food items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
