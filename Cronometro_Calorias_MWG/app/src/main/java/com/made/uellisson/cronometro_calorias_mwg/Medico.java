package com.made.uellisson.cronometro_calorias_mwg;

import android.graphics.Bitmap;

/**
 * Created by Uellisson on 29/09/2016.
 *
 * Classe modelo, que serve para criamos um obejto do tipo Medico
 */

public class Medico {
    private String nome;
    private String telefone;
    private Bitmap foto;

    public Medico(String nome, String telefone, Bitmap foto) {
        this.nome = nome;
        this.telefone = telefone;
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public Bitmap getFoto() {
        return foto;
    }
}
