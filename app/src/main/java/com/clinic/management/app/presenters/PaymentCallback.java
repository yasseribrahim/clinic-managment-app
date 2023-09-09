package com.clinic.management.app.presenters;

import com.clinic.management.app.models.Payment;

import java.util.List;

public interface PaymentCallback extends BaseCallback {
    void onSavePaymentComplete();

    void onSavePaymentFail(int messageId);

    void onGetPaymentsComplete(List<Payment> payments);

    void onChargeWalletComplete();
}
