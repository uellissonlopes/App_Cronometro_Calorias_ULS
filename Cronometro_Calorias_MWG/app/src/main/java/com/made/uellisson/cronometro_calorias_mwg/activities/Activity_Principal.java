package com.made.uellisson.cronometro_calorias_mwg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.made.uellisson.cronometro_calorias_mwg.Dominio;
import com.made.uellisson.cronometro_calorias_mwg.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Uellisson on 28/09/2016.
 *
 * Classe responsavel por criar e gerenciar a tela de com o cronometro e o contador de calorias.
 */
public class Activity_Principal extends AppCompatActivity implements View.OnClickListener {

    private Chronometer cronomotro;
    private Button bt_play;
    private Button bt_reset;
    private ImageButton ib_proximo;
    private TextView tv_contador_calorias;

    private boolean pausado = true;
    private long tempo_parado;
    private long tempo_continua;
    private long tempo_mudou_tela;
    //String calorias;

    private String pausado_am;
    private String tempo_parado_am;
    private String tempo_continua_am;
    private String tempo_mudou_tela_am;//pausa_am;
    private String calorias_am;

    private Timer timer;
    private TimerTask timerTask;
    private int passedSenconds;

    Dominio dominio = new Dominio();


    /**
     * Como O metodo OnCreate e o primeiro a ser executado inicializamos, carreamos os layout,
     * inicializamos os componentes e recuperamos algumas informaceos, quando a activite e iniciada
     * a partir da Activity_Medico.
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        cronomotro = (Chronometer) findViewById(R.id.cronomotro);
        tv_contador_calorias = (TextView) findViewById(R.id.tv_contador_calorias);

        bt_play = (Button) findViewById(R.id.bt_play);
        bt_reset = (Button) findViewById(R.id.bt_reset);
        ib_proximo = (ImageButton) findViewById(R.id.ib_proximo);

        bt_play.setOnClickListener(this);
        bt_reset.setOnClickListener(this);
        ib_proximo.setOnClickListener(this);


        //ativacao dos botoes
        play_pause(bt_play);
        reset(bt_reset);

        bt_reset.setEnabled(false);


        /*
        Recuperacao dos dados da tela anterior e execucao das operacoes necessarias,
        para preservar o estado do cronometro e do timer
         */
        Bundle bdl = getIntent().getExtras();
        if (bdl != null) {
            tempo_parado_am = bdl.getString("tempo_parado_am");
            tempo_continua_am = bdl.getString("tempo_continua_am");
            pausado_am = bdl.getString("pausado_am");
            tempo_mudou_tela_am = bdl.getString("tempo_mudou_tela_am");
            calorias_am = bdl.getString("calorias_am");


            //evento quando o cronometro retorna pausado
            if (pausado_am.equalsIgnoreCase("true")) {

                tempo_parado = Long.parseLong(tempo_parado_am);
                tempo_continua = Long.parseLong(tempo_continua_am);

                if (tempo_continua > 0) {
                    tempo_continua = System.currentTimeMillis() - tempo_continua;
                }

                cronomotro.setBase(SystemClock.elapsedRealtime() - (tempo_parado));

                tv_contador_calorias.setText(calorias_am);

                reScheduleTimer();
                timer.cancel();

                bt_reset.setEnabled(true);

                dominio.formata_cronometro(cronomotro, tempo_parado);


                //evento quando o cronometro retorna rodando
            } else {
                tempo_parado = Long.parseLong(tempo_mudou_tela_am);
                tempo_continua = Long.parseLong(tempo_continua_am);

                if (tempo_continua > 0) {
                    tempo_continua = System.currentTimeMillis() - tempo_continua;
                }

                cronomotro.setBase(SystemClock.elapsedRealtime() - (tempo_parado + tempo_continua));
                cronomotro.start();
                pausado = false;

                bt_reset.setEnabled(false);

                reScheduleTimer();

            }

        }

    }

    /**
     * Metodo que recebe um button play/pause como parametro e
     * executa as respectivas operacoes, mudando de estado a cada clique.
     *
     * @param button_pp
     */
    private void play_pause(Button button_pp) {
        button_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (pausado) {
                    /*
                    o metodo SystemClock.elapsedRealtime() retorna o tempo em milesegundos desde que o dispositivo foi
                    iniciando, entao subtraimos ele pelo tempo em melisegundao que o cronometo foi parado
                     */
                    cronomotro.setBase(SystemClock.elapsedRealtime() - tempo_parado);
                    cronomotro.start();
                    pausado = false;

                    bt_reset.setEnabled(false);

                    //inicia ou continua contador de calorias
                    reScheduleTimer();

                } else {
                    cronomotro.stop();
                     /*
                    O tempo parado ira receber a subtracao do SystemClock.elapsedRealtime() pelo cronomotro.GetBase(),
                    que retorna os milesegundos que passaram desde o inicio do cronometro
                     */
                    tempo_parado = SystemClock.elapsedRealtime() - cronomotro.getBase();

                    //pause o contador de calorias
                    timer.cancel();

                    pausado = true;
                    bt_reset.setEnabled(true);

                }
            }
        });
        dominio.formata_cronometro(cronomotro, tempo_parado);

    }


    /**
     * metodo que zera o cronometro e o contador de calorias
     *
     * @param button_res
     */
    private void reset(Button button_res) {
        button_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cronomotro.stop();

                timer.cancel();
                tv_contador_calorias.setText("0.00");

                pausado = true;

                tempo_parado = 0;
                bt_reset.setEnabled(false);

                dominio.formata_cronometro(cronomotro, tempo_parado);

            }
        });

    }

    /**
     * Metodo que salva alguns valores que precisaremos para retornar e faz a transacao
     * para a tela seguinte, com os dados do medico.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == ib_proximo) {
            Intent it = new Intent(this, Activity_Medico.class);

            //esse e o tempo no momento quando mudamos a tela
            tempo_mudou_tela = SystemClock.elapsedRealtime() - cronomotro.getBase();

            it.putExtra("tempo_parado", String.valueOf(tempo_parado));
            it.putExtra("tempo_continua", String.valueOf(tempo_continua));
            it.putExtra("tempo_mudou_tela", String.valueOf(tempo_mudou_tela));
            it.putExtra("calorias", String.valueOf(tv_contador_calorias.getText()));
            it.putExtra("pausado", String.valueOf(pausado));

            startActivity(it);

            finish();

        }

    }


    /**
     * Metodo que cria um timer, que e usado como o contador de calorias
     */
    public void reScheduleTimer() {
        timer = new Timer();
        timerTask = new Contador_de_Calorias();
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * Classe responsavel por gerenciar o nosso contador de calorias
     */
    private class Contador_de_Calorias extends TimerTask {
        /**
         * Thread que atualiza o cronometro em paralelo, para nao
         * interferir outros procedimentos
         */
        @Override
        public void run() {
            passedSenconds++;
            updateLabel.sendEmptyMessage(0);
        }
    }

    /**
     * Metodo que calcula a quantidade de calorias perdidas,
     * em determinado tempo.
     */
    private Handler updateLabel = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            tempo_parado = SystemClock.elapsedRealtime() - cronomotro.getBase();

            Double tempo_treino = ((tempo_parado * (1.7 / 60000)));
            String num_arredondado = dominio.arredondar(tempo_treino);

            tv_contador_calorias.setText(num_arredondado);
        }
    };

}
