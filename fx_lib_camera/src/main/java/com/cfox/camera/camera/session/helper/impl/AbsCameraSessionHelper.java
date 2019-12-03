package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraAccessException;
import android.util.Range;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.helper.ICameraSessionHelper;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public abstract class AbsCameraSessionHelper implements ICameraSessionHelper {
    @Override
    public Observable<FxResult> onOpenCamera(FxRequest request) {
        return getCameraSession(request).onOpenCamera(request);
    }

    @Override
    public Observable<FxResult> createPreviewSession(FxRequest request) throws CameraAccessException {
        applyPreviewRepeatingBuilder(request);
        return getCameraSession(request).onCreatePreviewSession(request);
    }

    @Override
    public Observable<FxResult> sendPreviewRepeatingRequest(FxRequest request) {
        return getCameraSession(request).onRepeatingRequest(request);
    }

    public void applyPreviewRepeatingBuilder(FxRequest request) throws CameraAccessException {}

    @Override
    public Range<Integer> getEvRange() {
        return null;
    }

    public abstract ICameraSession getCameraSession(FxRequest request);
}
