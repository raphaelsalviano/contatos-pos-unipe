package br.com.unipe.pos.raphael.contatos.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class ProductSansRegular extends AppCompatTextView {


    public ProductSansRegular(Context context) {
        super(context);
        setTypeface(context);
    }

    public ProductSansRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public ProductSansRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    private void setTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/regular.ttf"));
    }
}
