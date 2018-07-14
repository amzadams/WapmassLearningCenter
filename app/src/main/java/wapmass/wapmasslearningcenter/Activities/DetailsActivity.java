package wapmass.wapmasslearningcenter.Activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import wapmass.wapmasslearningcenter.Model.Course;
import wapmass.wapmasslearningcenter.R;

public class DetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String title, desc;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        description = findViewById(R.id.desc);
        setupToolbar();
        setupUI();
    }

    public String[] getIncomingData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            title = bundle.getString("courseTitle");
            desc = bundle.getString("courseDesc");
        }
        String [] data = {title, desc};
        return data;
    }


    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getIncomingData()[0]);
    }

    public void setupUI(){
        description.setText(getIncomingData()[1]);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }
}
