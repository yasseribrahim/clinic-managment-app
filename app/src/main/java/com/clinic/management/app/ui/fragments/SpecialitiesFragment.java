package com.clinic.management.app.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.clinic.management.app.R;
import com.clinic.management.app.databinding.FragmentSpecialitiesBinding;
import com.clinic.management.app.models.Speciality;
import com.clinic.management.app.presenters.SpecialitiesCallback;
import com.clinic.management.app.presenters.SpecialitiesPresenter;
import com.clinic.management.app.ui.adapters.SpecialityAdapter;
import com.clinic.management.app.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SpecialitiesFragment extends Fragment implements SpecialitiesCallback, SpecialityAdapter.OnSpecialityActionListener {
    private FragmentSpecialitiesBinding binding;
    private SpecialitiesPresenter presenter;
    private SpecialityAdapter adapter;
    private List<Speciality> specialities;
    private Speciality selectedSpeciality;

    public static SpecialitiesFragment newInstance() {
        Bundle args = new Bundle();
        SpecialitiesFragment fragment = new SpecialitiesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSpecialitiesBinding.inflate(inflater);

        presenter = new SpecialitiesPresenter(this);
        bind(new Speciality(null));

        binding.refreshLayout.setColorSchemeResources(R.color.refreshColor1, R.color.refreshColor2, R.color.refreshColor3, R.color.refreshColor4);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        binding.btnNewSpeciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.textValue.getText().toString();
                Speciality speciality = new Speciality(null, name);
                int index = specialities.indexOf(speciality);
                if (name.isEmpty() || index != -1) {
                    ToastUtils.longToast(R.string.str_invalid_entered_value);
                    return;
                }

                selectedSpeciality.setName(name);
                presenter.save(selectedSpeciality);
            }
        });

        specialities = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SpecialityAdapter(specialities, this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void load() {
        presenter.getSpecialities();
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onGetSpecialitiesComplete(List<Speciality> specialities) {
        this.specialities.clear();
        this.specialities.addAll(specialities);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onShowLoading() {
        binding.refreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(Speciality speciality) {
        bind(speciality);
        showConfirmDeleteDialog(speciality);
    }

    @Override
    public void onEditClick(Speciality speciality) {
        bind(speciality);
    }

    @Override
    public void onSaveSpecialityComplete() {
        bind(new Speciality(null));
        Toast.makeText(getContext(), R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        load();
    }

    @Override
    public void onEditSpecialityComplete() {
        bind(new Speciality(null));
        Toast.makeText(getContext(), R.string.str_message_updated_successfully, Toast.LENGTH_LONG).show();
        load();
    }

    @Override
    public void onDeleteSpecialityComplete() {
        bind(new Speciality(null));
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
        load();
    }

    private void bind(Speciality speciality) {
        selectedSpeciality = speciality;
        binding.textValue.setText(speciality.getName());
    }

    private void showConfirmDeleteDialog(Speciality speciality) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(R.string.str_delete_title);
        alert.setMessage(R.string.str_delete_message);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.remove(speciality);
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}