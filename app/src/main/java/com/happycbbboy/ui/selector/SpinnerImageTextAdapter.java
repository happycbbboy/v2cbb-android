package com.happycbbboy.ui.selector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.happycbbboy.R;

import java.util.List;

public class SpinnerImageTextAdapter extends ArrayAdapter<SpinnerImageTextAdapter.SelectorImageTextIterm> {
    private Context context;
    private List<SelectorImageTextIterm> iterm;

    public SpinnerImageTextAdapter(@NonNull Context context, int resource, List<SelectorImageTextIterm> iterm) {
        super(context, 0, iterm);
        this.context = context;
        this.iterm = iterm;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.selector_image_text_iterm, parent, false);

        TextView textView = row.findViewById(R.id.spinner_text);
        ImageView imageView = row.findViewById(R.id.spinner_image);

        SelectorImageTextIterm selectorImageTextAdapter = iterm.get(position);
        textView.setText(selectorImageTextAdapter.description);
        imageView.setImageResource(selectorImageTextAdapter.layout);

        return row;
    }

    public static class SelectorImageTextIterm {
        String description;
        int layout;
        int policy;

        public SelectorImageTextIterm(String description, int layout, int policy) {
            this.description = description;
            this.layout = layout;
            this.policy = policy;
        }

        public int getPolicy() {
            return policy;
        }

    }
}
