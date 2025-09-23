package cn.nahco3awa.naouc.ui.ouc;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;
import java.util.Random;

import cn.nahco3awa.naouc.OucLoginMainActivity;
import cn.nahco3awa.naouc.R;
import cn.nahco3awa.naouc.databinding.FragmentOucBinding;
import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import cn.nahco3awa.naouc.network.ouc.request.GetBarCodePayOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.GetInfoByTokenOUCRequest;
import cn.nahco3awa.naouc.network.ouc.response.GetBarCodePayOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.GetInfoByTokenOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.OUCCallback;

public class OUCFragment extends Fragment {
    private SharedPreferences preferences;
    private FragmentOucBinding binding;
    private String sno = null;
    private String sourceType = null;
    private Button loginButton;
    private TextView welcomeTextView;
    private View root;
    private ActivityResultLauncher<Intent> loginLauncher;

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

        loginButton.setOnClickListener(this::onClickLogin);

        refreshLogonState();

        return root;
    }

    private GetInfoByTokenOUCResponse infoResponse = null;
    public void refreshLogonState() {
        if (isLogon()) {
            loginButton.setText("登出");
            sno = preferences.getString("sno",  "");
            sourceType = preferences.getString("source_ticket", "");
            OUCRequestSender.getInstance().getInfoByToken(new GetInfoByTokenOUCRequest(OUCRequestSender.getInstance().getImeiTicket(), sourceType, sourceType), new OUCCallback<GetInfoByTokenOUCResponse>() {
                @Override
                public void onSuccess(GetInfoByTokenOUCResponse response) {
                    infoResponse = response;
                    requireActivity().runOnUiThread(() -> {
                        welcomeTextView.setText("欢迎回来，" + response.getName());
                        String account = infoResponse.getAccount();
                        String cardId = infoResponse.getCardId();

                        try {
                            GetBarCodePayOUCRequest getBarCodePayOUCRequest = new GetBarCodePayOUCRequest(account, cardId, OUCRequestSender.getInstance().getImeiTicket(), OUCRequestSender.getInstance().getSourceTypeTicket());
                            OUCRequestSender.getInstance().getBarCodePay(getBarCodePayOUCRequest, new OUCCallback<>() {
                                @Override
                                public void onSuccess(GetBarCodePayOUCResponse response) {
                                    try {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("到期时间：")
                                                .append(response.getExpires())
                                                .append("\n账户编号：")
                                                .append(response.getAccount())
                                                .append("\n条码信息：");
                                        for (String s : response.getBarcode()) {
                                            stringBuilder.append('\n')
                                                    .append(s);
                                        }
                                        requireActivity().runOnUiThread(() -> new AlertDialog.Builder(getActivity())
                                                .setTitle("获取支付码成功！")
                                                .setMessage(stringBuilder.toString())
                                                .setNegativeButton("蒿", null)
                                                .show());
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
                    });
                }

                @Override
                public void onFailure(Throwable e) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "获取个人信息失败！", Toast.LENGTH_SHORT).show();
                        welcomeTextView.setText("可能需要重新登录？");
                    });
                }
            });
        } else {
            loginButton.setText("登录");
            welcomeTextView.setText("尚未登录");
        }
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
}