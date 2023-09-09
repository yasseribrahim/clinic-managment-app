package com.clinic.management.app.ui.activities;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.clinic.management.app.databinding.ActivityTextToSpeechBinding;

import java.util.Locale;

public class TextToSpeechActivity extends AppCompatActivity {
    ActivityTextToSpeechBinding binding;
    TextToSpeech speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextToSpeechBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.forLanguageTag("ar"));
                }
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = binding.editText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    public void onPause() {
        if (speech != null) {
            speech.stop();
            speech.shutdown();
        }
        super.onPause();
    }
}