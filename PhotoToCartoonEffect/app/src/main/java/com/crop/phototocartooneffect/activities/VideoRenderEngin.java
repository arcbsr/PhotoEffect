package com.crop.phototocartooneffect.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoRenderEngin {
    private MediaCodec mediaCodec;
    private MediaMuxer mediaMuxer;
    private int videoTrackIndex = -1;
    private boolean muxerStarted = false;
    private long presentationTimeUs = defaultFrameTime; // Presentation time in microseconds
    private Surface inputSurface;

    final public static long defaultFrameTime = 100000L;
    final public static long lowerFrameTime = 10000L;
    final public static long zeroFrameTime = 200L;

    private static VideoRenderEngin instance;

    public static int getFrameByTime(VideoFrames.FrameRate frameRate) {
        return (int) ((VideoRenderEngin.defaultFrameTime / 10000L) / frameRate.getValue()) * frameRate.getValue();
    }

    public interface ProgressCallback {
        void onProgressUpdate(int progress);

        void onCompleted(String outputFilePath);

        void onError(String msg, int code);
    }

    private VideoRenderEngin() {
        // Private constructor to prevent instantiation
    }

    public static synchronized VideoRenderEngin getInstance() {
        if (instance == null) {
            instance = new VideoRenderEngin();
        }
        return instance;
    }

    private ProgressCallback progressCallback;

    public void initialize(String outputFilePath, int screenWidth, int screenHeight, ProgressCallback progressCallback) throws IOException {
        init(screenWidth, screenHeight, outputFilePath);
        this.progressCallback = progressCallback;
    }

    String outputFilePath;

    private void init(int width, int height, String outputFilePath) throws IOException {
        this.outputFilePath = outputFilePath;
        mediaMuxer = new MediaMuxer(outputFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 1000000);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        inputSurface = mediaCodec.createInputSurface();
        mediaCodec.start();
    }
    public boolean createCanvasForRender(Bitmap bitmap, Matrix matrix, Paint paint) {
        return createCanvasForRender(bitmap, matrix, paint, lowerFrameTime);
    }
    public boolean createCanvasForRender(Bitmap bitmap, Matrix matrix, Paint paint, long frameTime) {
        Canvas canvas = inputSurface.lockCanvas(null);
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            // Draw the rotated and scaled bitmap
            canvas.drawBitmap(bitmap, matrix, paint);
            // Unlock and post the frame
            inputSurface.unlockCanvasAndPost(canvas);
            // Process the output frame
            processOutput(frameTime);
            return true;
        }
        return false;
    }

    public boolean createCanvasForRender(Bitmap bitmap, int left, int top, Paint paint) {
        Canvas canvas = inputSurface.lockCanvas(null);
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            // Draw the rotated and scaled bitmap
            canvas.drawBitmap(bitmap, left, top, paint);
            // Unlock and post the frame
            inputSurface.unlockCanvasAndPost(canvas);
            // Process the output frame
            processOutput();
            return true;
        }
        return false;
    }

    public void processOutput() {
        processOutput(defaultFrameTime);
    }

    private void processOutput(long frameTime) {
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

        while (true) {
            Log.e("Rafiur>>", "processOutput called");
            int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 20000);

            if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                Log.e("Rafiur>>", "INFO_OUTPUT_FORMAT_CHANGED");
                MediaFormat newFormat = mediaCodec.getOutputFormat();
                videoTrackIndex = mediaMuxer.addTrack(newFormat);

                if (!muxerStarted) {
                    mediaMuxer.start();
                    muxerStarted = true;
                }
            } else if (outputBufferIndex >= 0) {
                Log.e("Rafiur>>", "outputBufferIndex: " + outputBufferIndex);
                ByteBuffer encodedData = mediaCodec.getOutputBuffer(outputBufferIndex);

                if (encodedData != null && bufferInfo.size != 0) {
                    encodedData.position(bufferInfo.offset);
                    encodedData.limit(bufferInfo.offset + bufferInfo.size);
                    setPresentationTime(bufferInfo, frameTime);
                    mediaMuxer.writeSampleData(videoTrackIndex, encodedData, bufferInfo);
                }

                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);

                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.e("Rafiur>>", "Error occurred in processOutput");

                    break;
                }
            } else if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                Log.e("Rafiur>>", "INFO_TRY_AGAIN_LATER in processOutput");
                break;
            }
        }
    }

    private void setPresentationTime(MediaCodec.BufferInfo bufferInfo, long frameTime) {
//        presentationTimeUs += frameRate.getValue();
        bufferInfo.presentationTimeUs = presentationTimeUs;
        presentationTimeUs += frameTime; // 1 second per frame
    }

    public void finalizeVideo() {
        progressCallback.onCompleted(outputFilePath);
        mediaCodec.signalEndOfInputStream();
        processOutput();
        mediaCodec.stop();
        mediaCodec.release();
        mediaMuxer.stop();
        mediaMuxer.release();
        instance = null;
        progressCallback = null;
    }
}
