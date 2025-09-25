package cn.nahco3awa.naouc.ui.ouc.activity;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import cn.nahco3awa.naouc.R;
import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import cn.nahco3awa.naouc.network.ouc.request.GetRsaOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.GetValidateCodeOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.LoginOUCRequest;
import cn.nahco3awa.naouc.network.ouc.response.GetRsaKeyOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.GetValidateCodeOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.LoginOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.OUCCallback;

public class OucLoginMainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private EditText snoEdit;
    private EditText passwordEdit;
    private EditText ysmEdit;
    private CheckBox rememberCheck;
    private ImageView ysmImageView;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouc_login_main);

        snoEdit = findViewById(R.id.editTextOucSno);
        passwordEdit = findViewById(R.id.editTextOucPassword);
        ysmEdit = findViewById(R.id.editTextOucYsm);
        rememberCheck = findViewById(R.id.checkBoxOucRemember);
        ysmImageView = findViewById(R.id.oucYsmImageView);
        loginButton = findViewById(R.id.buttonOucLogin);

        preferences = getSharedPreferences("ouc", MODE_PRIVATE);
        if (preferences.getBoolean("mem", false)) {
            snoEdit.setText(preferences.getString("mem_sno", ""));
            passwordEdit.setText(preferences.getString("mem_pwd", ""));
            rememberCheck.setChecked(true);
        }

        getLoginNeeds();
    }

    private void getLoginNeeds() {
        loginButton.setEnabled(false);
        refreshYsmCode();
    }

    private void refreshYsmCode() {
        OUCRequestSender.getInstance().getValidateCode(new GetValidateCodeOUCRequest(System.currentTimeMillis() + 1000 * 60 * 5), new OUCCallback<>() {
            @Override
            public void onSuccess(GetValidateCodeOUCResponse response) {
                runOnUiThread(() -> {
                    ysmImageView.setImageBitmap(response.getImage());
                    loginButton.setEnabled(true);
                });
            }

            @Override
            public void onFailure(Throwable e) {
                runOnUiThread(() -> {
                    Toast.makeText(OucLoginMainActivity.this, "网络连接有点问题，请重试", LENGTH_SHORT).show();
                    new AlertDialog.Builder(OucLoginMainActivity.this)
                            .setTitle("错误")
                            .setMessage("网络错误：\n" + e.getMessage())
                            .setNegativeButton("好", null)
                            .show();
                });

            }
        });
    }

    private void onLoginFailed(Throwable e) {
        new AlertDialog.Builder(this)
                .setTitle("登录失败……")
                .setMessage("中国海洋大学门户登录失败，请稍后再试。\n" + e.getMessage())
                .setNegativeButton("好", null)
                .show();
        refreshYsmCode();
    }

    public void onClickLogin(View view) {
        loginButton.setEnabled(false);
        OUCRequestSender.getInstance().getRsaKey(new GetRsaOUCRequest(), new OUCCallback<>() {
            @Override
            public void onSuccess(GetRsaKeyOUCResponse response) {
                try {
                    String pwd = passwordEdit.getText().toString();
                    BigInteger bigExpo = new BigInteger(response.getE(), 16);
                    BigInteger bigModulus = new BigInteger(response.getMod(), 16);
                    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigModulus, bigExpo);
                    KeyFactory factory = KeyFactory.getInstance("RSA");
                    PublicKey publicKey = factory.generatePublic(keySpec);
                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                    byte[] light = pwd.getBytes(StandardCharsets.UTF_8);
                    byte[] encrypted = cipher.doFinal(light);
                    StringBuilder builder = new StringBuilder(encrypted.length * 2);
                    for (byte b : encrypted) {
                        String hexB = Integer.toHexString(b & 0xFF);
                        if (hexB.length() == 1) {
                            hexB = "0" + hexB;
                        }
                        builder.append(hexB);
                    }
                    String encryptedHex = builder.toString();
                    OUCRequestSender.getInstance().login(new LoginOUCRequest(snoEdit.getText().toString(), encryptedHex, rememberCheck.isChecked(), "8898", ysmEdit.getText().toString(), response.getMsg()), new OUCCallback<>() {
                        @Override
                        public void onSuccess(LoginOUCResponse loginResponse) {
                            runOnUiThread(() -> {
                                if (rememberCheck.isChecked()) {
                                    preferences.edit()
                                            .putString("mem_sno", loginResponse.getSno())
                                            .putString("mem_pwd", pwd)
                                            .apply();
                                }
                                preferences.edit()
                                        .putString("source_ticket", loginResponse.getRescouseType())
                                        .putBoolean("mem", rememberCheck.isChecked())
                                        .putBoolean("logon", true)
                                        .putString("sno", loginResponse.getSno())
                                        .apply();
                                String name = preferences.getString("nickname", loginResponse.getName());
                                Toast.makeText(OucLoginMainActivity.this, name + "，欢迎回来~", LENGTH_SHORT).show();
                                setResult(Activity.RESULT_OK);
                                finish();
                            });
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            runOnUiThread(() -> OucLoginMainActivity.this.onLoginFailed(e));
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> OucLoginMainActivity.this.onLoginFailed(e));
                    Log.e("OUCLogin", "Login failed", e);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                runOnUiThread(() -> OucLoginMainActivity.this.onLoginFailed(e));
            }
        });
    }
}