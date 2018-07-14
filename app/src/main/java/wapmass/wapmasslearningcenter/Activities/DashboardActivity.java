package wapmass.wapmasslearningcenter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import wapmass.wapmasslearningcenter.Adapter.CourseAdapter;
import wapmass.wapmasslearningcenter.Model.Course;
import wapmass.wapmasslearningcenter.R;



public class DashboardActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DatabaseReference mDatabaseReference;
    private CourseAdapter adapter;
    private ArrayList<Course> courses;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setupToolbar();


        mAuth = FirebaseAuth.getInstance();

        mProgressBar = findViewById(R.id.progressBar);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Course");
        courses = new ArrayList<>();
        recyclerView =  findViewById(R.id.recyclerView);
        adapter = new CourseAdapter(this, courses);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCourses();
    }

    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        Bundle   bundle = getIntent().getExtras();
//        if(bundle.getString("user").equals("admin")) {
//            MenuItem item = menu.findItem(R.id.add_course);
//            item.setVisible(true);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about_wapmass:
                startActivity(new Intent(DashboardActivity.this, AboutActivity.class));
                break;

            case R.id.add_course:
                startActivity(new Intent(DashboardActivity.this, AddCourse.class));
                break;

            case R.id.signOut:
                mAuth.signOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getCourses(){
        showDialog();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot singleSnapshot:  dataSnapshot.getChildren()){
                        Course course = singleSnapshot.getValue(Course.class);
                        Log.d("data", course.toString());
                        courses.add(course);
                        adapter.notifyDataSetChanged();
                    }
                }
                hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideDialog();
            }
        });
    }

    private void showDialog()
    {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}



