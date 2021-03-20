package com.cfox.camera.capture.impl;


import android.graphics.ImageFormat;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.IConfigWrapper;
import com.cfox.camera.camera.info.CameraInfo;
import com.cfox.camera.camera.info.CameraInfoHelper;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.capture.ImageCapture;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.ImageMode;
import com.cfox.camera.capture.business.IBusiness;
import com.cfox.camera.capture.business.impl.PhotoBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;
// 整理发送
public class ImageCaptureImpl implements ImageCapture {

    private final ImageMode mImageMode;
    private final CameraInfoManager mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
    private final IBusiness mBusiness;
    public ImageCaptureImpl(ImageMode imageMode, IConfigWrapper configWrapper) {
        mImageMode = imageMode;
        mBusiness = new PhotoBusiness(configWrapper);
    }

    @Override
    public void onStartPreview(EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        // 切换Camera 信息管理中的 Camera 信息， 如前置camera  或 后置Camera
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        CameraInfo cameraInfo = CameraInfoHelper.getInstance().getCameraInfo(cameraId);
        mCameraInfoManager.initCameraInfo(cameraInfo);

        // 设置预览大小
        Size previewSizeForReq = (Size) request.getObj(Es.Key.PREVIEW_SIZE);
        Size previewSize = mBusiness.getPreviewSize(previewSizeForReq, mCameraInfoManager.getPreviewSize(surfaceHelper.getSurfaceClass()));
        surfaceHelper.setAspectRatio(previewSize);

        // 设置图片大小
        Size pictureSizeForReq = (Size) request.getObj(Es.Key.PIC_SIZE);
        int imageFormat = request.getInt(Es.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        Size pictureSize = mBusiness.getPictureSize(pictureSizeForReq, mCameraInfoManager.getPictureSize(imageFormat));
        request.put(Es.Key.PIC_SIZE, pictureSize);


        mImageMode.requestPreview(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                EsLog.d("onNext: .requestPreview....");


            }
        });
    }

    @Override
    public void onCameraConfig(EsRequest request) {
        mImageMode.requestCameraConfig(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                EsLog.d("onNext: .requestCameraConfig....");

            }
        });
    }

    @Override
    public void onStop(EsRequest request) {
        mImageMode.requestStop(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                EsLog.d("onNext: .requestStop....");

            }
        });
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return mCameraInfoManager.getEvRange();
    }

    @Override
    public void onCapture(EsRequest request) {
        int sensorOrientation = mCameraInfoManager.getSensorOrientation();
        int picOrientation = mBusiness.getPictureOrientation(sensorOrientation);
        request.put(Es.Key.PIC_ORIENTATION, picOrientation);
        mImageMode.requestCapture(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                EsLog.d("onNext: .requestCapture....");

            }
        });
    }
}
