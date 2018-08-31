package br.com.unipe.pos.raphael.contatos.service;

import java.util.List;

import br.com.unipe.pos.raphael.contatos.model.Pessoa;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PessoaService {

    @GET("api/v1.0/persons")
    Call<List<Pessoa>> getAll();

    @GET("api/v1.0/persons/{id}")
    Call<Pessoa> getPerson(@Path("id") Long id);

    @PUT("api/v1.0/persons/{id}")
    Call<Pessoa> updatePerson(@Path("id") Long id, @Body Pessoa pessoa);

    @POST("api/v1.0/persons")
    Call<Pessoa> createPerson(@Body Pessoa pessoa);

    @DELETE("api/v1.0/persons/{id}")
    Call<ResponseBody> deletePerson(@Path("id") Long id);

}
