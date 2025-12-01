package cn.nahco3awa.naouc.ui.ouc;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import cn.nahco3awa.naouc.network.ouc.request.NetCheckOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.OUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.TsmOUCRequest;
import cn.nahco3awa.naouc.network.ouc.response.TsmOUCResponse;
import cn.nahco3awa.naouc.ui.ouc.activity.OucBalanceActivity;
import cn.nahco3awa.naouc.ui.ouc.activity.OucLoginMainActivity;
import cn.nahco3awa.naouc.R;
import cn.nahco3awa.naouc.databinding.FragmentOucBinding;
import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import cn.nahco3awa.naouc.network.ouc.request.GetBarCodePayOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.GetCardAccInfoOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.GetInfoByTokenOUCRequest;
import cn.nahco3awa.naouc.network.ouc.response.GetBarCodePayOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.GetCardAccInfoOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.GetInfoByTokenOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.OUCCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class OUCFragment extends Fragment {
    private SharedPreferences preferences;
    private FragmentOucBinding binding;
    private String sno = null;
    private String sourceType = null;
    private Button loginButton;
    private TextView welcomeTextView;
    private View root;
    private ActivityResultLauncher<Intent> loginLauncher;
    private ImageView barcodeImageView;
    private ImageView qrCodeImageView;
    private TextView balanceTextView;
    private TextView netBalanceTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOucBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        Context context = root.getContext();

        preferences = context.getSharedPreferences("ouc", MODE_PRIVATE);
        if (!preferences.contains("imei")) {
            StringBuilder stringBuilder = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 32; i++) {
                stringBuilder.append(random.nextInt(10));
            }
            preferences.edit()
                    .putString("imei", stringBuilder.toString())
                    .apply();
        }
        OUCRequestSender.init(preferences.getString("imei", "1145141919810deadbeef52013149922"));

        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                refreshLogonState();
            }
        });

        loginButton = root.findViewById(R.id.oucMainLoginButton);
        welcomeTextView = root.findViewById(R.id.oucMainWelcomeTextView);
        barcodeImageView = root.findViewById(R.id.oucBarcodeImageView);
        qrCodeImageView = root.findViewById(R.id.qrCodeImageView);
        balanceTextView = root.findViewById(R.id.balanceTextView);
        netBalanceTextView = root.findViewById(R.id.netBalanceTextView);

        loginButton.setOnClickListener(this::onClickLogin);
        welcomeTextView.setOnClickListener(this::onClickWelcomeText);
        barcodeImageView.setOnClickListener(this::onClickRefreshPayCode);
        qrCodeImageView.setOnClickListener(this::onClickRefreshPayCode);
        root.findViewById(R.id.cashButton).setOnClickListener(this::onClickBalance);

        refreshLogonState();

        return root;
    }

    private GetInfoByTokenOUCResponse infoResponse = null;
    public void refreshLogonState() {
        if (isLogon()) {
            loginButton.setText("登出");
            sno = preferences.getString("sno",  "");
            sourceType = preferences.getString("source_ticket", "");
            OUCRequestSender.getInstance().setSourceTypeTicket(sourceType);
            OUCRequestSender.getInstance().getInfoByToken(new GetInfoByTokenOUCRequest(OUCRequestSender.getInstance().getImeiTicket(), sourceType, sourceType), new OUCCallback<>() {
                @Override
                public void onSuccess(GetInfoByTokenOUCResponse response) {
                    infoResponse = response;
                    requireActivity().runOnUiThread(() -> {
                        refreshWelcomeText();
                        refreshPayCode();
                        refreshBalance();
                        refreshNetBalance();
                    });
                }

                @Override
                public void onFailure(Throwable e) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "获取个人信息失败！", Toast.LENGTH_SHORT).show();
                        welcomeTextView.setText("可能需要重新登录？");
                        if (e.getMessage() != null && e.getMessage().equals("令牌失效")) {
                            preferences.edit()
                                    .putBoolean("logon", false)
                                    .apply();
                            refreshLogonState();
                        }
                    });
                }
            });
        } else {
            loginButton.setText("登录");
            welcomeTextView.setText("尚未登录");
        }
    }

    private void onClickRefreshPayCode(View view) {
        if (isLogon() && infoResponse != null) {
            refreshPayCode();
            refreshBalance();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLogonState();
    }

    private void refreshNetBalance() {
        netBalanceTextView.setText("...");
        if (OUCRequestSender.getInstance().getjSessionId().isEmpty()) {
            OUCRequestSender.getInstance().sendRequest(new NetCheckOUCRequest(), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() -> {
                        netBalanceTextView.setText("获取失败");
                        new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                                .setTitle("网费详情获取失败")
                                .setMessage(e.getMessage())
                                .show();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    refreshNetByTsm();
                }
            });
        } else {
            refreshNetByTsm();
        }
    }

    private void refreshNetByTsm() {
        String account = infoResponse.getAccount();
        OUCRequestSender.getInstance().tsm(new TsmOUCRequest(account, infoResponse.getSno()), new OUCCallback<>() {
            @Override
            public void onSuccess(TsmOUCResponse response) {
                requireActivity().runOnUiThread(() -> netBalanceTextView.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", response.getBalance() / 100.0)));
            }

            @Override
            public void onFailure(Throwable e) {
                requireActivity().runOnUiThread(() -> {
                    netBalanceTextView.setText("获取失败");
                    new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                            .setTitle("网费详情获取失败")
                            .setMessage(e.getMessage())
                            .show();
                });
            }
        });
    }

    private void refreshBalance() {
        String account = infoResponse.getAccount();
        balanceTextView.setText("...");
        OUCRequestSender.getInstance().getCardAccInfo(new GetCardAccInfoOUCRequest(account), new OUCCallback<>() {
            @Override
            public void onSuccess(GetCardAccInfoOUCResponse response) {
                requireActivity().runOnUiThread(() -> balanceTextView.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", response.getBalance() / 100.0)));
            }

            @Override
            public void onFailure(Throwable e) {
                requireActivity().runOnUiThread(() -> balanceTextView.setText("获取失败"));
            }
        });
    }

    private void refreshPayCode() {
        String account = infoResponse.getAccount();

        try {
            GetBarCodePayOUCRequest getBarCodePayOUCRequest = new GetBarCodePayOUCRequest(account, "1", OUCRequestSender.getInstance().getImeiTicket(), OUCRequestSender.getInstance().getSourceTypeTicket());
            OUCRequestSender.getInstance().getBarCodePay(getBarCodePayOUCRequest, new OUCCallback<>() {
                @Override
                public void onSuccess(GetBarCodePayOUCResponse response) {
                    try {
                        requireActivity().runOnUiThread(() -> {
                            try {
                                BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                BitMatrix bitMatrix = barcodeEncoder.encode(response.getBarcode()[0], barcodeFormat, barcodeImageView.getWidth(), barcodeImageView.getHeight());
                                Bitmap barcodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
                                barcodeImageView.setImageBitmap(barcodeBitmap);

                                bitMatrix = barcodeEncoder.encode(response.getBarcode()[0], BarcodeFormat.QR_CODE, qrCodeImageView.getWidth(), qrCodeImageView.getHeight());
                                Bitmap qrBitmap = barcodeEncoder.createBitmap(bitMatrix);
                                qrCodeImageView.setImageBitmap(qrBitmap);
                            } catch (WriterException e) {
                                requireActivity().runOnUiThread(() -> new AlertDialog.Builder(requireActivity())
                                        .setTitle("显示支付码错误！")
                                        .setMessage(e.getMessage())
                                        .setNegativeButton("蒿", null)
                                        .show());
                            }

                        });
                    } catch (Exception e) {
                        requireActivity().runOnUiThread(() -> new AlertDialog.Builder(requireActivity())
                                .setTitle("显示支付码错误！")
                                .setMessage(e.getMessage())
                                .setNegativeButton("蒿", null)
                                .show());
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    requireActivity().runOnUiThread(() -> new AlertDialog.Builder(requireActivity())
                            .setTitle("获取支付码错误！")
                            .setMessage(e.getMessage())
                            .setNegativeButton("蒿", null)
                            .show());
                }
            });
        } catch (Exception e) {
            requireActivity().runOnUiThread(() -> new AlertDialog.Builder(requireActivity())
                    .setTitle("获取支付码错误！")
                    .setMessage(e.getMessage())
                    .setNegativeButton("蒿", null)
                    .show());
        }
    }

    private void onClickBalance(View view) {
        if (isLogon() && infoResponse != null) {
            Intent intent = new Intent(getActivity(), OucBalanceActivity.class);
            intent.putExtra("account", infoResponse.getAccount());
            startActivity(intent);
        }
    }

    private void refreshWelcomeText() {
        if (isLogon()) {
            welcomeTextView.setText("欢迎回来，" + getName() + "~");
        } else {
            welcomeTextView.setText("尚未登录");
        }
    }

    private String getName() {
        return preferences.getString("nickname", infoResponse == null ? "" : infoResponse.getName());
    }

    public boolean isLogon() {
        return preferences.getBoolean("logon",false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onClickLogin(View view) {
        if (isLogon()) {
            preferences.edit()
                    .putBoolean("logon", false)
                    .apply();
            refreshLogonState();
        } else {
            loginLauncher.launch(new Intent(getActivity(), OucLoginMainActivity.class));
        }
    }

    private void onClickWelcomeText(View view) {
        EditText editText = new EditText(requireContext());
        editText.setHint("输出昵称...");
        String nick = preferences.getString("nickname", infoResponse == null ? "" : infoResponse.getName());
        editText.setText(nick);
        new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setTitle("设置昵称：")
                .setView(editText)
                .setNegativeButton("取消", null)
                .setPositiveButton("应用", (dialogInterface, i) -> {
                    preferences.edit()
                            .putString("nickname", editText.getText().toString())
                            .apply();
                    refreshWelcomeText();
                })
                .show();
    }
}