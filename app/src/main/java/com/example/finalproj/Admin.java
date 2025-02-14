package com.example.finalproj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Admin extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private EditText etID, etName, etDescription, etIngredients;
    private Button btnAddFood, btnUpdateFood, btnDeleteFood, btnSelectAll, btnSelectImage;
    private ImageView ivFoodImage;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mDatabase = FirebaseDatabase.getInstance().getReference("foods");
        mStorage = FirebaseStorage.getInstance().getReference("food_images");

        etID = findViewById(R.id.etID);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etIngredients = findViewById(R.id.etIngredients);
        ivFoodImage = findViewById(R.id.ivFoodImage);

        btnAddFood = findViewById(R.id.btnAddFood);
        btnUpdateFood = findViewById(R.id.btnUpdateFood);
        btnDeleteFood = findViewById(R.id.btnDeleteFood);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        btnAddFood.setOnClickListener(v -> addFood());
        btnUpdateFood.setOnClickListener(v -> updateFood());
        btnDeleteFood.setOnClickListener(v -> deleteFood());
        btnSelectAll.setOnClickListener(v -> selectAllFood());
        btnSelectImage.setOnClickListener(v -> selectImage());
    }

    private void addFood() {
        String foodID = etID.getText().toString();
        String foodName = etName.getText().toString();
        String foodDescription = etDescription.getText().toString();
        String foodIngredients = etIngredients.getText().toString();

        if (TextUtils.isEmpty(foodID) || TextUtils.isEmpty(foodName) || TextUtils.isEmpty(foodDescription) || TextUtils.isEmpty(foodIngredients)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload the image to Firebase Storage
        StorageReference imageRef = mStorage.child(foodID + ".jpg");
        imageRef.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    // Create a map of food data
                    Map<String, Object> foodData = new HashMap<>();
                    foodData.put("foodName", foodName);
                    foodData.put("foodDescription", foodDescription);
                    foodData.put("foodIngredients", foodIngredients);
                    foodData.put("foodImage", imageUrl);

                    // Store the food data in Firebase Realtime Database
                    mDatabase.child(foodID).setValue(foodData).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(Admin.this, "Food added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Admin.this, "Failed to add food", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } else {
                Toast.makeText(Admin.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFood() {
        String foodID = etID.getText().toString();
        String foodName = etName.getText().toString();
        String foodDescription = etDescription.getText().toString();
        String foodIngredients = etIngredients.getText().toString();

        if (TextUtils.isEmpty(foodID) || TextUtils.isEmpty(foodName) || TextUtils.isEmpty(foodDescription) || TextUtils.isEmpty(foodIngredients)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // If an image is selected, upload the new image
        if (imageUri != null) {
            StorageReference imageRef = mStorage.child(foodID + ".jpg");
            imageRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        updateFoodData(foodID, foodName, foodDescription, foodIngredients, imageUrl);
                    });
                } else {
                    Toast.makeText(Admin.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateFoodData(foodID, foodName, foodDescription, foodIngredients, null);
        }
    }

    private void updateFoodData(String foodID, String foodName, String foodDescription, String foodIngredients, String imageUrl) {
        // Update food data
        Map<String, Object> updatedFoodData = new HashMap<>();
        updatedFoodData.put("foodName", foodName);
        updatedFoodData.put("foodDescription", foodDescription);
        updatedFoodData.put("foodIngredients", foodIngredients);
        if (imageUrl != null) {
            updatedFoodData.put("foodImage", imageUrl);
        }

        mDatabase.child(foodID).updateChildren(updatedFoodData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Admin.this, "Food updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Admin.this, "Failed to update food", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFood() {
        String foodID = etID.getText().toString();
        if (TextUtils.isEmpty(foodID)) {
            Toast.makeText(this, "Please enter a food ID", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.child(foodID).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Admin.this, "Food deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Admin.this, "Failed to delete food", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectAllFood() {
        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                StringBuilder foods = new StringBuilder();
                for (com.google.firebase.database.DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    String foodID = foodSnapshot.getKey();
                    String foodName = foodSnapshot.child("foodName").getValue(String.class);
                    foods.append("ID: ").append(foodID).append(", Name: ").append(foodName).append("\n");
                }
                // Display all food items (could be in a TextView or Toast)
                Toast.makeText(Admin.this, foods.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Toast.makeText(Admin.this, "Failed to load food items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivFoodImage.setImageURI(imageUri);
        }
    }
}

