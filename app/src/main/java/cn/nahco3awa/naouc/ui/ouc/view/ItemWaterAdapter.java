package cn.nahco3awa.naouc.ui.ouc.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import cn.nahco3awa.naouc.R;
import cn.nahco3awa.naouc.network.ouc.response.data.SingleWaterHzWatchData;

public class ItemWaterAdapter extends RecyclerView.Adapter<ItemWaterAdapter.ViewHolder> {
    private final List<SingleWaterHzWatchData> data;
    public ItemWaterAdapter(List<SingleWaterHzWatchData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_ouc_water, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleWaterHzWatchData datum = data.get(position);
        holder.setData(datum);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView statusTextView;
        private final TextView bookTextView;
        private SingleWaterHzWatchData data = null;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cellOucWaterNameTextView);
            statusTextView = itemView.findViewById(R.id.cellOucWaterStatusTextView);
            bookTextView = itemView.findViewById(R.id.cellOucWaterBookTextView);

            itemView.setOnClickListener(view -> {
                if (data != null) {

                }
            });
        }

        private void setData(SingleWaterHzWatchData data) {
            this.data = data;
            int color = data.Actkind() == 1 ? 0xFFF44336 : 0xFF8BC34A;
            nameTextView.setText(data.ClassName());
            statusTextView.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", data.UseFreeRate() / 100.0f) + "% 可用");
            bookTextView.setTextColor(color);
            bookTextView.setText(data.Actkind() == 1 ? "预约号：" + data.BookCode() : (data.UseFreeRate() > 0 ? "可预约" : "不可用"));
        }
    }
}
