package org.shazamclone;

import be.tarsos.dsp.*;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.util.fft.FFT;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeatureExtractor {

    public static List<Fingerprint> extractFingerprints(File audioFile) throws Exception {
        int bufferSize = 2048;
        int overlap = 1024;
        float sampleRate = 44100f;

        List<Fingerprint> fingerprints = new ArrayList<>();
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, bufferSize, overlap);
        FFT fft = new FFT(bufferSize);
        float[] amplitudes = new float[bufferSize / 2];

        final int[] frameCount = {0};

        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] audioBuffer = audioEvent.getFloatBuffer().clone();
                fft.forwardTransform(audioBuffer);
                fft.modulus(audioBuffer, amplitudes);

                int maxIndex = 0;
                float maxAmp = -1;
                for (int i = 0; i < amplitudes.length; i++) {
                    if (amplitudes[i] > maxAmp) {
                        maxAmp = amplitudes[i];
                        maxIndex = i;
                    }
                }

                int quantizedBin = maxIndex / 5;  // group nearby bins

                fingerprints.add(new Fingerprint(frameCount[0], quantizedBin));
                frameCount[0]++;
                return true;
            }

            @Override
            public void processingFinished() {
            }
        });

        dispatcher.run();
        return fingerprints;
    }
}
