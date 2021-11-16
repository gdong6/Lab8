package com.example.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(null);
    }

    public void uploadMethod(View view) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference flowerRef = storageReference.child("images/flower.jpeg");

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.flower);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] flowerByteStream = baos.toByteArray();

        UploadTask uploadTask = flowerRef.putBytes(flowerByteStream);
        uploadTask.addOnFailureListener((exception) -> {})
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i("!!!!!!成功ImageUpload", "Image successfully uploaded to Firebase");
                }
        });
    }

    public void downloadMethod(View view) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference flowerRef = storageReference.child("images/flower.jpeg");

        final ImageView imageView = findViewById(R.id.imageView);
        final long THREE_MEGABYTE = 26*1024*1024;

        flowerRef.getBytes(THREE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.i("Error", "下载失败Download Failed");
            }
        });
    }
}