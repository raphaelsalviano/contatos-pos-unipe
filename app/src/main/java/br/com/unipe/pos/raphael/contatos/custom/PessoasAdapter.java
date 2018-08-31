package br.com.unipe.pos.raphael.contatos.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.unipe.pos.raphael.contatos.R;
import br.com.unipe.pos.raphael.contatos.model.Pessoa;

public class PessoasAdapter extends RecyclerView.Adapter<PessoasAdapter.PessoasViewHolder> {

    private List<Pessoa> pessoas;
    private Context context;
    private PessoaOnClickListener onClickListener;

    public PessoasAdapter(List<Pessoa> pessoas, Context context, PessoaOnClickListener onClickListener) {
        this.pessoas = pessoas;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public interface PessoaOnClickListener {
        public void onClickPessoa(View view, int index);
    }

    @Override
    public PessoasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.content_item, viewGroup, false);

        return new PessoasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PessoasViewHolder holder, final int index) {

        Pessoa pessoa = this.pessoas.get(index);

        holder.textView.setText(pessoa.getFirstName() + " " + pessoa.getLastName());

        if (onClickListener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickPessoa(holder.view, index);
                }
            });
        }

    }

    public void updatePessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
        this.notifyDataSetChanged();
    }

    public void refresh() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.pessoas != null ? this.pessoas.size() : 0;
    }

    class PessoasViewHolder extends RecyclerView.ViewHolder {

        private View view;

        private TextView textView;

        PessoasViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.item_name);
            view = itemView.findViewById(R.id.itemClick);
        }
    }

}
