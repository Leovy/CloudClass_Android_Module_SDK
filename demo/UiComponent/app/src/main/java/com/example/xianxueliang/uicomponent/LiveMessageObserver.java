package com.example.xianxueliang.uicomponent;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Looper;
import android.support.annotation.AnyThread;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

interface LiveMessageObserver<T extends LiveMessageObserver.TargetData> extends Observer<T> {

    @CallSuper
    @Override
    default void onChanged(@Nullable T t) {
        if (t != null) {
            String current = getTarget();
            Objects.requireNonNull(current);
            if (t.getTarget().equals(current)) {
                safeOnChanged(t);
            }
        }
    }

    void safeOnChanged(@NonNull T data);

    @NonNull
    String getTarget();

    class TargetData {
        @NonNull
        private final String mTarget;

        TargetData(@NonNull String target) {
            this.mTarget = Objects.requireNonNull(target);
        }

        @NonNull
        String getTarget() {
            return mTarget;
        }
    }

    class LiveMessage<T> {

        @NonNull
        private final MutableLiveData<T> mTarget;
        @NonNull
        private T mData;

        static <T extends TargetData> LiveMessage obtain(@NonNull MutableLiveData<T> target,
                                                         @NonNull T data) {
            return new LiveMessage<>(target, data);
        }

        LiveMessage(@NonNull MutableLiveData<T> liveData, @NonNull T data) {
            this.mTarget = Objects.requireNonNull(liveData);
            this.mData = Objects.requireNonNull(data);
        }

        @AnyThread
        @SuppressLint("WrongThread")
        void sendToTarget() {
            if (isMainThread()) {
                mTarget.setValue(mData);
            } else {
                mTarget.postValue(mData);
            }
        }

        private boolean isMainThread() {
            return Looper.getMainLooper().getThread() == Thread.currentThread();
        }
    }
}