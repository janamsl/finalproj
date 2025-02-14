package com.example.finalproj;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FoodDetail extends AppCompatActivity {

    private TextView foodNameText, foodDescriptionText, foodIngredientsText;
    private ImageView foodImageView;
    private DatabaseReference mFoodRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        foodNameText = findViewById(R.id.foodNameText);
        foodDescriptionText = findViewById(R.id.foodDescription);
        foodIngredientsText = findViewById(R.id.foodIngredientsText);
        foodImageView = findViewById(R.id.foodImageView);

        mFoodRef = FirebaseDatabase.getInstance().getReference("foods");

        int foodId = getIntent().getIntExtra("foodId", -1);

        if (foodId != -1) {
            loadFoodDetails(foodId);
        }
    }

    private void loadFoodDetails(int foodId) {
        mFoodRef.child(String.valueOf(foodId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String foodName = dataSnapshot.child("foodName").getValue(String.class);
                    String foodDescription = dataSnapshot.child("foodDescription").getValue(String.class);
                    String foodIngredients = dataSnapshot.child("foodIngredients").getValue(String.class);
                    String foodImage = dataSnapshot.child("foodImage").getValue(String.class);

                    foodNameText.setText(foodName);
                    foodDescriptionText.setText(foodDescription);
                    foodIngredientsText.setText(foodIngredients);

                    // Set image (Here you would use an image loading library like Glide or Picasso)
                    // foodImageView.setImageResource(foodImage);
                } else {
                    Toast.makeText(FoodDetail.this, "Food not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FoodDetail.this, "Error fetching food details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
