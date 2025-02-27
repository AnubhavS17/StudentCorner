package com.example.communityapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PdfFragment extends Fragment {


    ListView listView;
    public PdfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_pdf, container, false);
       listView=view.findViewById(R.id.list_view);
//       listView2=view.findViewById(R.id.list_view2);
       String[] subjects_names=getResources().getStringArray(R.array.subjects_names);
//        String[] subjects_names2=getResources().getStringArray(R.array.subjects_names2);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,subjects_names);
//        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,subjects_names2);
//        listView2.setAdapter(adapter2);
        listView.setAdapter(adapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   Intent intent=new Intent(getContext(), pdfActivity.class);
                   intent.putExtra("key_position",i);
                   startActivity(intent);
           }
       });
//       listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//           @Override
//           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//               Intent intent=new Intent(getContext(), pdfActivity.class);
//               intent.putExtra("key_position2",i);
//               startActivity(intent);
//           }
//       });


       return view;
    }
}