package vc.ddns.luna.sert.binaryfileuploadtest.app;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by Sert on 2014/08/01.
 */
public class Recording extends MediaRecorder {
    public Recording(String outputPath) {
        setAudioSource(AudioSource.MIC);
        setOutputFormat(OutputFormat.MPEG_4);
        setAudioEncoder(AudioEncoder.AAC);
        setAudioSamplingRate(22050);
        setOutputFile(outputPath);
    }

    public void recStart() {
        try {
            prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

    public void recEnd() {
        stop();
        reset();
        release();
    }
}
