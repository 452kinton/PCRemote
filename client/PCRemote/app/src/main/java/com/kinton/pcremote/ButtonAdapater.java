package com.kinton.pcremote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ButtonAdapater extends RecyclerView.Adapter<ButtonAdapater.ButtonHodler> {
    private Context context;
    private List<ButtonInfo> buttonInfos;
    public ButtonAdapater(Context context, List<ButtonInfo> buttonInfos,ItemClickListener listener){
        this.context = context;
        this.buttonInfos = buttonInfos;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ButtonHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_button_item,viewGroup,false);
        ButtonHodler buttonHodler = new ButtonHodler(view);
        return buttonHodler;
    }

    @Override
    public void onBindViewHolder(@NonNull final ButtonHodler viewHolder, int i) {
        ButtonInfo buttonInfo = buttonInfos.get(i);
        viewHolder.tvContent.setText("");
        viewHolder.ivIcon.setImageBitmap(null);
        Log.i("type",buttonInfo.getType()+"");
        if(buttonInfo.getType() == ButtonInfo.TYPE_TEXT){
            viewHolder.tvContent.setText(buttonInfo.getContent());
        }else{
            viewHolder.ivIcon.setImageResource(buttonInfo.getRes_id());
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return buttonInfos.size();
    }

    public class ButtonHodler extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private TextView tvContent;
        public ButtonHodler(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.textView);
            ivIcon = itemView.findViewById(R.id.imageView);
        }
    }
    private ItemClickListener listener;

    public interface ItemClickListener{
        void onItemClick(int position);
    }
}
