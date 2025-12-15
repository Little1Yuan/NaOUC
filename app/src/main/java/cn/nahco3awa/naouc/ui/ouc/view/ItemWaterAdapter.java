package cn.nahco3awa.naouc.ui.ouc.view;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import cn.nahco3awa.naouc.R;
import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import cn.nahco3awa.naouc.network.ouc.request.WaterApiBookCodeCancelRequest;
import cn.nahco3awa.naouc.network.ouc.request.WaterApiBookCodeRequest;
import cn.nahco3awa.naouc.network.ouc.response.OUCCallback;
import cn.nahco3awa.naouc.network.ouc.response.WaterApiBookCodeCancelResponse;
import cn.nahco3awa.naouc.network.ouc.response.WaterApiBookCodeResponse;
import cn.nahco3awa.naouc.network.ouc.response.data.SingleWaterHzWatchData;

public class ItemWaterAdapter extends RecyclerView.Adapter<ItemWaterAdapter.ViewHolder> {
    private final List<SingleWaterHzWatchData> data;
    private final String account;
    private final Activity activity;
    public ItemWaterAdapter(List<SingleWaterHzWatchData> data, String account, Activity activity) {
        this.data = data;
        this.account = account;
        this.activity = activity;
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
        holder.setData(datum, account, activity);
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
        private String account = null;
        private boolean active;
        private Activity activity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            active = true;
            nameTextView = itemView.findViewById(R.id.cellOucWaterNameTextView);
            statusTextView = itemView.findViewById(R.id.cellOucWaterStatusTextView);
            bookTextView = itemView.findViewById(R.id.cellOucWaterBookTextView);

            itemView.setOnClickListener(view -> {
                if (data != null && active) {
                    active = false;
                    if (data.Actkind() == 0) {
                        // 预约请求
                        OUCRequestSender.getInstance().waterApiBookCode(new WaterApiBookCodeRequest(account, data.ClassNo()), new OUCCallback<>() {
                            @Override
                            public void onSuccess(WaterApiBookCodeResponse response) {
                                activity.runOnUiThread(() -> {
                                    data.setActkind(1);
                                    data.setBookCode(response.getBookCode());
                                    setData(data, account, activity);
                                });
                            }

                            @Override
                            public void onFailure(Throwable e) {
                                activity.runOnUiThread(() -> Toast.makeText(view.getContext(), "预约失败", LENGTH_SHORT).show());
                            }
                        });
                    } else {
                        // 取消预约
                        OUCRequestSender.getInstance().waterApiBookCodeCancel(new WaterApiBookCodeCancelRequest(account, data.ClassNo()), new OUCCallback<>() {
                            @Override
                            public void onSuccess(WaterApiBookCodeCancelResponse response) {
                                activity.runOnUiThread(() -> {
                                    data.setActkind(0);
                                    data.setBookCode("");
                                    setData(data, account, activity);
                                });
                            }

                            @Override
                            public void onFailure(Throwable e) {
                                activity.runOnUiThread(() -> Toast.makeText(view.getContext(), "取消预约失败", LENGTH_SHORT).show());
                            }
                        });
                    }
                }
            });
        }

        private void setData(SingleWaterHzWatchData data, String account, Activity activity) {
            this.account = account;
            this.data = data;
            this.activity = activity;
            active = true;
            int color = (data.Actkind() == 1 || data.UseFreeRate() <= 0) ? 0xFFF44336 : 0xFF8BC34A;
            nameTextView.setText(data.ClassName());
            statusTextView.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", data.UseFreeRate() / 100.0f) + "% 可用");
            bookTextView.setTextColor(color);
            bookTextView.setText(data.Actkind() == 1 ? data.BookCode() : (data.UseFreeRate() > 0 ? "可预约" : "不可用"));
        }
    }
}
