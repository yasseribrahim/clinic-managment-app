package com.clinic.management.app.presenters;

import com.clinic.management.app.models.Speciality;

import java.util.List;

public interface SpecialitiesCallback extends BaseCallback {
    default void onGetSpecialitiesComplete(List<Speciality> specialities) {
    }

    default void onSaveSpecialityComplete() {
    }

    default void onDeleteSpecialityComplete() {
    }

    default void onEditSpecialityComplete() {
    }
}
