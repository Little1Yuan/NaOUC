package cn.nahco3awa.naouc.ui.ouc.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import cn.nahco3awa.naouc.R;
import cn.nahco3awa.naouc.network.ouc.response.data.SingleBillData;

public class ItemMyBillAdapter extends RecyclerView.Adapter<ItemMyBillAdapter.ViewHolder> {
    private final List<SingleBillData> billData;

    public ItemMyBillAdapter(List<SingleBillData> billData) {
        this.billData = billData;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_my_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleBillData data = billData.get(position);
        holder.setData(data);
    }

    @Override
    public int getItemCount() {
        return billData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconImageView;
        private final TextView tranNameTextView;
        private final TextView mercnNameTextView;
        private final TextView amountTextView;
        private final TextView balanceTextView;
        private SingleBillData data = null;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.cellMyBillImageView);
            tranNameTextView = itemView.findViewById(R.id.cellMyBillTranNameTextView);
            mercnNameTextView = itemView.findViewById(R.id.cellMyBillMercnNameTextView);
            amountTextView = itemView.findViewById(R.id.cellMyBillAmountTextView);
            balanceTextView = itemView.findViewById(R.id.cellMyBillBalanceTextView);

            itemView.setOnClickListener(view -> {
                if (data != null) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("消费详情")
                            .setMessage("订单时间：" + data.OCCTIME +
                                    "\n生效时间：" + data.EFFECTDATE +
                                    "\n流水金额：" + getFormat(data.TRANAMT) +
                                    "\n账户余额：" + getFormat(data.CARDBAL) +
                                    "\n支付地点：" + data.MERCNAME +
                                    "\n\n" + data.JDESC)
                            .setNegativeButton("好",null)
                            .show();
                }
            });
        }

        private void setData(SingleBillData data) {
            this.data = data;
             float amt = data.TRANAMT;
             int color = amt < 0 ? 0xFFF44336 : 0xFF8BC34A;
             iconImageView.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
             amountTextView.setTextColor(color);
             amountTextView.setText(getFormat(amt));
             tranNameTextView.setText(data.TRANNAME);
             mercnNameTextView.setText(data.MERCNAME);
             balanceTextView.setText("余额：" + getFormat(data.CARDBAL));
        }

        @NonNull
        private static String getFormat(float amt) {
            return String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", amt);
        }
    }
}
