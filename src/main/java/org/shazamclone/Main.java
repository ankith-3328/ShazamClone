package org.shazamclone;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<String, List<Fingerprint>> fingerprintDatabase = new HashMap<>();

        fingerprintDatabase.put("big", FeatureExtractor.extractFingerprints(new File("src/main/resources/big.wav")));
        fingerprintDatabase.put("sample", FeatureExtractor.extractFingerprints(new File("src/main/resources/sample.wav")));
        fingerprintDatabase.put("tone", FeatureExtractor.extractFingerprints(new File("src/main/resources/pure-tone.wav")));
        fingerprintDatabase.put("voice", FeatureExtractor.extractFingerprints(new File("src/main/resources/voice-sample.wav")));

        List<Fingerprint> testClipPrints = FeatureExtractor.extractFingerprints(new File("src/main/resources/cut-voice.wav"));

        String bestMatch = null;
        int maxMatches = 0;

        for (Map.Entry<String, List<Fingerprint>> entry : fingerprintDatabase.entrySet()) {
            String songName = entry.getKey();
            List<Fingerprint> songPrints = entry.getValue();

            int matches = calculateSimilarity(testClipPrints, songPrints);
            System.out.println("Matches with " + songName + ": " + matches);

            if (matches > maxMatches) {
                maxMatches = matches;
                bestMatch = songName;
            }
        }

        System.out.println("Best match: " + bestMatch + " (" + maxMatches + " matching fingerprints)");

    }
    public static int calculateSimilarity(List<Fingerprint> shortClip, List<Fingerprint> fullSong) {
        Set<Fingerprint> songSet = new HashSet<>(fullSong);
        int count = 0;

        for (Fingerprint fp : shortClip) {
            if (songSet.contains(fp)) {
                count++;
            }
        }
        return count;
    }
}
