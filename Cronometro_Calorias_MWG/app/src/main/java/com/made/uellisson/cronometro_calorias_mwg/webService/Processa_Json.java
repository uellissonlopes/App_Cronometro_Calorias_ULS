package com.made.uellisson.cronometro_calorias_mwg.webService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.made.uellisson.cronometro_calorias_mwg.Medico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Uellisson on 29/09/2016.
 *
 * Esta classe e respondavel pelos metodos que analisam e processam o arquivo josn,
 * para exibicao das suas informacoes posteriormente.
 *
 */
public class Processa_Json {

    /**
     * Metodo que  e processa o arquivo json, passado como parametro
     * em formato String e retorna um objeto Medico.
     *
     * @param json_string
     * @return json_string
     */
    private Medico processa_Json(String json_string){


        Medico medico = null;

        JSONObject jsonObj = null;
        JSONArray array = null;
        JSONObject objArray = null;


        try {

            jsonObj = new JSONObject(json_string);

            //todas as informacoes do servidor estao em um array chamados results
            array = jsonObj.getJSONArray("results");

            objArray = array.getJSONObject(0);

            //dentro de results existe outro array nome, do qual pegamos o nome e sobrenome do medico
            JSONObject array_nome = objArray.getJSONObject("name");
            String nome = array_nome.getString("first");
            String sobrenome = array_nome.getString("last");

            //o telefones esta no array results
            String telefone = objArray.getString("phone");

            //muda a primeira letra do nome para maiuscula
            nome = nome.substring(0,1).toUpperCase()+nome.substring(1);
            sobrenome = sobrenome.substring(0,1).toUpperCase()+sobrenome.substring(1);

            //Pega Imagem do medico, que esta no array picture
            JSONObject array_foto = objArray.getJSONObject("picture");
            Bitmap foto = baixarImagem(array_foto.getString("large"));

            medico = new Medico(nome+" "+sobrenome, telefone, foto);

            return medico;
        }

        catch (JSONException e){
            e.printStackTrace();
            Log.d("Erro", "erro null "+e.getMessage());
            return null;
        }

    }

    /**
     * Faz o dawnload da imagem aparir da sua url e retorna uma imagem Bitmap
     * @param url
     * @return Bitmap
     */
    private Bitmap baixarImagem(String url) {
        try{
            URL endereco;
            InputStream inputStream;
            Bitmap imagem;
            endereco = new URL(url);
            inputStream = endereco.openStream();
            imagem = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return imagem;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo que usa uma URL passara como parametro para retornar
     * um objeto do tipo Medico, com os dados trazidas do WebService,
     * pelo metodo getJSON_no_WS(), da classe Gerencia_WebService
     * e tradados pelo processa_Json, criado nesta classe.
     *
     * @param url
     * @return Medico
     */
    public Medico getMedico(String url){
        String json;
        Medico medico;

        json = Gerencia_WebService.getJSON_no_WS(url);
        Log.i("Resultado", json);

        medico = processa_Json(json);

        return medico;
    }

}
