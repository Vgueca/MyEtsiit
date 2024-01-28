package com.example.myetsiit.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myetsiit.R;
import com.example.myetsiit.view.adapter.NewsItem;
import com.example.myetsiit.view.adapter.NewsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final long AUTO_SCROLL_DELAY = 7000;


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;

    public HomeFragment() {
        // Constructor vacío requerido por Fragment.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // Configurar el ViewPager y el TabLayout
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        // Obtener la lista de noticias (deberías tener tu propia lógica para obtenerlas)
        List<NewsItem> newsList = new ArrayList<>();

        newsList.add(new NewsItem(R.drawable.servicio_atencion, "Servicio de Información y Atención", "\uD83C\uDF84 Servicio de Información y Atención UGR Navidad 2023 \uD83C\uDF84\n" +
                "\n" +
                "\uD83D\uDDD3\uFE0F Del 24 de diciembre al 6 de enero\n" +
                "\n" +
                "✅ Servicios y trámites disponibles\n" +
                "\n" +
                "✅ Vía de acceso centralizada (a través de formulario) para cualquier consulta"));

        newsList.add(new NewsItem(R.drawable.movilidad_internacional, "Movilidad Internacional 24/25", " Presentación de solicitudes para el curso 2024/2025\n" +
                "\n" +
                "✅ Hasta el 11 de enero de 2024\n" +
                "\n" +
                "✅ Más información: https://movilidadinternacional.ugr.es/pages/movilidad/estudiantes/erasmus"));

        newsList.add(new NewsItem(R.drawable.deportes_ugr, "Pruebas Equipo Rugby", " Pruebas de captación equipo universitario de Rugby 2023 \n" +
                "\n" +
                "\uD83D\uDDD3\uFE0F 22 de Noviembre\n" +
                "\n" +
                "✅ a las 20:00h \n" +
                "\n" +
                "✅ Campo Rugby Fuentenueva"));

        // Configurar el adaptador del ViewPager con las noticias
        NewsPagerAdapter pagerAdapter = new NewsPagerAdapter(getChildFragmentManager(), newsList);
        viewPager.setAdapter(pagerAdapter);

        // Vincular el ViewPager con el TabLayout
        tabLayout.setupWithViewPager(viewPager);

        // Iniciar el auto-scrolling
        startAutoScroll();

        return view;
    }

    // Método para iniciar el auto-scrolling
    private void startAutoScroll() {
        autoScrollHandler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager != null && viewPager.getAdapter() != null) {
                    int currentItem = viewPager.getCurrentItem();
                    int totalItems = viewPager.getAdapter().getCount();

                    // Si estamos en la última página, volver a la primera; de lo contrario, avanzar a la siguiente
                    viewPager.setCurrentItem((currentItem + 1) % totalItems);

                    autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
                }
            }
        };

        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    // Detener el auto-scrolling al pausar o detener la actividad/fragmento
    @Override
    public void onPause() {
        super.onPause();
        stopAutoScroll();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAutoScroll();
    }

    private void stopAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

}

