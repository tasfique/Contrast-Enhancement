package com.csci366_2020.tasfique_enam.contrastenhancement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button_SelectGallery);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); //help us select any image.

                startActivityForResult(Intent.createChooser(intent, "Select from Gallery"), 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 1) {
            ImageView imageView = findViewById(R.id.imageView_SelectedImage);
            ImageView imageView1 = findViewById(R.id.imageView_ProcessedImage);

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 600, 450, true);
                imageView.setImageBitmap(resized);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }
}