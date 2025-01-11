package com.dog.translator.talkingdog.prank.simulator.intro;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dog.translator.talkingdog.prank.simulator.R;

public class RatingDialog extends Dialog {
    private OnPress onPress;
    private TextView tvTitle, tvContent;
    private RatingBar rtb;
    private ImageView imgIcon, imageView;
    private EditText editFeedback;
    private Context context;
    private Button btnRate, btnLater;

    public RatingDialog(Context context2) {
        super(context2, R.style.BaseDialog);
        this.context = context2;
        setContentView(R.layout.dialog_rating_app);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(attributes);
        getWindow().setSoftInputMode(16);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        rtb = (RatingBar) findViewById(R.id.rtb);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);
        imageView = (ImageView) findViewById(R.id.imageView);
        editFeedback = (EditText) findViewById(R.id.editFeedback);
        btnRate = (Button) findViewById(R.id.btnRate);
        btnLater = (Button) findViewById(R.id.btnLater);
        onclick();
        changeRating();

    }

    public interface OnPress {
        void send();

        void rating();

        void cancel();

        void later();
    }

    public void init(Context context, OnPress onPress) {
        this.onPress = onPress;
    }

    public void changeRating() {
        rtb.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            String getRating = String.valueOf(rtb.getRating());
            switch (getRating) {
                case "1.0":
                    editFeedback.setVisibility(View.GONE);
                    btnRate.setText(context.getResources().getString(R.string.Send));
                    imgIcon.setImageResource(R.drawable.rating_1);
                    tvTitle.setText(context.getResources().getString(R.string.Oh_no));
                    tvContent.setText(context.getResources().getString(R.string.Please_leave_us_some_feedback));
                    break;
                case "2.0":
                    editFeedback.setVisibility(View.GONE);
                    btnRate.setText(context.getResources().getString(R.string.Send));
                    imgIcon.setImageResource(R.drawable.rating_2);
                    tvTitle.setText(context.getResources().getString(R.string.Oh_no));
                    tvContent.setText(context.getResources().getString(R.string.Please_leave_us_some_feedback));
                    break;
                case "3.0":
                    editFeedback.setVisibility(View.GONE);
                    btnRate.setText(context.getResources().getString(R.string.Send));
                    imgIcon.setImageResource(R.drawable.rating_3);
                    tvTitle.setText(context.getResources().getString(R.string.rate_thanks));
                    tvContent.setText(context.getResources().getString(R.string.Please_leave_us_some_feedback));
                    break;
                case "4.0":
                    editFeedback.setVisibility(View.GONE);
                    btnRate.setText(context.getResources().getString(R.string.rate));
                    imgIcon.setImageResource(R.drawable.rating_4);
                    tvTitle.setText(context.getResources().getString(R.string.We_like_you_too));
                    tvContent.setText(context.getResources().getString(R.string.rate_thanks));
                    break;
                case "5.0":
                    editFeedback.setVisibility(View.GONE);
                    btnRate.setText(context.getResources().getString(R.string.rate));
                    imgIcon.setImageResource(R.drawable.rating_5);
                    tvTitle.setText(context.getResources().getString(R.string.We_like_you_too));
                    tvContent.setText(context.getResources().getString(R.string.rate_thanks));
                    break;
                default:
                    btnRate.setText(context.getResources().getString(R.string.rate));
                    editFeedback.setVisibility(View.GONE);
                    imgIcon.setImageResource(R.drawable.rating_0);
                    tvTitle.setText(context.getResources().getString(R.string.We_are_working));
                    tvContent.setText(context.getResources().getString(R.string.We_greatly_appreciate));
                    break;
            }
        });


    }

    public String getNewName() {
        return this.editFeedback.getText().toString();
    }

    public String getRating() {
        return String.valueOf(this.rtb.getRating());
    }

    public void onclick() {
        btnRate.setOnClickListener(view -> {
            if (rtb.getRating() == 0) {
                Toast.makeText(context, context.getResources().getString(R.string.Please_feedback), Toast.LENGTH_SHORT).show();
                return;
            }
            if (rtb.getRating() <= 3.0) {
                imageView.setVisibility(View.GONE);
                imgIcon.setVisibility(View.GONE);
                onPress.send();
            } else {
                imageView.setVisibility(View.VISIBLE);
                imgIcon.setVisibility(View.VISIBLE);
                onPress.rating();
            }
        });
        btnLater.setOnClickListener(view -> onPress.later());

    }

}

