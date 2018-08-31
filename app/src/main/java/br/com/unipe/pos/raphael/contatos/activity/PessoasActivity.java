package br.com.unipe.pos.raphael.contatos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.unipe.pos.raphael.contatos.ContatosApplication;
import br.com.unipe.pos.raphael.contatos.R;
import br.com.unipe.pos.raphael.contatos.model.Pessoa;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PessoasActivity extends AppCompatActivity {

    private String screen;
    private int idPessoa;

    private ContatosApplication application;

    private View createOrUpdateView;
    private View viewPeople;
    private View progress;

    private Pessoa pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pessoas);

        application = (ContatosApplication) getApplicationContext();

        createOrUpdateView = findViewById(R.id.editOrCreatePessoas);
        viewPeople = findViewById(R.id.viewPessoas);
        progress = findViewById(R.id.load);

        this.screen = "";
        if (getIntent().getExtras() != null) {
            this.screen = getIntent().getExtras().getString("screen");
            this.idPessoa = getIntent().getExtras().getInt("id");
        }

        this.pessoa = this.application.getPessoas().get(this.idPessoa);

        Toolbar toolbar = findViewById(R.id.toolbarPessoas);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (screen.equalsIgnoreCase("editar")) {
                getSupportActionBar().setTitle("Editar pessoa");
                this.createOrUpdateView.setVisibility(View.VISIBLE);
                this.viewPeople.setVisibility(View.GONE);
            } else if (screen.equalsIgnoreCase("view")) {
                getSupportActionBar().setTitle("");
                this.createOrUpdateView.setVisibility(View.GONE);
                this.viewPeople.setVisibility(View.VISIBLE);
            } else if (screen.equalsIgnoreCase("add")) {
                getSupportActionBar().setTitle("Adicionar pessoa");
                this.createOrUpdateView.setVisibility(View.VISIBLE);
                this.viewPeople.setVisibility(View.GONE);
            }
        }

        if (this.screen.equalsIgnoreCase("editar")) {

            final EditText firstName = findViewById(R.id.firstNameEditText);
            firstName.setText(this.pessoa.getFirstName());

            final EditText lastName = findViewById(R.id.lastNameEditText);
            lastName.setText(this.pessoa.getLastName());

            Button button = findViewById(R.id.btn_salvar);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PessoasActivity.this.pessoa.setFirstName(firstName.getText().toString());
                    PessoasActivity.this.pessoa.setLastName(lastName.getText().toString());

                    Call<Pessoa> call = PessoasActivity.this.application.getApi().updatePerson(PessoasActivity.this.pessoa.getId(), PessoasActivity.this.pessoa);
                    call.enqueue(new Callback<Pessoa>() {
                        @Override
                        public void onResponse(Call<Pessoa> call, Response<Pessoa> response) {
                            PessoasActivity.this.refreshPessoas();

                            Pessoa pessoaAtualizada = response.body();
                            PessoasActivity.this.pessoa = pessoaAtualizada;

                            Toast.makeText(PessoasActivity.this, "Pessoa atualizada! Puxe para atualizar", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PessoasActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Pessoa> call, Throwable t) {
                            PessoasActivity.this.refreshPessoas();

                            Toast.makeText(PessoasActivity.this, "Ocorreu um problema ao salvar", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PessoasActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            });

        } else if (this.screen.equalsIgnoreCase("add")) {

            final EditText firstName = findViewById(R.id.firstNameEditText);

            final EditText lastName = findViewById(R.id.lastNameEditText);

            Button button = findViewById(R.id.btn_salvar);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Pessoa createPessoa = new Pessoa();
                    createPessoa.setFirstName(firstName.getText().toString());
                    createPessoa.setLastName(lastName.getText().toString());

                    Call<Pessoa> call = PessoasActivity.this.application.getApi().createPerson(createPessoa);
                    call.enqueue(new Callback<Pessoa>() {
                        @Override
                        public void onResponse(Call<Pessoa> call, Response<Pessoa> response) {
                            PessoasActivity.this.refreshPessoas();

                            Toast.makeText(PessoasActivity.this, "Pessoa criada! Puxe para atualizar", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PessoasActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Pessoa> call, Throwable t) {
                            PessoasActivity.this.refreshPessoas();

                            Toast.makeText(PessoasActivity.this, "Ocorreu um problema ao salvar", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PessoasActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            });

        } else if (this.screen.equalsIgnoreCase("view")) {
            TextView nome = findViewById(R.id.nome);
            nome.setText(this.pessoa.getFirstName());

            TextView sobrenome = findViewById(R.id.sobrenome);
            sobrenome.setText(this.pessoa.getLastName());

            TextView criadoEm = findViewById(R.id.criadoEm);
            criadoEm.setText(this.pessoa.getCreatedAt().toString());

            TextView atualizadoEm = findViewById(R.id.atualizadoEm);
            atualizadoEm.setText(this.pessoa.getUpdatedAt().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.screen.equalsIgnoreCase("editar")) {
            getMenuInflater().inflate(R.menu.menu_create_or_update, menu);
        } else if (this.screen.equalsIgnoreCase("add")) {
            getMenuInflater().inflate(R.menu.menu_create_or_update, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_pessoas, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent;

            if (this.screen.equalsIgnoreCase("Editar")) {
                intent = new Intent(this, PessoasActivity.class);
                intent.putExtra("screen", "View");
                intent.putExtra("id", PessoasActivity.this.idPessoa);
            } else {
                intent = new Intent(this, MainActivity.class);
            }

            startActivity(intent);
            finish();
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(this, PessoasActivity.class);
            intent.putExtra("screen", "Editar");
            intent.putExtra("id", PessoasActivity.this.idPessoa);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setMessage("Tem certeza que deseja remover este contato?")
                    .setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Call<ResponseBody> call = PessoasActivity.this.application.getApi().deletePerson((long) PessoasActivity.this.pessoa.getId());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    PessoasActivity.this.refreshPessoas();
                                    Toast.makeText(PessoasActivity.this, "Pessoa removida! Puxe para atualizar", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PessoasActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(PessoasActivity.this, "Ocorreu um erro ao remover", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PessoasActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }).setNegativeButton("Cancelar", null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshPessoas() {

        Call<List<Pessoa>> call = PessoasActivity.this.application.getApi().getAll();
        call.enqueue(new Callback<List<Pessoa>>() {
            @Override
            public void onResponse(Call<List<Pessoa>> call, Response<List<Pessoa>> response) {
                PessoasActivity.this.application.setPessoas(response.body());
            }

            @Override
            public void onFailure(Call<List<Pessoa>> call, Throwable t) {
                PessoasActivity.this.application.setPessoas(new ArrayList<Pessoa>());
            }
        });

    }
}
