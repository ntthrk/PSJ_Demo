package com.finalproject.ntthrk_win.psj_demo;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;

/**
 * Created by Ntthrk_win on 2/14/2017.
 */

public class CTextToSpeech extends UtteranceProgressListener implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener{
    public static CTextToSpeech cTextToSpeech;
    private Context context;
    private TextToSpeech tts;
    private Locale locale = Locale.getDefault();
    private String enginePackageName;
    private String message;
    private boolean isRunning;
    private int speakCount;

    public static CTextToSpeech getcTextToSpeech(Context context){
        if(cTextToSpeech == null){
            cTextToSpeech = new CTextToSpeech(context);
        }
        return cTextToSpeech;
    }

    public CTextToSpeech(Context context){
        this.context = context;
    }

    public void speak(String massage){
        this.message = massage;

        if(tts == null || !isRunning){
            speakCount = 0;

            if(enginePackageName != null && !enginePackageName.isEmpty()){
                tts = new TextToSpeech(context, this, enginePackageName);
            }else{
                tts = new TextToSpeech(context, this);
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
                tts.setOnUtteranceProgressListener(this);
            }else{
                tts.setOnUtteranceProgressListener(this);
            }

            isRunning = true;
        }else{
            startSpeak();
        }
    }

    public CTextToSpeech setEngine(String packageName){
        enginePackageName = packageName;
        return this;
    }

    private CTextToSpeech setLocale(Locale locale){
        this.locale = locale;
        return this;
    }

    private void startSpeak(){
        speakCount++;

        if(locale != null){
            tts.setLanguage(locale);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "");
        }else{
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void clear(){
        speakCount--;

        if(speakCount == 0){
            tts.shutdown();
            isRunning = false;
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            startSpeak();
        }
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        clear();
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {
        clear();
    }

    @Override
    public void onError(String utteranceId) {
        clear();
    }

}
