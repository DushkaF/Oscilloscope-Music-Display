package io.output;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;

public class PlayThread extends Thread {
    SourceDataLine sourceDataLine;
    AudioInputStream audioInputStream;
    PlayThread(SourceDataLine _sourceDataLine, AudioInputStream _audioInputStream) {
        sourceDataLine = _sourceDataLine;
        audioInputStream = _audioInputStream;
    }
    byte tempBuffer[] = new byte[10000];

    public void run() {
        try {
            int cnt;

            while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                if (cnt > 0) {
                    sourceDataLine.write(tempBuffer, 0, cnt);
                }
            }
            sourceDataLine.drain();
            sourceDataLine.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
