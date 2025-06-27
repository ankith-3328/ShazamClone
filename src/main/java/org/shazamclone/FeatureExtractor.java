package org.shazamclone;

import be.tarsos.dsp.*;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.util.fft.FFT;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeatureExtractor {
    public static List<Double> extractFrequencies(File audioFile) throws Exception {
        int bufferSize = 2048;
        int overlap = 1024;
        float sampleRate = 44100f;

        List<Double> frequencies = new ArrayList<>();
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, bufferSize, overlap);
        FFT fft = new FFT(bufferSize);
        float[] amplitudes = new float[bufferSize / 2];

        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] audioBuffer = audioEvent.getFloatBuffer().clone();
                fft.forwardTransform(audioBuffer);
                fft.modulus(audioBuffer, amplitudes);

                int maxIndex = 0;
                float maxAmplitude = -1;

                for (int i = 0; i < amplitudes.length; i++) {
                    if (amplitudes[i] > maxAmplitude) {
                        maxAmplitude = amplitudes[i];
                        maxIndex = i;
                    }
                }

                double frequency = fft.binToHz(maxIndex, sampleRate);
                frequencies.add(frequency);
                return true;
            }

            @Override
            public void processingFinished() {}
        });

        dispatcher.run();
        return frequencies;
    }
}
