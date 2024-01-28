package com.example.myetsiit.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myetsiit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.view.View;


public class ComedorLlevarActivity extends AppCompatActivity {
    private WebView webViewComedor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comedorllevar);

        webViewComedor = findViewById(R.id.webviewComedorLlevar);
        webViewComedor.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Inject JavaScript to display only the specific part
                webViewComedor.evaluateJavascript(
                        "(function() { " +
                                "    var title = document.querySelector('#contenido > div.content_doku > h1:nth-child(3)');" +
                                "    var table = document.querySelector('#contenido > div.content_doku > div:nth-child(5) > table');" +
                                "    if (title && table) {" +
                                "        var style = '<style>" +
                                "            .customDayStyle { font-weight: bold; color: white; background-color: #C20000; border-top: 20px solid white; }" + // Dark red background with white bold text and top white border
                                "            .customMenuCell { font-size: larger; color: black; background-color: #A9A9A9; text-align: center; }" + // Bigger black text in silver background cell
                                "        </style>';" +
                                "        var daysOfWeek = ['LUNES', 'MARTES', 'MIÉRCOLES', 'JUEVES', 'VIERNES', 'SÁBADO'];" +
                                "        for (var i = 0; i < table.rows.length; i++) {" +
                                "            var dayCell = table.rows[i].querySelector('th.leftalign > strong');" +
                                "            if (dayCell && daysOfWeek.some(day => dayCell.textContent.includes(day))) {" +
                                "                for (var j = 0; j < table.rows[i].cells.length; j++) {" +
                                "                    table.rows[i].cells[j].className += ' customDayStyle';" + // Apply styles to each cell in the row
                                "                }" +
                                "            }" +
                                "            var menuCell = table.rows[i].querySelector('td > strong');" +
                                "            if (menuCell && menuCell.textContent.includes('Menú')) {" +
                                "                var parentTd = menuCell.parentElement;" +
                                "                parentTd.className += ' customMenuCell';" + // Apply styles to the cell
                                "            }" +
                                "        }" +
                                "        document.body.innerHTML = style + '<h1>' + title.innerHTML + '</h1>' + '<table>' + table.outerHTML + '</table>';" +
                                "    } else {" +
                                "        console.error('Title or table not found');" +
                                "    }" +
                                "})();",
                        null);
            }
        });

        webViewComedor.getSettings().setJavaScriptEnabled(true);  // Enable JavaScript
        webViewComedor.loadUrl("https://scu.ugr.es/pages/menu/llevar");  // Load the specific website

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewComedorLlevar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    switchToMainActivity(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_profile) {
                    switchToMainActivity(new ProfileFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_chatbot) {
                    // Si es "Localización", cambiar a LocalizationFragment
                    switchTo(new FragmentDialogFlow());
                    return true;
                }
                return false;
            }
        });


        Button btnNavigateLlevar = findViewById(R.id.btnNavigate);
        btnNavigateLlevar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComedorLlevarActivity.this, ComedorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void switchToMainActivity(Fragment fragment) {
        Intent intent = new Intent(ComedorLlevarActivity.this, MainActivity.class);
        intent.putExtra("fragmentToLoad", fragment.getClass().getSimpleName());
        startActivity(intent);
        finish();  // Finalizar ComedorActivity
    }
    private void switchTo(FragmentActivity fragment) {
        Intent intent = new Intent(ComedorLlevarActivity.this, MainActivity.class);
        intent.putExtra("fragmentToLoad", fragment.getClass().getSimpleName());
        startActivity(intent);
        finish();  // Finalizar LocalizationActivity
    }

    @Override
    public void onBackPressed() {
        if (webViewComedor.canGoBack()) {
            webViewComedor.goBack();  // Go back in WebView history
        } else {
            super.onBackPressed();
        }
    }
}

