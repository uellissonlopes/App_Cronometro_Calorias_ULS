package com.made.uellisson.cronometro_calorias_mwg.webService;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Uellisson on 29/09/2016.
 *
 * Esta classe sera responsavel por gerenciar a conexao com o webservice.
 */
public class Gerencia_WebService {

    /**
     * Metodo utilzado para realizar a conexao com um webservice, usando sua url
     * passada como parametro e retornando a conexao, que sera do tipo HttpURLConnection
     *
     * @param url
     * @return conexao
     */
    public static HttpURLConnection getConexao(String url){

        HttpURLConnection conexao = null;

        /*
         usamos um bloco try ... catch para tratarmos possiveis exececos que possam ocorre na conexao
         */
        try {

            //criar o objeto que fara a conexao com o endereco passado no parametro
            URL apiEnd = new URL(url);

            //istancia o objeto que recebera a conexao, que e tratada pelo protocolo http
            conexao = (HttpURLConnection) apiEnd.openConnection();

            //configuracao dos parametros da conexao
            conexao.setRequestMethod("GET");//tipo de requisicao
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            //Estabelece a conexao
            conexao.connect();

            return conexao;
        }

        //excecao relacionada a url
        catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("Erro: ", "erro url: "+e.getMessage());
        }
        //excecao relacionada a abetura da conexao
        catch (IOException e) {
            e.printStackTrace();
            Log.d("Erro: ", "erro conexao: "+e.getMessage());
        }

        return conexao;
    }



//    public static InputStream get_is(String url){
//        URL url_ = null;
//        InputStream is = null;
//
//        try {
//            url_ = new URL(url);
//            HttpsURLConnection con = (HttpsURLConnection)url_.openConnection();
//            //InputStream conS = con.getInputStream();
//            is = new URL(url).openStream();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return is;
//    }

    /**
     * Metodo utilzado para trazer informacoes do webservice, no formato JSON, a partir da conexao
     * passada como parametro.
     * Seu retorno sera uma string, que foi convertida para facilitar o processamento da informacoes.
     *
     * A escolha de trabalharmos com um arquivo Json no padrao RESTful se da pelo fato desta aplicacao
     * ser simples, desse modo nao estaremos consumindo muitos recursos do disposiivo, como
     * seria no caso de desenvolver uma solucao com o protocolo SOAP.
     *
     * @param url
     * @return string com as informacoes trazidas do webservice.
     */
    public static String getJSON_no_WS(String url){
        String informacoes_ws = "";

        //abertura da conexao
        HttpURLConnection conexao = getConexao(url);
        int codigoRespotas = 0;
        //obejeto que recebera o conteudo do Json
        InputStream is = null;

        try {
            codigoRespotas = conexao.getResponseCode();

            //verifico se o tempo para acessar o objeto e menor que o tempo maximo para esgotar a conexao
            if (codigoRespotas < HttpURLConnection.HTTP_BAD_REQUEST){
                //adiciona o resultado da consulta ao inputstring
                is = conexao.getInputStream();
            }
            else{
                is = conexao.getErrorStream();

            }

            //converte o inputstrin em uma string, para facilitar o processamento
            informacoes_ws = converterInputStreamToString(is);
            is.close();
            conexao.disconnect();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return informacoes_ws;
    }

    /**
     * Metodo utilizado para converter um InputStream em String, que facilitara
     * o processamento das informacoes trazidas do WebService.
     *
     * @param is
     * @return
     */
    private static String converterInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while((linha = br.readLine())!=null){
                buffer.append(linha);
            }

            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();
    }

}
