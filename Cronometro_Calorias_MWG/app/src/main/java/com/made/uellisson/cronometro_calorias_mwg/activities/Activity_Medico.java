package com.made.uellisson.cronometro_calorias_mwg.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.made.uellisson.cronometro_calorias_mwg.Medico;
import com.made.uellisson.cronometro_calorias_mwg.webService.Processa_Json;
import com.made.uellisson.cronometro_calorias_mwg.R;

/**
 * Created by Uellisson on 28/09/2016.
 *
 * Classe responsavel por criar e gerenciar a tela de informcoes sobre o Medico.
 */
public class Activity_Medico extends AppCompatActivity implements View.OnClickListener{

    private Button bt_voltar;

    private String tempo_parado;
    private Long tempo_continua;
    private String tempo_mudou_tela;
    private String pausado;
    private String calorias;

    private TextView tv_nome_medico;
    private TextView tv_telefone;
    private ImageView iv_foto;

    private ProgressDialog load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);

        tv_nome_medico = (TextView) findViewById(R.id.tv_nome_medico);
        tv_telefone = (TextView) findViewById(R.id.tv_telefone);
        iv_foto = (ImageView) findViewById(R.id.iv_foto);

        bt_voltar = (Button) findViewById(R.id.bt_voltar);
        bt_voltar.setOnClickListener(this);

        Bundle bdl = getIntent().getExtras();
        tempo_parado= bdl.getString("tempo_parado");
        pausado = bdl.getString("pausado");
        tempo_mudou_tela = bdl.getString("tempo_mudou_tela");
        calorias = bdl.getString("calorias");


        tempo_continua = System.currentTimeMillis();


        GetJson ws = new GetJson();
        //Chama Async Task para fazer pegar dados do webservic
        ws.execute();

        //Toast.makeText(this, tempo_parado, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        voltar();
    }

    @Override
    public void onClick(View view) {
        if (view==bt_voltar){
            voltar();
        }
    }

    private void voltar(){
        Intent it = new Intent(this, Activity_Principal.class);

        it.putExtra("tempo_parado_am", tempo_parado);
        it.putExtra("tempo_continua_am", tempo_continua.toString());
        it.putExtra("pausado_am", pausado);
        it.putExtra("tempo_mudou_tela_am", tempo_mudou_tela);
        it.putExtra("calorias_am", calorias);

        startActivity(it);

        finish();
    }

    /**
     * Classe responsavel por fazer toda a manipulacao do webservice
     * em paralelo, para que a aplicacao nao pareca esta travada
     */
    private class GetJson extends AsyncTask<Void, Void, Medico> {

        /**
         * Primeiro metodo a ser executado, desse modo inicia a barra de progresso,
         * para que o usuario saiba que a informacao esta sendo buscada.
         *
         * */
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(Activity_Medico.this, "Acessando Informações...", "Por Favor Aguarde um Momento...");
        }


        /**
         * Metodo que acessa o webserice e retorna um objeto do tipo Medico
         *
         * @param params
         * @return
         */
        @Override
        protected Medico doInBackground(Void... params) {

            Processa_Json processa = new Processa_Json();

            /*
            url do webservice onde estao as informacoes que iremos acessar.
            Lembando que a escolha deste novo WebService se deu devido ao certificado
            do https://mwg.pro.br/teste/Medico esta vencido desde 03 de 03 de 2015.
             */
            String url_ws =  "https://randomuser.me/api/";


            return processa.getMedico(url_ws);
        }

        /**
         * Metodo executado apos pegarmos as informaceoe dos servidor,
         * desse modo ele ira setar os componetes com os devidos dados
         * e finalizar a barra de progresso.
         * @param medico
         */
        @Override
        protected void onPostExecute(Medico medico){

            tv_nome_medico.setText(medico.getNome());
            tv_telefone.setText(medico.getTelefone());
            iv_foto.setImageBitmap(medico.getFoto());

            //finaliza a barra de progresso
            load.dismiss();
        }
    }


}
