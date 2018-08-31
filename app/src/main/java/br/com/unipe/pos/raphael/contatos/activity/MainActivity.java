package br.com.unipe.pos.raphael.contatos.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.unipe.pos.raphael.contatos.ContatosApplication;
import br.com.unipe.pos.raphael.contatos.R;
import br.com.unipe.pos.raphael.contatos.custom.PessoasAdapter;
import br.com.unipe.pos.raphael.contatos.custom.UpDownRecyclerScroll;
import br.com.unipe.pos.raphael.contatos.model.Pessoa;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ContatosApplication application;

    private SwipeRefreshLayout refreshLayout;
    private PessoasAdapter pessoasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.application = (ContatosApplication) getApplicationContext();

        List<Pessoa> pessoas = application.getPessoas();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PessoasActivity.class);
                intent.putExtra("screen", "Add");
                startActivity(intent);
                finish();
            }
        });

        pessoasAdapter = new PessoasAdapter(pessoas, this, onClickListener());

        refreshLayout = findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPessoas();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(pessoasAdapter);
        recyclerView.addOnScrollListener(new UpDownRecyclerScroll() {
            @Override
            public void show() {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                fab.animate().translationY(fab.getHeight() + 30).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });
    }

    private PessoasAdapter.PessoaOnClickListener onClickListener() {
        return new PessoasAdapter.PessoaOnClickListener() {
            @Override
            public void onClickPessoa(View view, int index) {
                Intent intent = new Intent(MainActivity.this, PessoasActivity.class);
                intent.putExtra("screen", "View");
                intent.putExtra("id", index);
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_refresh) {
            refreshLayout.setRefreshing(true);
            refreshPessoas();
            return true;
        } else if (id == R.id.action_about) {
            new AlertDialog.Builder(this).setTitle("Sore o aplicativo").setMessage("Este aplicativo foi desenvolvido por Raphael Salviano Trajano da Silva, como avaliação da disciplina Introdução a Android da Pós Graduação em Desevolvimento Mobile do Unipe João Pessoa.")
                    .setPositiveButton("Ok", null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshPessoas() {

        Call<List<Pessoa>> call = MainActivity.this.application.getApi().getAll();
        call.enqueue(new Callback<List<Pessoa>>() {
            @Override
            public void onResponse(Call<List<Pessoa>> call, Response<List<Pessoa>> response) {
                MainActivity.this.application.setPessoas(response.body());
                MainActivity.this.pessoasAdapter.updatePessoas(response.body());
                MainActivity.this.pessoasAdapter.notifyDataSetChanged();
                MainActivity.this.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Pessoa>> call, Throwable t) {
                MainActivity.this.application.setPessoas(new ArrayList<Pessoa>());
                MainActivity.this.pessoasAdapter.refresh();
                MainActivity.this.pessoasAdapter.notifyDataSetChanged();
                MainActivity.this.refreshLayout.setRefreshing(false);
            }
        });

    }
}
