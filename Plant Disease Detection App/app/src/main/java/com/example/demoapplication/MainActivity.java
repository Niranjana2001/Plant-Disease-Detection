package com.example.demoapplication;

import static com.example.demoapplication.R.id.backbutton;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Bitmap bitmap = null;
    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }
        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
    private void openImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openGallery();
                } else {
                    openCamera();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                // Handle image selected from gallery
                Bitmap selectedImage = getBitmapFromUri(data.getData());
                if (selectedImage != null) {
                    bitmap = selectedImage;
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);
                }
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                // Handle image captured from camera
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                if (capturedImage != null) {
                    bitmap = capturedImage;
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
    public void onBackPressed() {
        // Start the main activity again when the back button is pressed
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
        startActivity(intent);
    }


    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button predictButton = findViewById(R.id.predictbutton);
        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform inference when the predict button is clicked
//                performInference();
                Log.e("image","error preprocessing");
                displayPredictedDisease(); // Display the predicted disease
            }
        });
        Button startbutton = findViewById(R.id.button);
        startbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // This will be called when the button is clicked
                v.setEnabled(false);
                openImagePickerDialog();
                System.out.println("image selected");
                Log.e("image","gallery ");
            }
        });
        if (savedInstanceState != null) {
            Bitmap savedBitmap = savedInstanceState.getParcelable("bitmap");
            if (savedBitmap != null) {
                bitmap = savedBitmap;
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            }
        }


        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform inference when the predict button is clicked
//                performInference();
                Log.e("image","error7");
                System.out.println("function");
                displayPredictedDisease(); // Display the predicted disease
            }
        });
        Button backButton = findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Back" button click
                onBackPressed();
            }
        });
    }




    private void displayPredictedDisease() {
        System.out.println("beginning");
        try {
            Log.e("image","error in the beginnning of the function");
            Module module = Module.load(assetFilePath(this, "android_model.ptl"));
            Log.e("image","bitmap");

//            resize
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
//            adjust
            float[] mean={(float)0.485, (float)0.456,(float) 0.406};
            float[] std={(float)0.229, (float)0.224, (float)0.225};

            Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resized,mean,std);
            Log.e("image","bitmap conversion");

            Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
            float[] scores = outputTensor.getDataAsFloatArray();

            float maxScore = -Float.MAX_VALUE;
            int maxScoreIdx = -1;
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] > maxScore) {
                    maxScore = scores[i];
                    maxScoreIdx = i;
                }
            }

            Log.e("image","error after classifying : "+maxScoreIdx+ " : "+maxScore);

            String[] diseaseClasses = {
                    "Apple___Apple_scab",
                    "Apple___Black_rot",
                    "Apple___Cedar_apple_rust",
                    "Apple___healthy",
                    "Blueberry___healthy",
                    "Cherry_(including_sour)___Powdery_mildew",
                    "Cherry_(including_sour)___healthy",
                    "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot",
                    "Corn_(maize)___Common_rust_",
                    "Corn_(maize)___Northern_Leaf_Blight",
                    "Corn_(maize)___healthy",
                    "Grape___Black_rot",
                    "Grape___Esca_(Black_Measles)",
                    "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)",
                    "Grape___healthy",
                    "Orange___Haunglongbing_(Citrus_greening)",
                    "Peach___Bacterial_spot",
                    "Peach___healthy",
                    "Pepper,_bell___Bacterial_spot",
                    "Pepper,_bell___healthy",
                    "Potato___Early_blight",
                    "Potato___Late_blight",
                    "Potato___healthy",
                    "Raspberry___healthy",
                    "Soybean___healthy",
                    "Squash___Powdery_mildew",
                    "Strawberry___Leaf_scorch",
                    "Strawberry___healthy",
                    "Tomato___Bacterial_spot",
                    "Tomato___Early_blight",
                    "Tomato___healthy",
                    "Tomato___Late_blight",
                    "Tomato___Leaf_Mold",
                    "Tomato___Septoria_leaf_spot",
                    "Tomato___Spider_mites Two-spotted_spider_mite",
                    "Tomato___Target_Spot",
                    "Tomato___Tomato_mosaic_virus",
                    "Tomato___Tomato_Yellow_Leaf_Curl_Virus"
            };

            String className = diseaseClasses[maxScoreIdx];

            Log.e("image","error after giving label");

            TextView textView = findViewById(R.id.textView);
            String prediction = maxScore + " " + className;
//            textView.setText(prediction);
            textView.setText(className);

            Log.e("image","error while printing text");
        } catch (Exception e) {
            Log.e("PTRTDryRun", "Error processing output tensor", e);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bitmap", bitmap);
    }
}
