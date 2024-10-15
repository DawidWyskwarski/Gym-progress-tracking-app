package com.example.forthegym;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class exercise_RecycleViewAdapter extends RecyclerView.Adapter<exercise_RecycleViewAdapter.MyViewHolder> {

    private static Context context;
    private ArrayList<Exercises> exercises;

    public exercise_RecycleViewAdapter(ArrayList<Exercises> exercises, Context context) {
        this.exercises = exercises;
        this.context = context;
    }

    public void setFilteredList(ArrayList<Exercises> filteredList){
        this.exercises = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercise_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(exercises.get(position).getName());
        holder.lifts.setText(exercises.get(position).getLastFive());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.relativeLayout.getVisibility() == View.GONE){
                    expandView(holder.relativeLayout,0);
                }else{
                    collapseView(holder.relativeLayout);
                }
            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                editView(position);

                return true;
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModifier(position,holder.relativeLayout);
            }
        });

        holder.lifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreTraining(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout linearLayout;
        private TextView name;
        private TextView lifts;
        private RelativeLayout relativeLayout;
        private Button add;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            name = itemView.findViewById(R.id.name);
            lifts = itemView.findViewById(R.id.lifts);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            add = itemView.findViewById(R.id.add);

        }

    }

    private void expandView(@NonNull final View view, int initialHeight) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        view.getLayoutParams().height = initialHeight;
        view.setVisibility(View.VISIBLE);

        ValueAnimator animator = ValueAnimator.ofInt(initialHeight, targetHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }
    private void collapseView(@NonNull final View view) {
        final int initialHeight = view.getMeasuredHeight();

        ValueAnimator animator = ValueAnimator.ofInt(initialHeight, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                view.setVisibility(View.GONE); // Set visibility to GONE after collapsing
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }

    private void showModifier(int position, View view) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.modify_load_box);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f; // 0.0 = no dimming, 1.0 = full dimming
        dialog.getWindow().setAttributes(layoutParams);

        EditText etLoad = dialog.findViewById(R.id.etLoad);
        EditText etReps = dialog.findViewById(R.id.etReps);
        Button updateButton = dialog.findViewById(R.id.updateButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                LocalDate date = LocalDate.now();
                String load = etLoad.getText().toString();
                String reps = etReps.getText().toString();

                if(!load.isEmpty() && !reps.isEmpty()){

                    exercises.get(position).addTraining(new DLR(date, Float.valueOf(load), Integer.valueOf(reps)));

                    notifyItemChanged(position);
                    collapseView(view);

                    dialog.dismiss();
                }else if( load.isEmpty() ){
                    etLoad.setError("Enter the load");
                }else if( reps.isEmpty() ){
                    etReps.setError("Enter the reps");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void showMoreTraining(int position){

        Dialog window = new Dialog(context);
        window.setContentView(R.layout.moretraining);
        window.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button close = window.findViewById(R.id.close);
        TextView text = window.findViewById(R.id.all_lifts);

        text.setText(exercises.get(position).getAll());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        window.show();
    }

    private void editView(int position){
        Dialog window = new Dialog(context);
        window.setContentView(R.layout.edit_exercise);
        window.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button deleteLast = window.findViewById(R.id.deleteLast);
        Button deleteAll = window.findViewById(R.id.deleteExercise);

        deleteLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercises.get(position).deleteLast();
                notifyItemChanged(position);
                window.dismiss();
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercises.remove(position);
                notifyDataSetChanged();
                window.dismiss();

            }
        });

        window.show();
    }
}
