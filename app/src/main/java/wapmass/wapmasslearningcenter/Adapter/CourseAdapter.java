package wapmass.wapmasslearningcenter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import wapmass.wapmasslearningcenter.Activities.DetailsActivity;
import wapmass.wapmasslearningcenter.Model.Course;
import wapmass.wapmasslearningcenter.R;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyCourseview>{
    private Context context;
    private ArrayList<Course> dataLists;
    public CourseAdapter(Context context, ArrayList<Course> dataLists){
        this.context = context;
        this.dataLists = dataLists;
    }

    @NonNull
    @Override
    public CourseAdapter.MyCourseview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_row, parent, false);
        return new MyCourseview(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.MyCourseview holder, int position) {
        Course course = dataLists.get(position);
        holder.title.setText(course.getcTitle());
        Picasso.get()
                .load(course.getImageLink())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {

        return dataLists.size();
    }

    public class MyCourseview extends RecyclerView.ViewHolder {
        //instantiate our row_item views
        private TextView title;
        private ImageView image;
        public MyCourseview(View rowList , Context ctx) {
            super(rowList);
            context = ctx;
            title = rowList.findViewById(R.id.title);
            image = rowList.findViewById(R.id.image);

            rowList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    int index = getAdapterPosition();
                    intent.putExtra("courseTitle", dataLists.get(index).getcTitle());
                    intent.putExtra("courseDesc", dataLists.get(index).getcDesc());
                    context.startActivity(intent);

                }
            });
        }

    }
}
