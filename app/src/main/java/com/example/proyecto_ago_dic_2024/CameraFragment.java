package com.example.proyecto_ago_dic_2024;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CameraFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE =1;
    private static final int REQUEST_PERMISSIONS =100;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        Button captureButton = rootView.findViewById(R.id.button_capture);
        imageView = rootView.findViewById(R.id.imageView_photo);

        captureButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            } else {
                openCamera();
            }
        });
        return rootView;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        } catch (Exception e){
            Toast.makeText(getContext(),"No se pudo abrir la cámara", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            saveImageToGallery(imageBitmap);
        }
    }

    private void saveImageToGallery (Bitmap bitmap) {
        OutputStream outputStream;
        String imageName = "captured_image_" + System.currentTimeMillis() + ".jpg";

        try{
            File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyApp");
            if(!imagesFolder.exists()){
                imagesFolder.mkdirs();
            }   
            File imageFile = new File(imagesFolder,imageName);

            outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),imageFile.getAbsolutePath(), imageName,"Imagen capturada");
            Toast.makeText(getContext(),"Imagen guardada en la galería", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getContext(),"Error al guardar la imagen", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        if(requestCode == REQUEST_PERMISSIONS){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                openCamera();
            } else {
                Toast.makeText(getContext(),"Permisos de cámara y almacenamiento denegados", Toast.LENGTH_SHORT).show();
            }

        }
    }


}