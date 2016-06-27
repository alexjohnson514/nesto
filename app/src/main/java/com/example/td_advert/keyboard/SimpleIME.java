package com.example.td_advert.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;

import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.example.td_advert.R;
import com.example.td_advert.dialog.PromotionDialog;

/**
 * Created by Narongdej on 19/1/2558.
 */
public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean isThai = true;
    private boolean caps = false;
    private boolean isNumpad = false;

    private String thai[] = {"ๅ", "ภ", "ถ", "ุ", "ึ", "ค", "ต", "จ", "ข", "ช",
                        "ๆ", "ไ", "ำ", "พ", "ะ", "ั", "ี", "ร", "น", "ย", "บ", "ล",
                        "ฟ", "ห", "ก", "ด", "เ", "้", "่", "า", "ส", "ว", "ง", "ฃ",
                        "ผ", "ป", "แ", "อ", "ิ", "ื", "ท", "ม", "ใ", "ฝ"};

    private String thai_cap[] = {"ู", "๐", "ฎ", "ฑ", "ธ", "ํ" , "๊", "ณ", "ฯ", "ญ", "ฐ",
                            "ฤ", "ฆ", "ฏ", "โ", "ฌ", "็", "๋", "ษ", "ศ", "ซ", "ฅ",
                            "ฉ", "ฮ", "ฺ", "์", "ฒ", "ฬ", "ฦ"};

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);

        keyboard = new Keyboard(this, R.xml.thai);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        return kv;
    }

    @Override
    public void onUpdateExtractingVisibility(EditorInfo ei) {
        ei.imeOptions |= EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        super.onUpdateExtractingVisibility(ei);
    }

    @Override
    public void onStartInputView(final EditorInfo attribute,
                                 final boolean restarting) {
        super.onStartInputView(attribute, restarting);
        switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {
            case EditorInfo.TYPE_CLASS_PHONE:
                keyboard = new Keyboard(this, R.xml.hexkbd);
                kv.setKeyboard(keyboard);
                isNumpad = true;
                break;
            default:
                if(isNumpad){
                    keyboard = new Keyboard(this, R.xml.thai);
                    kv.setKeyboard(keyboard);
                    caps = false;
                    isThai = true;
                    isNumpad = false;
                }

        }
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d("KeyEvent", Integer.toString(primaryCode));

        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();

                if(isThai && caps){
                    keyboard = new Keyboard(this, R.xml.thai_cap);
                    kv.setKeyboard(keyboard);
                    kv.invalidateAllKeys();
                } else if(isThai && !caps) {
                    keyboard = new Keyboard(this, R.xml.thai);
                    kv.setKeyboard(keyboard);
                    kv.invalidateAllKeys();
                }
                break;

            case Keyboard.KEYCODE_DONE:
                //ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                PromotionDialog.keyboardLoose();
                requestHideSelf(0);
                break;
            case 5000:
                if(isThai){
                    keyboard = new Keyboard(this, R.xml.qwerty);
                    kv.setKeyboard(keyboard);
                    caps = false;
                    kv.setShifted(false);
                    isThai = false;
                    kv.invalidateAllKeys();
                } else {
                    keyboard = new Keyboard(this, R.xml.thai);
                    kv.setKeyboard(keyboard);
                    caps = false;
                    kv.setShifted(false);
                    isThai = true;
                    kv.invalidateAllKeys();
                }
                break;
            default:
                ic.commitText(getPrintable(primaryCode), 1);
        }
    }

    private String getPrintable(int primaryCode){
        if (primaryCode < 255) {
            char code = (char) primaryCode;
            if(Character.isLetter(code) && caps){
                code = Character.toUpperCase(code);
            }
            return String.valueOf(code);
        } else if(primaryCode < 2000) {
            return thai[primaryCode - 1000];
        } else {
            return thai_cap[primaryCode - 2000];
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }
}