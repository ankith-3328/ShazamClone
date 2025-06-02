package org.shazamclone;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        File audioFile = new File("src/main/resources/sample.wav");

        int bufferSize = 2048;
        int overlap = 1024;
        float sampleRate = 44100f;

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, bufferSize, overlap);

        FFT fft = new FFT(bufferSize);
        float[] amplitudes = new float[bufferSize / 2];
        int i = 0;

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
                System.out.println("Peak frequency: " + frequency + " Hz");
                return true;
            }

            @Override
            public void processingFinished() {
                System.out.println("Finished processing.");
            }
        });

        dispatcher.run();
    }
}
