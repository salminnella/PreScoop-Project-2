package com.example.anthony.prescoop.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.anthony.prescoop.R;
import com.example.anthony.prescoop.adapters.SchoolAdapter;
import com.example.anthony.prescoop.models.PreSchool;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {


    ArrayList<PreSchool> preSchools;
    ListView listView;
    SchoolAdapter schoolAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        fillListOfPreschools();
        initViews();

    }

    private void fillListOfPreschools() {
        preSchools = new ArrayList<>();
        preSchools.add(new PreSchool("Acorn Learning Center","816 Diablo Rd, Danville, CA 94526", 0.00, R.string.acorn_description, 5, "East Bay"));
        preSchools.add(new PreSchool("The Quarry Lane School", "3750 Boulder St., Pleasanton", 1400.00, R.string.quarry_description, 3, "East Bay"));
        preSchools.add(new PreSchool("San Francisco Montessori Academy", "1566 32nd Ave., San Francisco", 0.00, R.string.sf_montesorri_description, 5, "San Francisco"));
        preSchools.add(new PreSchool("Sunset Co-Op Nursery School", "4245 Lawton St, San Francisco, CA", 305.00, R.string.sunset_co_op_description, 4, "San Francisco"));
        preSchools.add(new PreSchool("Little Urbanites", "1258 20th Ave San Francisco California 94122", 0.00, R.string.little_urbanites, 4, "San Francisco"));
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.school_results_list);
        schoolAdapter = new SchoolAdapter(ListActivity.this, preSchools);
        listView.setAdapter(schoolAdapter);
    }
}