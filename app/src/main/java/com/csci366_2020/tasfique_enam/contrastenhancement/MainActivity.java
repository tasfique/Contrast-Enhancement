package com.csci366_2020.tasfique_enam.contrastenhancement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;


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

        if (resultCode == RESULT_OK && requestCode == 1) {
            ImageView imageView = findViewById(R.id.imageView_SelectedImage);
            final ImageView imageView1 = findViewById(R.id.imageView_ProcessedImage);

            Button button1 = findViewById(R.id.button_Processed);



            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 600, 450, true);
                imageView.setImageBitmap(resized);

                //USING THE ALGO TO MAKE HISTOGRAM EQUILISATION

                final Bitmap modifiedImage = Bitmap.createBitmap(600, 450, Bitmap.Config.ARGB_8888);

                int[] h = CalculateHist(resized);
                int[] h1 = CalculateHist1(resized);
                int[] h2 = CalculateHist2(resized);
                //calculate total number of pixel
                int mass = resized.getWidth() * resized.getHeight();
                int k;
                int k1;
                int k3;
                float scale = (float) 255.0 / mass;
                //CDF calculation
                int[] C1 = cdf(h, scale);
                int[] C2 = cdf(h1, scale);
                int[] C3 = cdf(h2, scale);

                //mapping new pixels values
                for (int i = 0; i < resized.getWidth(); i++) {
                    for (int j = 0; j < resized.getHeight(); j++) {
                        int pixel = resized.getPixel(i, j);
                        //set the new value
                        k = C1[Color.red(pixel)];
                        k1 = C2[Color.green(pixel)];
                        k3 = C3[Color.blue(pixel)];
                        int rgb = Color.rgb(k, k1, k3);
                        modifiedImage.setPixel(i, j, rgb);

                        //modifiedImage1.getPixels(pixels, 0, width,0,0, width, height);
                    }
                }

                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView1.setImageBitmap(modifiedImage);
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    public int[] CalculateHist(Bitmap bi) {
        int k;
        int levels[] = new int[256];
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int pixel = bi.getPixel(i, j);
                levels[Color.red(pixel)]++;
            }
        }
        return levels;
    }

    public int[] CalculateHist1(Bitmap bi) {
        int k;
        int levels[] = new int[256];
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int pixel = bi.getPixel(i, j);
                levels[Color.green(pixel)]++;
            }
        }
        return levels;
    }

    public int[] CalculateHist2(Bitmap bi) {
        int k;
        int levels[] = new int[256];
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int pixel = bi.getPixel(i, j);
                levels[Color.blue(pixel)]++;
            }
        }
        return levels;
    }

    public int[] cdf(int[] h, float scale) {
        int sum = 0;
        for (int x = 0; x < h.length; x++) {
            sum += h[x];
            int value1 = (int) (scale * sum);
            h[x] = value1;
        }
        return h;
    }
}