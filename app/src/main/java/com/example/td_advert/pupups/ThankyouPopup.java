package com.example.td_advert.pupups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.td_advert.R;

/**
 * Created by Administrator on 6/6/2016.
 */
public class ThankyouPopup extends Activity {
    ImageView exitButton;
    ImageView preButton;
    RelativeLayout questionSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.thankyou_layout);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.4), (int) (height * 0.7));

        exitButton = (ImageView) this.findViewById(R.id.exit_button);
        preButton = (ImageView) this.findViewById(R.id.gender_pre);
        questionSend = (RelativeLayout) this.findViewById(R.id.questions_send);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        questionSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prePage();
            }
        });

    }

    public void prePage() {
        Intent intent = new Intent(this, OccupationPopup.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition (0, 0);
        finish();
    }
}
