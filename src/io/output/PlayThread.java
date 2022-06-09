package io.output;

import javax.sound.sampled.*;

public class PlayThread extends Thread {
    AudioFormat audioFormat;
    SourceDataLine sourceDataLine;
    AudioInputStream audioInputStream;
//    PlayThread(SourceDataLine _sourceDataLine, ) {
//        sourceDataLine = _sourceDataLine;
//    }

    PlayThread(AudioFormat _audioFormat) {
        audioFormat = _audioFormat;
//        audioInputStream = new AudioInputStream();
    }

    byte tempBuffer[] = new byte[10000];

    public void setCaptureStream(AudioInputStream newStream){
        audioInputStream = newStream;
    }

    public void run() {
        try {
            /*
            int cnt;

            while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                if (cnt > 0) {
                    sourceDataLine.write(tempBuffer, 0, cnt);
                }
            }
            sourceDataLine.drain();
            sourceDataLine.close();
             */

            Clip clip = AudioSystem.getClip();
            clip.open(audioFormat, audioInputStream.readAllBytes(), 0, audioInputStream.available());
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
