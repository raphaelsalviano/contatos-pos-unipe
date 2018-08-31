package br.com.unipe.pos.raphael.contatos.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.unipe.pos.raphael.contatos.ContatosApplication;
import br.com.unipe.pos.raphael.contatos.R;
import br.com.unipe.pos.raphael.contatos.model.Pessoa;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements Runnable {

    private ContatosApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        application = (ContatosApplication) getApplicationContext();

        hideNavigationBar();

        AsyncTask.execute(this);
    }

    @Override
    public void run() {
        Call<List<Pessoa>> call = this.application.getApi().getAll();
        call.enqueue(new Callback<List<Pessoa>>() {
            @Override
            public void onResponse(Call<List<Pessoa>> call, Response<List<Pessoa>> response) {
                SplashActivity.this.application.setPessoas(response.body());
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<List<Pessoa>> call, Throwable t) {
                SplashActivity.this.application.setPessoas(new ArrayList<Pessoa>());
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
