package io.args;

import javax.sound.sampled.AudioFormat;

import java.util.Arrays;

import static java.lang.Math.toRadians;

public class OutputArgs {
    public float sampleRate;
    public float sampleRateList[]={8000,11025,16000,22050,44100, 192000};
    //{8000,11025,16000,22050,44100, 192000}
    public int sampleSizeInBits; //TODO 16 bit
    //8,16
    public int channels = 2; // do not change
    //1,2
    public boolean signed = true;   // do not change
    //true,false
    public boolean bigEndian = false; // do not change
    public String savePath; //save path to audio file

    public AudioFormat getAudioFormat() {
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }
    public OutputArgs(){
        savePath="";
        sampleRate = 192000;
        sampleSizeInBits = 8;
    }

    public byte command(String message) {
        if (message.length() < 4) {
            return -1;
        } else {
            String[] ms = message.substring(3).split(" ");
            if (ms.length != 2) return -1;
            switch (ms[0]) {
                case "sampleRate":
                    float sR = -1;
                    try {
                        sR = Float.parseFloat(ms[1]);
                        boolean flag=false;
                        for (float f :sampleRateList) {
                            if(f==sR)flag=true;
                        }
                        if(!flag)return -1;
                        sampleRate=sR;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "sampleSize":
                    int sS = -1;
                    try {
                        sS = Integer.parseInt(ms[1]);
                        if(!(sS==16||sS==8))return -1;
                        sampleSizeInBits=sS;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                default:
                    return -1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "OutputArgs{" +
                "sampleRate=" + sampleRate +
                ", sampleRateList=" + Arrays.toString(sampleRateList) +
                ", sampleSizeInBits=" + sampleSizeInBits +
                ", channels=" + channels +
                ", signed=" + signed +
                ", bigEndian=" + bigEndian +
                ", savePath='" + savePath + '\'' +
                '}';
    }
}
