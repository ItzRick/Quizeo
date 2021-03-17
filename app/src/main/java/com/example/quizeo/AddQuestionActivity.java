package com.example.quizeo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RESULT_LOAD_IMAGE = 1;

    //local variables
    ImageView imageUpload;
    Button buttonUploadImage;
    EditText textQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question);

        imageUpload = (ImageView) findViewById(R.id.imageUpload);
        buttonUploadImage = (Button) findViewById(R.id.buttonUpload);
        textQuestion = (EditText) findViewById(R.id.textQuestion);

        imageUpload.setOnClickListener(this);
        buttonUploadImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v ) {
        switch(v.getId()){
            case R.id.imageUpload:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.buttonUpload:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageUpload.setImageURI(selectedImage);
        }
    }
}
