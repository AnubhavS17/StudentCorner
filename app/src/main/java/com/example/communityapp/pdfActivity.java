package com.example.communityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class pdfActivity extends AppCompatActivity {
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfView=findViewById(R.id.pdfView);

        int subject_position=getIntent().getIntExtra("key_position",0);
//        int subject_position2=getIntent().getIntExtra("key_position2",0);
//        subjectSem5(subject_position2);
        if(subject_position==0){
            pdfView.fromAsset("ML pdf.pdf").load();
        }
        else if(subject_position==1){
            pdfView.fromAsset("sic.pdf").load();
        } else if (subject_position==4) {
            pdfView.fromAsset("cc.pdf").load();
        } else if (subject_position==3) {
            pdfView.fromAsset("sqa.pdf").load();
        }
        else{
            pdfView.fromAsset("cl.pdf").load();
        }


    }
//    void subjectSem5(int subject_position2){
//        if(subject_position2==0){
//            pdfView.fromAsset("ai.pdf").load();
//        }
//    }
}