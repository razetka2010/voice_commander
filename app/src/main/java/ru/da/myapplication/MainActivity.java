package ru.da.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView testText;
    private ImageView imMain;
    private SoundPool sound;
    private int sound_sirena;
    private TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        testText = findViewById(R.id.testText);
        imMain = findViewById(R.id.imMain);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });
        createSoundPool();
        sound_sirena = sound.load(this, R.raw.sirena, 1);
    }

    private void createSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes
                        .CONTENT_TYPE_SONIFICATION).build();
        sound = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }


    public void onClickMic(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 99) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                assert text != null;
                testText.setText(text.get(0).toLowerCase());
                do_command(text.get(0).toLowerCase());
            }
        }
    }

    private void do_command(String text) {
        switch (text) {
            case "яблоко":
                imMain.setImageResource(R.drawable.manzana_lista);
                break;

            case "дыня":
                imMain.setImageResource(R.drawable.melon_listo);
                break;


            case "арбуз":
                imMain.setImageResource(R.drawable.sandia_listo);
                break;

            case "корова":
                imMain.setImageResource(R.drawable.vaca_lista);
                break;

            case "протокол самоуничтожения":
                protocolDist();
                break;
            case "кто будет призидентом сша":
                textToSpeech.speak("я", textToSpeech.QUEUE_FLUSH, null);
                break;

            case "ой?":
                textToSpeech.speak("ой", textToSpeech.QUEUE_FLUSH, null);
                break;
        }
    }

    private void protocolDist() {
        sound.play(sound_sirena, 1.0f, 1.0f, 1, 0, 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 6; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final int finalI = 5 - i;
                    testText.post(new Runnable() {
                        @Override
                        public void run() {
                            testText.setText(String.valueOf(finalI));
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imMain.setImageResource(R.drawable.calavera);
                    }
                });
            }
        }).start();
    }
}