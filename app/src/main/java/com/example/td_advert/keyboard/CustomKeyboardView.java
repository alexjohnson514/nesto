package com.example.td_advert.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * Created by Narongdej on 19/1/2558.
 */
public class CustomKeyboardView extends KeyboardView {

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

//        List<Keyboard.Key> keys = getKeyboard().getKeys();
//        for(Keyboard.Key key : keys){
//            if(key.codes[0] == -4){
//                Drawable dr = this.getResources().getDrawable(R.drawable.done_key);
//                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
//                dr.draw(canvas);
//            }
//        }
    }
}
