package ca.mcgill.ecse321.townlibrary;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentPersonalBinding;

public class PersonalFragment extends Fragment {

    private FragmentPersonalBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textViewUserId.setText(LoginStatus.INSTANCE.getUserId().toString());
        binding.textViewName.setText(LoginStatus.INSTANCE.getUsername());
        binding.textViewAddress.setText(LoginStatus.INSTANCE.getAddress());
        binding.textViewEmail.setText(LoginStatus.INSTANCE.getEmailAddress());
        binding.textViewTownStatus.setText(LoginStatus.INSTANCE.getInTown().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
