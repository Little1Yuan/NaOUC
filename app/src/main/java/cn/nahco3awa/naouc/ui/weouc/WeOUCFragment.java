package cn.nahco3awa.naouc.ui.weouc;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.nahco3awa.naouc.databinding.FragmentWeoucBinding;

public class WeOUCFragment extends Fragment {
    private FragmentWeoucBinding binding;
    private Activity activity;
    private Context context;
    private View root;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWeoucBinding.inflate(inflater, container, true);
        activity = requireActivity();
        context = requireContext();
        root = binding.getRoot();

        preferences = context.getSharedPreferences("weouc", MODE_PRIVATE);


        return root;
    }


}
