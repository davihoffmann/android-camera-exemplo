package br.edu.unidavi.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button abrirCamera;
    private ImageView imagem;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        abrirCamera = findViewById(R.id.tirar_foto);
        imagem = findViewById(R.id.imagem);

        abrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrir camera
                file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");
                Uri outputDir = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID, file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputDir);

                startActivityForResult(intent, 1_000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1_000) {
            if(data != null && data.hasExtra("data")) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                imagem.setImageBitmap(thumbnail);
            } else {
                int width = imagem.getWidth();
                int height = imagem.getHeight();

                BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                factoryOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), factoryOptions);
                int imageWidth = factoryOptions.outWidth;
                int imageHeight = factoryOptions.outHeight;

                //verificar o quanto precisamos escalar a imagem
                int scaleFactor = Math.min(imageWidth / width, imageHeight / height);
                factoryOptions.inJustDecodeBounds = false;
                factoryOptions.inSampleSize = scaleFactor;

                Bitmap image = BitmapFactory.decodeFile(file.getPath(), factoryOptions);
                imagem.setImageBitmap(image);
            }
        }
    }
}
