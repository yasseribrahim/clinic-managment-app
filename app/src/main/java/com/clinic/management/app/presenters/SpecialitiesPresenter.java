package com.clinic.management.app.presenters;

import com.clinic.management.app.models.Constants;
import com.clinic.management.app.models.Speciality;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SpecialitiesPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private SpecialitiesCallback callback;

    public SpecialitiesPresenter(SpecialitiesCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_SPECIALITIES).getRef();
        this.callback = callback;
    }

    public void save(Speciality speciality) {
        if (callback != null) {
            callback.onShowLoading();
        }
        if (speciality.getId() == null) {
            speciality.setId(Calendar.getInstance().getTimeInMillis() + "");
        }

        reference.child(speciality.getId()).setValue(speciality).addOnCompleteListener(runnable -> {
            if (callback != null) {
                callback.onSaveSpecialityComplete();
                callback.onHideLoading();
            }
        });
    }

    public void remove(Speciality speciality) {
        if (callback != null) {
            callback.onShowLoading();
        }
        if (speciality.getId() == null) {
            speciality.setId(Calendar.getInstance().getTimeInMillis() + "");
        }

        reference.child(speciality.getId()).removeValue().addOnCompleteListener(runnable -> {
            if (callback != null) {
                callback.onDeleteSpecialityComplete();
                callback.onHideLoading();
            }
        });
    }

    public void getSpecialities() {
        if (callback != null) {
            callback.onShowLoading();
        }
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Speciality> specialities = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Speciality speciality = child.getValue(Speciality.class);
                    specialities.add(speciality);
                }

                if (callback != null) {
                    callback.onGetSpecialitiesComplete(specialities);
                    callback.onHideLoading();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (callback != null) {
                    callback.onFailure("Unable to get message: " + databaseError.getMessage(), null);
                    callback.onHideLoading();
                }
            }
        };
        reference.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void onDestroy() {
        if (reference != null && listener != null) {
            reference.removeEventListener(listener);
        }
    }
}
