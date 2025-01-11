package com.dog.translator.talkingdog.prank.simulator.language.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dog.translator.talkingdog.prank.simulator.R;
import com.dog.translator.talkingdog.prank.simulator.language.IClickLanguage;
import com.dog.translator.talkingdog.prank.simulator.language.LanguageModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<LanguageModel> lists;
    private IClickLanguage iClickLanguage;

    public LanguageAdapter(Context context, List<LanguageModel> lists, IClickLanguage iClickLanguage) {
        this.context = context;
        this.lists = lists;
        this.iClickLanguage = iClickLanguage;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new LanguageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_language, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        LanguageModel data = lists.get(position);
        if (holder instanceof LanguageViewHolder) {
            ((LanguageViewHolder) holder).bind(data);
            ((LanguageViewHolder) holder).relayLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickLanguage.onClick(data);
                }
            });
            ((LanguageViewHolder) holder).rbActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickLanguage.onClick(data);
                }
            });

            ((LanguageViewHolder) holder).rbActive.setChecked(data.isCheck());
        }
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvTitle;
        RelativeLayout relayLanguage;
        RadioButton rbActive;

        public LanguageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.img_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            relayLanguage = itemView.findViewById(R.id.relay_language);
            rbActive = itemView.findViewById(R.id.rb_active);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bind(LanguageModel data) {
            ivAvatar.setImageDrawable(context.getDrawable(data.getImage()));
            tvTitle.setText(data.getLanguageName());
        }
    }

    public void setSelectLanguage(LanguageModel model) {
        for (LanguageModel data : lists) {
            if (data.getLanguageName().equals(model.getLanguageName())) {
                data.setCheck(true);
            } else {
                data.setCheck(false);
            }
        }
        notifyDataSetChanged();
    }
}

