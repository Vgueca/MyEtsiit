package com.example.myetsiit.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.myetsiit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class FragmentDialogFlow extends FragmentActivity {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText tvResult;

    EditText tvBot;

    SpeechRecognizer speechRecognizer;

    int count = 0;

    private SessionsClient sessionsClient;
    private SessionName session;
    private final String uuid = UUID.randomUUID().toString();

    private String et_text_input;

    private TextToSpeech textToSpeechEngine;

    private  int speakResult = 0;

    private double destinationLatitude;
    private double destinationLongitude;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_flow);

        Button btnSpeak = findViewById(R.id.btnAlwaysVisible);
        tvBot = findViewById(R.id.tvBot);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new MyRecognitionListener());
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        initV2Chatbot();

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0){
                    speechRecognizer.startListening(speechRecognizerIntent);
                    count = 1;
                } else {
                    speechRecognizer.stopListening();
                    count = 0;
                }
            }

        });

        textToSpeechEngine = new TextToSpeech(this, status -> {
            // Establece el idioma si la inicialización es exitosa
            if (status == TextToSpeech.SUCCESS) {
                textToSpeechEngine.setLanguage(new Locale("es", "ES"));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1) {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Permiso concedido", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this,"Permiso denegado", Toast.LENGTH_SHORT);
            }
        }
    }

    private class MyRecognitionListener implements RecognitionListener {
        private static final String TAG = "MyRecognitionListener";
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
        }
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB)
        {
            /*Log.d(TAG, "onRmsChanged");*/
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndofSpeech");
        }
        public void onError(int error) {
            String errorMessage;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    errorMessage = "Error de audio";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    errorMessage = "Error del cliente";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    errorMessage = "Permisos insuficientes";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    errorMessage = "Error de red";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    errorMessage = "Tiempo de espera de red agotado";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    errorMessage = "No se encontró coincidencia";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    errorMessage = "Reconocedor ocupado";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    errorMessage = "Error del servidor";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    errorMessage = "Tiempo de espera del habla agotado";
                    break;
                default:
                    errorMessage = "Error desconocido";
                    break;
            }

            Log.e(TAG, "onError: " + errorMessage);
        }
        public void onResults(Bundle results)
        {
            ArrayList<String> data = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
            String recognizedText = data.get(0);
            sendMessage(recognizedText);
        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }

    private void initV2Chatbot() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            Log.d("Credentials: ", credentials.toString());
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();
            Log.d("ProjectID: ", projectId);
            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            Log.d("SessionsSettings: ", sessionsSettings.toString());
            sessionsClient = SessionsClient.create(sessionsSettings);
            Log.d("SessionsClient: ", sessionsClient.toString());
            session = SessionName.of(projectId, uuid);
            Log.d("Hola: ", session.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String text) {
        String msg = text;
        if (msg.trim().isEmpty()) {
            Toast.makeText(this, "¡Inserta una frase!", Toast.LENGTH_LONG).show();
        } else {
            tvBot.setText("");

            // Java V2
            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("es-ES")).build();
            String sessionNameString = session.toString();
            Log.d("Session: ", sessionNameString);
            sessionNameString = sessionsClient.toString();
            Log.d("sessionsClient: ", sessionNameString);
            sessionNameString = queryInput.toString();
            Log.d("Query input: ", sessionNameString);

            RequestJavaV2 myAsyncTask = new RequestJavaV2(this, session, sessionsClient, queryInput);

            if (myAsyncTask != null){
                myAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        }
    }

    public void callbackV2(DetectIntentResponse response) {
        if (response != null) {
            String botReply = response.getQueryResult().getFulfillmentText();
            et_text_input = botReply;
            Log.d(TAG, "V2 Bot Reply: " + botReply);
            tvBot.setText(et_text_input);
            String text = et_text_input.trim();
            speakResult = textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1");
        } else {
            Log.d(TAG, "Bot Reply: Null");
        }
    }

}