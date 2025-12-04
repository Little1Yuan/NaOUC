package cn.nahco3awa.naouc.ui.ouc.activity;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import cn.nahco3awa.naouc.R;
import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import cn.nahco3awa.naouc.network.ouc.request.NetGdcOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.TsmOUCRequest;
import cn.nahco3awa.naouc.network.ouc.response.NetGdcOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.OUCCallback;
import cn.nahco3awa.naouc.network.ouc.response.TsmOUCResponse;

public class OucNetActivity extends AppCompatActivity {
    private TextView netStatusTextView;
    private TextView netBalanceTextView;
    private String account;
    private String sno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouc_net);
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        sno = intent.getStringExtra("sno");
        netStatusTextView = findViewById(R.id.netStatusTextView);
        netBalanceTextView = findViewById(R.id.netBalanceBigTextView);
        refreshBalance();
    }

    private void refreshBalance() {
        OUCRequestSender.getInstance().tsm(new TsmOUCRequest(account, sno), new OUCCallback<>() {
            @Override
            public void onSuccess(TsmOUCResponse response) {
                runOnUiThread(() -> {
                    netBalanceTextView.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", response.getBalance()));
                    netStatusTextView.setText(response.isFrozen() ? "校园网停机" : "校园网余额");
                    if (response.isFrozen()) netStatusTextView.setTextColor(0xFFF44336);
                });
            }

            @Override
            public void onFailure(Throwable e) {
                runOnUiThread(() -> {
                    netStatusTextView.setText("查询失败");
                    netBalanceTextView.setText("");
                });
            }
        });
    }

    public void onClickPay(View view) {
        EditText editText = new EditText(this);
        editText.setHint("请输入金额（元）：");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        new AlertDialog.Builder(this)
                .setTitle("网费缴纳")
                .setView(editText)
                .setNegativeButton("取消", null)
                .setPositiveButton("缴纳", (dialog, which) -> {
                    String amountString = editText.getText().toString();
                    double amount = Double.parseDouble(amountString) * 100;
                    OUCRequestSender.getInstance().netGdc(new NetGdcOUCRequest(account, sno, (int) amount), new OUCCallback<>() {
                        @Override
                        public void onSuccess(NetGdcOUCResponse response) {
                            runOnUiThread(() -> {
                                if (response.getRetCode() == 0) {
                                    Toast.makeText(OucNetActivity.this, "缴费成功~", LENGTH_SHORT);
                                    refreshBalance();
                                } else {
                                    Toast.makeText(OucNetActivity.this, "缴费失败：" + response.getErrMessage(), LENGTH_SHORT);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            runOnUiThread(() -> Toast.makeText(OucNetActivity.this, "缴费失败！", LENGTH_SHORT));
                        }
                    });
                })
                .show();
    }

    public void onClickOfficial(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://222.195.158.36/Self/"));
        startActivity(intent);
    }
}