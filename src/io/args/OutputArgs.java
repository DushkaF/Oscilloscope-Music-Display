package io.args;

import javax.sound.sampled.AudioFormat;

public class OutputArgs {
    public float sampleRate = 192000;
    //8000,11025,16000,22050,44100, 192000
    public int sampleSizeInBits = 8; //TODO 16 bit
    //8,16
    public int channels = 2; // do not change
    //1,2
    public boolean signed = true;   // do not change
    //true,false
    public boolean bigEndian = false; // do not change
    public AudioFormat getAudioFormat() {
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    @Override
    public String toString() {
        return "OutputArgs{" +
                "isActive=" + isActive +
                ", savePath='" + savePath + '\'' +
                '}';
    }

    public boolean isActive;
    public String savePath;

    public byte command(String message) {
        return 0;
    }
}
