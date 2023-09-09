package com.clinic.management.app.presenters;

import com.clinic.management.app.R;
import com.clinic.management.app.models.Constants;
import com.clinic.management.app.models.Message;
import com.clinic.management.app.models.Payment;
import com.clinic.management.app.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentPresenter implements BasePresenter, UsersCallback, FirebaseCallback {
    private final UsersPresenter usersPresenter;
    private final FirebasePresenter firebasePresenter;
    private DatabaseReference reference;
    private ValueEventListener listener;
    private PaymentCallback callback;

    public PaymentPresenter(PaymentCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_PAYMENTS).getRef();
        usersPresenter = new UsersPresenter(this);
        firebasePresenter = new FirebasePresenter(this);
        this.callback = callback;
    }

    public void save(double amount, User receiver, User sender) {
        if (sender.getWallet() > amount) {
            if (callback != null) {
                callback.onShowLoading();
            }

            Payment paymentReceived = new Payment();
            paymentReceived.setId(Calendar.getInstance().getTimeInMillis() + "");
            paymentReceived.setAmount(amount);
            paymentReceived.setDate(Calendar.getInstance().getTime());
            paymentReceived.setNotes("Receive Amount " + amount);
            paymentReceived.setReceiverId(receiver.getId());
            paymentReceived.setReceiverName(receiver.getUsername());
            paymentReceived.setSenderId(sender.getId());
            paymentReceived.setSenderName(sender.getUsername());

            Payment paymentSender = new Payment();
            paymentSender.setId(Calendar.getInstance().getTimeInMillis() + "");
            paymentSender.setAmount(amount);
            paymentSender.setDate(Calendar.getInstance().getTime());
            paymentSender.setNotes("Send Amount " + amount);
            paymentSender.setReceiverId(receiver.getId());
            paymentSender.setReceiverName(receiver.getUsername());
            paymentSender.setSenderId(sender.getId());
            paymentSender.setSenderName(sender.getUsername());

            sender.setWallet(sender.getWallet() - paymentReceived.getAmount());
            receiver.setWallet(receiver.getWallet() + paymentReceived.getAmount());
            reference.child(paymentReceived.getSenderId()).child(paymentSender.getId()).setValue(paymentSender);
            reference.child(paymentReceived.getReceiverId()).child(paymentReceived.getId()).setValue(paymentReceived);
            usersPresenter.save(sender, receiver);

            Message message = new Message();
            message.setMessage(paymentSender.getNotes());
            message.setSenderId(sender.getId());
            message.setSenderName("System Admin");
            message.setReceiveName(receiver.getFullName());
            message.setTimestamp(Calendar.getInstance().getTimeInMillis());
            firebasePresenter.send(message, sender.getTokens());

            message = new Message();
            message.setMessage(paymentReceived.getNotes());
            message.setSenderId(receiver.getId());
            message.setSenderName("System Admin");
            message.setReceiveName(receiver.getFullName());
            message.setTimestamp(Calendar.getInstance().getTimeInMillis());
            firebasePresenter.send(message, sender.getTokens());

            if (callback != null) {
                callback.onSavePaymentComplete();
                callback.onHideLoading();
            }
        } else {
            if (callback != null) {
                callback.onSavePaymentFail(R.string.str_delete_message);
            }
        }
    }

    public void chargeWallet(User user, double amount) {
        if (callback != null) {
            callback.onShowLoading();
        }

        Payment payment = new Payment();
        payment.setId(Calendar.getInstance().getTimeInMillis() + "");
        payment.setAmount(amount);
        payment.setDate(Calendar.getInstance().getTime());
        payment.setNotes("Charge Wallet By Amount " + amount);
        payment.setReceiverId(user.getId());
        payment.setReceiverName(user.getFullName());
        payment.setSenderId(user.getId());
        payment.setSenderName(user.getFullName());

        user.setWallet(user.getWallet() + payment.getAmount());
        reference.child(payment.getSenderId()).child(user.getId()).setValue(payment);
        usersPresenter.save(user);

        Message message = new Message();
        message.setMessage(payment.getNotes());
        message.setSenderId(payment.getId());
        message.setSenderName(user.getUsername());
        message.setReceiveName(user.getFullName());
        message.setTimestamp(Calendar.getInstance().getTimeInMillis());
        firebasePresenter.send(message, user.getTokens());

        if (callback != null) {
            callback.onChargeWalletComplete();
            callback.onHideLoading();
        }
    }

    public void getPayments(String userId) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Payment> payments = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Payment payment = child.getValue(Payment.class);
                    payments.add(payment);
                }

                if (callback != null) {
                    callback.onGetPaymentsComplete(payments);
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
        reference.child(userId).addListenerForSingleValueEvent(listener);
    }

    @Override
    public void onDestroy() {
        if (reference != null && listener != null) {
            reference.removeEventListener(listener);
        }
    }
}
