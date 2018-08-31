package br.com.unipe.pos.raphael.contatos;

import android.app.Application;

import java.util.List;

import br.com.unipe.pos.raphael.contatos.model.Pessoa;
import br.com.unipe.pos.raphael.contatos.service.PessoaConfig;
import br.com.unipe.pos.raphael.contatos.service.PessoaService;

public class ContatosApplication extends Application {

    private PessoaService api;

    private List<Pessoa> pessoas;

    @Override
    public void onCreate() {
        super.onCreate();

        this.api = new PessoaConfig().getPessoaService();
    }

    public PessoaService getApi() {
        return api;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
