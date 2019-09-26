package com.cfox.camera.model;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface CameraModule {

    Observable<FxResult> openCamera(FxRequest request);

    Observable<FxResult> changeModule(CameraModuleImpl.ModuleFlag moduleFlag);
}
