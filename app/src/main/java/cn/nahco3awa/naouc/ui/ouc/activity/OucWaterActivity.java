package cn.nahco3awa.naouc.ui.ouc.activity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.nahco3awa.naouc.R;
import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import cn.nahco3awa.naouc.network.ouc.request.WaterApiAccUseHzWatchRequest;
import cn.nahco3awa.naouc.network.ouc.response.OUCCallback;
import cn.nahco3awa.naouc.network.ouc.response.WaterApiAccUseHzWatchResponse;
import cn.nahco3awa.naouc.ui.ouc.view.ItemWaterAdapter;

public class OucWaterActivity extends AppCompatActivity {
    private String account = null;
    private RecyclerView waterListView;
    private ItemWaterAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouc_water);

        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        adapter = new ItemWaterAdapter(new ArrayList<>(), account, this);

        waterListView = findViewById(R.id.waterListView);
        waterListView.setVisibility(INVISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        waterListView.setLayoutManager(layoutManager);
        waterListView.setAdapter(adapter);

        refreshWaterList();
    }

    private void refreshWaterList() {
        waterListView.setVisibility(INVISIBLE);
        OUCRequestSender.getInstance().waterApiAccUseHzWatch(new WaterApiAccUseHzWatchRequest(account), new OUCCallback<>() {
            @Override
            public void onSuccess(WaterApiAccUseHzWatchResponse response) {
                runOnUiThread(() -> {
                    adapter = new ItemWaterAdapter(response.getData(), account, OucWaterActivity.this);
                    waterListView.setAdapter(adapter);
                    waterListView.setVisibility(VISIBLE);
                });
            }

            @Override
            public void onFailure(Throwable e) {
                runOnUiThread(() -> {
                    waterListView.setVisibility(INVISIBLE);
                    Toast.makeText(OucWaterActivity.this, "刷新列表失败：" + e.getMessage(), LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshWaterList();
    }
}