package com.example.myetsiit.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.ActivityOptions;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.myetsiit.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class FragmentDialogFlowCopia extends FragmentActivity {

    View layout;

    private final String uuid = UUID.randomUUID().toString();
    private static final int USER = 10001;
    private static final int BOT = 10002;
    private LinearLayout chatLayout;
    private EditText queryEditText;

    // Java V2
    private SessionsClient sessionsClient;
    private SessionName session;

    //SPEECH
    private static final int PERMISSION_REQUEST_AUDIO = 1;
    private SpeechRecognizer speechRecognizer;
    private FloatingActionButton fabListening;
    private FloatingActionButton back_home;
    private TextToSpeech textToSpeechEngine;
    private String et_text_input;
    private int speakResult;
    private double destinationLatitude = 37.182965053779945; // predefined latitude
    private double destinationLongitude = -3.6051149021000355; // predefined longitude
    private String location = null;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private ActivityOptions animacion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialogflow);

        final ScrollView scrollview = findViewById(R.id.chatScrollView);
        scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

        chatLayout = findViewById(R.id.chatLayout);

        layout = findViewById(R.id.dialogflow);

        queryEditText = findViewById(R.id.queryEditText);
        queryEditText.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        sendMessage();
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_AUDIO);
            }
        }
        if (ContextCompat.checkSelfPermission(FragmentDialogFlowCopia.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FragmentDialogFlowCopia.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        fabListening = findViewById(R.id.fb_listening);
        back_home = findViewById(R.id.volver);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                if (speakResult == TextToSpeech.SUCCESS) {
                    // Para detener la reproducción, puedes llamar a stop() en cualquier momento
                    textToSpeechEngine.stop();
                } else {
                    Log.e("TextToSpeech", "Error during speak operation");
                }
                queryEditText.setText("");
                queryEditText.setHint(getString(R.string.escuchando));
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                queryEditText.setText(data.get(0));
                queryEditText.setHint("");
                sendMessage();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        fabListening.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    speechRecognizer.startListening(speechRecognizerIntent);
                }

                return false;
            }
        });

        back_home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                animacion = ActivityOptions.makeCustomAnimation(FragmentDialogFlowCopia.this, R.anim.fade_in, R.anim.fade_out);
                startActivity(home,animacion.toBundle());
                return false;
            }
        });

        // Inicializar Chatbot con DialogFlow
        initV2Chatbot();

        textToSpeechEngine = new TextToSpeech(this, status -> {
            // Establece el idioma si la inicialización es exitosa
            if (status == TextToSpeech.SUCCESS) {
                textToSpeechEngine.setLanguage(new Locale("es", "ES"));
            }
        });


    }//Final del onCreate

    private void initV2Chatbot() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String msg = queryEditText.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast vacia = Toast.makeText(getApplicationContext(), getString(R.string.query_vacia), Toast.LENGTH_SHORT);
            vacia.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            vacia.show();
        } else {
            showTextView(msg, USER);
            queryEditText.setText("");

            // Java V2
            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("es-ES")).build();
            new RequestJavaV2(FragmentDialogFlowCopia.this, session, sessionsClient, queryInput).execute();
        }
    }

    public void callbackV2(DetectIntentResponse response) {
        if (response != null) {
            // process aiResponse here
            String botReply = response.getQueryResult().getFulfillmentText();
            et_text_input = botReply;
            String text = et_text_input.trim();
            if(et_text_input != null) {
                speakResult = textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1");
            }
            Log.d("TAG-BOT", "V2 Bot Reply: " + botReply);
            showTextView(botReply, BOT);
            manageResults(response);
        } else {
            Log.d("TAG-BOT", "Bot Reply: Null");
            showTextView(getString(R.string.error_chat_conection), BOT);
        }
    }

    private void showTextView(String message, int type) {
        FrameLayout f_layout;
        switch (type) {
            case USER:
                f_layout = getUserLayout();
                break;
            case BOT:
                f_layout = getBotLayout();
                break;
            default:
                f_layout = getBotLayout();
                break;
        }
        f_layout.setFocusableInTouchMode(true);
        chatLayout.addView(f_layout, 0); // move focus to text view to automatically make it scroll up if softfocus
        TextView tv = f_layout.findViewById(R.id.chatMsg);
        tv.setText(message);
        f_layout.requestFocus();
        queryEditText.requestFocus(); // change focus back to edit text to continue typing
    }

    private void manageResults(DetectIntentResponse response){
        // Menús
        if(response.getQueryResult().getAction().equals("ParaLlevar")){
            Log.d("TAG-BOT", "Action: " + response.getQueryResult().getAction());
            Intent comedor_llevar = new Intent(getApplicationContext(), ComedorLlevarActivity.class);
            startActivity(comedor_llevar);
        } else if(response.getQueryResult().getAction().equals("Presencial")){
            Intent comedor_presencial = new Intent(getApplicationContext(), ComedorActivity.class);
            startActivity(comedor_presencial);
        } else if(response.getQueryResult().getAction().equals("Comedores")) {
            location = getLocation(response.getQueryResult().getParameters().getFieldsMap().get("localizaciones_comedores").toString());
        } else if(response.getQueryResult().getAction().equals("Miscelanea")) {
            location = getLocation(response.getQueryResult().getParameters().getFieldsMap().get("localizaciones_miscelanea").toString());
        } else if(response.getQueryResult().getAction().equals("CAD")) {
            location = getLocation(response.getQueryResult().getParameters().getFieldsMap().get("localizaciones_cad").toString());
        } else if(response.getQueryResult().getAction().equals("Salir")) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            animacion = ActivityOptions.makeCustomAnimation(FragmentDialogFlowCopia.this, R.anim.fade_in, R.anim.fade_out);
            startActivity(home,animacion.toBundle());
        }

        // Localizaciones
        Log.d("TAG-BOT", "Location " + location);

        if(location!=null){
            setCoordinates();
            Log.d("TAG-BOT", "Localizacion1");
            getCurrentLocation();
        }


    }

    private void setCoordinates(){
        // establecemos las coordenadas de destino
        if(location.equals("Rectorado")){
            destinationLatitude= 37.18493014764161;
            destinationLongitude = -3.601100820034889;
        }

        if(location.equals("Servicio de Becas")){
            destinationLatitude = 37.18708940327209;
            destinationLongitude = -3.6041243893446135;
        }

        if(location.equals("Oficina RRII")){
            destinationLatitude = 37.192547979097824;
            destinationLongitude = -3.5947931777069844;
        }

        if(location.equals("Tienda UGR")){
            destinationLatitude = 37.175585506831005;
            destinationLongitude = -3.5968521777079423;
        }

        if(location.equals("CAD Cartuja")){
            destinationLatitude = 37.19061721651888;
            destinationLongitude = -3.598880677707074;
        }

        if(location.equals("CAD Fuentenueva")){
            destinationLatitude = 37.18260366131891;
            destinationLongitude = -3.609130819272596;
        }

        if (location.equals("Comedor Fuentenueva")) {
            destinationLatitude = 37.182965053779945;
            destinationLongitude = -3.6051149021000355;
        }

        if(location.equals("Comedor Aynadamar")){
            destinationLatitude = 37.19692720366017;
            destinationLongitude = -3.6242796181803074;
        }

        if(location.equals("Comedor PTS")){
            destinationLatitude = 37.14839096441252;
            destinationLongitude = -3.6036596740017384;
        }

        if(location.equals("Comedor Cartuja")){
            destinationLatitude = 37.19205973957023;
            destinationLongitude = -3.597918604689388;
        }
    }

    private String getLocation(String response) {
        String location = null;
        Log.d("TAG-BOT", "Response " + response);
        if(response.contains("Comedor PTS")) {location = "Comedor PTS";}
        else if(response.contains("Comedor Cartuja")) {location = "Comedor Cartuja";}
        else if(response.contains("Comedor Aynadamar")) {location = "Comedor Aynadamar";}
        else if(response.contains("Comedor Fuentenueva")) {location = "Comedor Fuentenueva";}
        else if(response.contains("CAD Fuentenueva")){location = "CAD Fuentenueva";}
        else if(response.contains("CAD Cartuja")) {location = "CAD Cartuja";}
        else if(response.contains("Rectorado")) {location = "Rectorado";}
        else if(response.contains("Servicio de Becas")){location = "Servicio de Becas";}
        else if(response.contains("Oficina RRII")) {location = "Oficina RRII";}
        else if(response.contains("Tienda UGR")) {location = "Tienda UGR";}

        return location;
    }

    FrameLayout getUserLayout() {
        LayoutInflater inflater = LayoutInflater.from(FragmentDialogFlowCopia.this);
        return (FrameLayout) inflater.inflate(R.layout.layout_mensaje_user, null);
    }

    FrameLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(FragmentDialogFlowCopia.this);
        return (FrameLayout) inflater.inflate(R.layout.layout_mensaje_bot, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQUEST_AUDIO && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, getString(R.string.audio_permission_granted), Toast.LENGTH_SHORT).show();
                onRestart();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();

    }

    protected void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d("TAG-BOT", "Localizacion2");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Use the location object to get latitude, longitude, etc.
                        navigateToLocation(location.getLatitude(), location.getLongitude());
                    } else {
                        // Handle the situation where location data is not available
                    }
                }
            });
        }
    }

    // Method to navigate to a predefined location using the current location
    protected void navigateToLocation(double currentLatitude, double currentLongitude) {

        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="
                + currentLatitude + "," + currentLongitude
                + "&destination=" + destinationLatitude + "," + destinationLongitude
                + "&travelmode=driving");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        Log.d("TAG-BOT", "Localizacion3");

        try {
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Log.d("MapIntent", "No activity found to handle map intent");
            }
        } catch (Exception e) {
            Log.e("MapIntentError", "Error starting map activity", e);
        }
    }
}
