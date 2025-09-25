package cn.nahco3awa.naouc.ui.ouc.view;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.nahco3awa.naouc.network.ouc.response.data.SingleBillData;

public class ItemMyBillAdapter extends RecyclerView.Adapter<ItemMyBillAdapter.ViewHolder> {
    private final List<SingleBillData> billData;

    public ItemMyBillAdapter(List<SingleBillData> billData) {
        this.billData = billData;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return billData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void setData(SingleBillData data) {

        }
    }
}
