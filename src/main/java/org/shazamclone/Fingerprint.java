package org.shazamclone;

import java.util.Objects;

public class Fingerprint {
    private final int timeIndex;
    private final int frequencyBin;

    public Fingerprint(int timeIndex, int frequencyBin) {
        this.timeIndex = timeIndex;
        this.frequencyBin = frequencyBin;
    }

    public int getTimeIndex() {
        return timeIndex;
    }

    public int getFrequencyBin() {
        return frequencyBin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fingerprint)) return false;
        Fingerprint that = (Fingerprint) o;
        return timeIndex == that.timeIndex && frequencyBin == that.frequencyBin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeIndex, frequencyBin);
    }

    @Override
    public String toString() {
        return "Fingerprint{" +
                "time=" + timeIndex +
                ", freqBin=" + frequencyBin +
                '}';
    }
}
