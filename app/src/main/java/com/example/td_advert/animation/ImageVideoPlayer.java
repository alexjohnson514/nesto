package com.example.td_advert.animation;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;

/**
 * Created by Narongdej on 4/2/2558.
 */
public class ImageVideoPlayer {

    ImageView imageholder;
    Context context;
    Timer myTimer;
    String folderName;
    String[] imvid;

    InputStream ims = null;
    List<Drawable> animImages;

    private long RETRY_TIME = 100;
    private long START_TIME = 100;

    public ImageVideoPlayer(Context ctx, ImageView img){
        imageholder = img;
        context = ctx;
    }

    private String[] listFiles(String dirFrom) throws IOException {
        AssetManager am = context.getAssets();
        String fileList[] = am.list(dirFrom);
        return fileList;
    }

    public void play(String folderName) {
        AnimationDrawable newAnim = new AnimationDrawable();

        try {
            imvid = listFiles(folderName);

            for(String imv: imvid){
                newAnim.addFrame((Drawable.createFromStream(context.getAssets().open(folderName + "/" + imv), null)), 50);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Zenyai", e.toString());
        }

        imageholder.setImageDrawable(newAnim);
        newAnim.start();

     }
}
