package com.dog.translator.talkingdog.prank.simulator.sounds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dog.translator.talkingdog.prank.simulator.R;
import com.dog.translator.talkingdog.prank.simulator.training.TrainingModel;

import java.util.ArrayList;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.IntroAdapterHolder> {

    private ArrayList<TrainingModel> mHelpTrainingModel;
    Context context;
    private OnClickItem onClickItem;

    public SoundAdapter(ArrayList<TrainingModel> mHelpTrainingModel, Context context, OnClickItem onClickItem) {
        this.mHelpTrainingModel = mHelpTrainingModel;
        this.context = context;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public IntroAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sound, parent, false);
        return new IntroAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntroAdapterHolder holder, int i) {
        TrainingModel helpTrainingModelModel = mHelpTrainingModel.get(i);
        holder.imageView.setImageResource(helpTrainingModelModel.getImg());
        holder.tvTitle.setText(helpTrainingModelModel.getTitle());
        holder.itemView.setOnClickListener(view -> onClickItem.onClickItem(i, helpTrainingModelModel));
    }

    @Override
    public int getItemCount() {
        if (mHelpTrainingModel != null) {
            return mHelpTrainingModel.size();
        } else {
            return 0;
        }
    }

    public class IntroAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvTitle;


        public IntroAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tvTitle);

        }
    }

    interface OnClickItem {
        void onClickItem(int pos, TrainingModel trainingModel);
    }
}
