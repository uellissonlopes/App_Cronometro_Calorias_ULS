package com.made.uellisson.cronometro_calorias_mwg;

import android.os.SystemClock;
import android.widget.Chronometer;

import java.math.BigDecimal;

/**
 * Created by Uellisson on 29/09/2016.
 *
 * Classe respondavel por alguns metodos auxiliares que serao usados na Activity_Prinicipal.
 * Seu objetivo é deixar a classe Activity_Prinicipal mais curta e simples.
 *
 */

public class Dominio {

    /**
     * Recebe como parametro um cronometro e muda seu padrao para 00:00:00
     * @param cron
     */
    public void formata_cronometro(final Chronometer cron, long tempo_parado){

        //usamos para carregar o cronometro
        cron.setBase(SystemClock.elapsedRealtime()-tempo_parado);
        cron.start();
        cron.stop();

        cron.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                CharSequence display = String.valueOf(cron.getText());

                if (display.length()  == 5) {
                    chronometer.setText("00:" + display);
                }
                else if (display.length() == 7) {
                    chronometer.setText("0" + display);
                }
            }
        });


    }


    /**
     * Metodo utilizado para arredondar um double, que e passado com parametro,
     * como geralmente arredondamos um numero para exibir o metodo decolve o numero
     * arredondado em formato String
     * @param numero
     * @return String arredondado
     */
    public String arredondar(Double numero){

        BigDecimal saidas_arr = new BigDecimal(numero);
        saidas_arr = saidas_arr.setScale(2, BigDecimal.ROUND_HALF_UP);
        String saida = saidas_arr.toString();

        /*
        As operaceos abaixo sao usadas para encontar o ponto que representa as casas decimais do numero
        e ver quantos digitos tem apos ele, caso tenha epenas um, adicionamos o 0.
        Isso ocorre porque o qualquer regra de arredondamento elimina o ultimo 0.
         */
        int tamanho_saida = saida.length();
        int ponto = 0;
        while (ponto< tamanho_saida && !saida.substring(ponto, ponto+1).equalsIgnoreCase(".")){
            ponto++;
        }

        if (tamanho_saida>ponto+3){
            saida = saida+"0";
        }

        return saida;
    }
}
