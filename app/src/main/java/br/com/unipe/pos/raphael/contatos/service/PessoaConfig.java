package br.com.unipe.pos.raphael.contatos.service;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class PessoaConfig {

    private final Retrofit retrofit;

    public PessoaConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://persons-unipe-api.herokuapp.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public PessoaService getPessoaService() {
        return this.retrofit.create(PessoaService.class);
    }
}
