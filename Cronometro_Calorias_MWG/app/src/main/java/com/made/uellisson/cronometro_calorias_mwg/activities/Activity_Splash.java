package com.made.uellisson.cronometro_calorias_mwg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.made.uellisson.cronometro_calorias_mwg.R;

/**
 * Classe responsavel por criar e gerenciar a o Splash, que aparece logo que
 * o app e iniciado.
 */
public class Activity_Splash extends AppCompatActivity {

    private int tempo_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tempo_splash = 2000;


            /*
             *  * Exibindo splash com um timer.
             */
            new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                Intent i = new Intent(Activity_Splash.this, Activity_Principal.class);
                startActivity(i);

                // Fecha esta activity
                finish();
            }
        }, tempo_splash);
    }
}
