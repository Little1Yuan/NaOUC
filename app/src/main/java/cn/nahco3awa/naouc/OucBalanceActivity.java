package cn.nahco3awa.naouc;

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

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import cn.nahco3awa.naouc.network.ouc.request.AccountPayAliPayOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.GetCardAccInfoOUCRequest;
import cn.nahco3awa.naouc.network.ouc.response.AccountPayOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.GetCardAccInfoOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.OUCCallback;

public class OucBalanceActivity extends AppCompatActivity {
    private String account;
    private TextView balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouc_balance);

        Intent intent = getIntent();
        account = intent.getStringExtra("account");

        balanceTextView = findViewById(R.id.balanceBigTextView);

        refreshBalance();
    }

    private void refreshBalance() {
        OUCRequestSender.getInstance().getCardAccInfo(new GetCardAccInfoOUCRequest(account), new OUCCallback<>() {
            @Override
            public void onSuccess(GetCardAccInfoOUCResponse response) {
                runOnUiThread(() -> balanceTextView.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", response.getBalance() / 100.0)));
            }

            @Override
            public void onFailure(Throwable e) {
                runOnUiThread(() -> Toast.makeText(OucBalanceActivity.this, "获取余额失败！", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void onClickPay(View view) {
        EditText editText = new EditText(this);
        editText.setHint("请输入金额...");
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        new AlertDialog.Builder(this)
                .setTitle("充值")
                .setView(editText)
                .setNegativeButton("取消", null)
                .setPositiveButton("充值", (dialogInterface, i) -> {
                    String amountString = editText.getText().toString();
                    double amount = Double.parseDouble(amountString) * 100;
                    String parsedAmount = String.valueOf((int)amount);
                    OUCRequestSender.getInstance().accountPayAlipay(new AccountPayAliPayOUCRequest(account, parsedAmount), new OUCCallback<>() {
                        @Override
                        public void onSuccess(AccountPayOUCResponse response) {
                            runOnUiThread(() -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.getUrl()));
                                startActivity(intent);
                            });
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            runOnUiThread(() -> Toast.makeText(OucBalanceActivity.this, "下单失败！", Toast.LENGTH_SHORT).show());
                        }
                    });
                })
                .show();
    }
}