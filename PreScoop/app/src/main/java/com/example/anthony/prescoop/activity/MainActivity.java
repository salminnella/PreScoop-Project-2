package com.example.anthony.prescoop.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anthony.prescoop.DBHelper.DatabaseHelper;
import com.example.anthony.prescoop.R;
import com.example.anthony.prescoop.adapters.SpinAdapter;
import com.example.anthony.prescoop.models.PopulatePreschoolDB;
import com.example.anthony.prescoop.models.PreSchool;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String SEARCH_CRITERIA = "searchCriteria";
    public static final String SEARCH_NAME = "searchName";
    public static final String SEARCH_RANGE = "searchRange";
    public static final String SEARCH_RATING = "searchRating";
    public static final String SEARCH_PRICE = "searchPrice";
    public static final String SEARCH_FAVS = "searchPrice";
    public static final String FAVS_KEY = "findFavs";

    //region private variables
    private EditText priceEdit;
    private EditText schoolNameEdit;
    private Spinner rangeSpinner;
    private ArrayAdapter<CharSequence> rangeAdapter;
    private Spinner ratingSpinner;
    private SpinAdapter ratingAdapter;
    private Button search;
    DatabaseHelper db;
    // endregion private variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DatabaseHelper.getInstance(MainActivity.this);
        initViews();
        initClickListeners();

        fillTempPreschool();

    }

    private void initViews() {
        priceEdit = (EditText) findViewById(R.id.price_main_edit);
        schoolNameEdit = (EditText) findViewById(R.id.school_name_main_edit);
        search = (Button) findViewById(R.id.search_button);



        rangeSpinner = (Spinner) findViewById(R.id.range_spinner_main);
        // Create an ArrayAdapter for Range drop down using the string-array in strings.xml
        rangeAdapter = ArrayAdapter.createFromResource(this, R.array.range_array, android.R.layout.simple_spinner_item);
        // default layout to use when the list of choices appears
        rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //connect the two
        rangeSpinner.setAdapter(rangeAdapter);

        // for the images on the schoolrating drop down spinner
        ratingSpinner = (Spinner) findViewById(R.id.rating_spinner_main);
        // Create a custom adapter forrating drop down
        ratingAdapter = new SpinAdapter(MainActivity.this, new Integer[]{R.drawable.pixel, R.drawable.one_star, R.drawable.two_stars, R.drawable.three_stars, R.drawable.four_stars, R.drawable.five_stars});

        //connect the two
        ratingSpinner.setAdapter(ratingAdapter);


    }

    private void initClickListeners() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Search searchData = new Search(schoolNameEdit.getText().toString(), getRange(), getRating(), priceEdit.getText().toString());
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                //intent.putExtra("searchObject", searchData);
                intent.putExtra(SEARCH_NAME, schoolNameEdit.getText().toString());
                intent.putExtra(SEARCH_RANGE, getRange());
                intent.putExtra(SEARCH_RATING, getRating());
                intent.putExtra(SEARCH_PRICE, priceEdit.getText().toString());
               startActivity(intent);
            }
        });
    }

    private void fillTempPreschool() {
        //DatabaseHelper db = new DatabaseHelper(this);
        // do a get on preschools
        Cursor cursor = db.getAllPreschools();
        cursor.moveToFirst();


        // if the cursor result is empty, insert the schools below
        // if the cursor isn't empty skip the insert
        if (cursor.getCount() > 0) {
            cursor.close();
        } else {

            ArrayList<PreSchool> preschools = PopulatePreschoolDB.getPreSchoolItems(MainActivity.this);
            for (PreSchool school : preschools) {
                db.insertIntoPreschools(school.getName(), school.getSchoolDescription(), school.getPrice(),
                        school.getStreetAddress(), school.getCity(), school.getState(), school.getZipCode(),
                        school.getType(), school.getRating(), school.getRegion(), school.getRange(),
                        school.getPhoneNumber(), school.getAgeGroup(), school.getFavorite(), school.getImages(), school.getImageDescription());
            }
            cursor.close();
        }
      }

    private String getRange() {
        int rangeAsNumber = 0;
        if (rangeSpinner.getSelectedItem().toString().isEmpty()) {
            return "";
        }

        switch (rangeSpinner.getSelectedItem().toString()) {
            case "1 Mile":
                rangeAsNumber = 1;
                break;
            case "5 Miles":
                rangeAsNumber = 5;
                break;
            case "10 Miles":
                rangeAsNumber = 10;
                break;
            case "15 Miles":
                rangeAsNumber = 15;
                break;
            case "20 Miles":
                rangeAsNumber = 20;
                break;
            default:
                break;
       }

        return String.valueOf(rangeAsNumber);
    }

    private String getRating() {
        int rating=0;
        // TODO try getselecteditemposition
        if (ratingSpinner.getSelectedItem().equals(R.drawable.pixel)) rating = 0;
        else if (ratingSpinner.getSelectedItem().equals(R.drawable.one_star)) rating = 1;
        else if (ratingSpinner.getSelectedItem().equals(R.drawable.two_stars)) rating = 2;
        else if (ratingSpinner.getSelectedItem().equals(R.drawable.three_stars)) rating = 3;
        else if (ratingSpinner.getSelectedItem().equals(R.drawable.four_stars)) rating = 4;
        else if (ratingSpinner.getSelectedItem().equals(R.drawable.five_stars)) rating = 5;

        return String.valueOf(rating);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_find_favorites:
                //go to list activity and find all favorites
                DatabaseHelper searchHelper = DatabaseHelper.getInstance(MainActivity.this);
                Cursor cursor = searchHelper.findFavoritePreschools();
                if (cursor.getCount() > 0) {
                    Intent intentForFavs = new Intent(MainActivity.this, ListActivity.class);
                    intentForFavs.putExtra(SEARCH_FAVS, FAVS_KEY);
                    startActivity(intentForFavs);
                } else {
                    Toast.makeText(MainActivity.this, "No Favorites To View, Add One From The Details Page", Toast.LENGTH_LONG).show();
                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}