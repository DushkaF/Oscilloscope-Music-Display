package io.output;

import factory.vectors.Point;
import factory.vectors.Vector;
import io.args.OutputArgs;


import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedList;

public class Output implements Runnable {
    private OutputArgs outputArgs;
    private AudioFormat audioFormat;
    private Thread outputThread;
    private PlayThread playThread;
    private  Clip clip;

    public Output(OutputArgs outputArgs) {
        this.outputArgs = outputArgs;
        audioFormat = outputArgs.getAudioFormat();
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(LinkedList<Vector> map, double kFactor) {
        //System.out.println("k " + kFactor);
        LinkedList<int[]> outputReadyStream = new LinkedList<>();
        for (Vector nextVector : map) {
            if (nextVector.visible) {
                int startX = (int) ((double) nextVector.start.x * kFactor - Math.pow(2, outputArgs.sampleSizeInBits) / 2.0);
                int startY = (int) (-(double) nextVector.start.y * kFactor + Math.pow(2, outputArgs.sampleSizeInBits) / 2.0);
                int endX = (int) ((double) nextVector.end.x * kFactor - Math.pow(2, outputArgs.sampleSizeInBits) / 2.0);
                int endY = (int) (-(double) nextVector.end.y * kFactor + Math.pow(2, outputArgs.sampleSizeInBits) / 2.0);

                Point convertedStart = new Point(startX, startY, 0, 0);
                Point convertedEnd = new Point(endX, endY, 0, 0);
//                System.out.println("[" + convertedStart.toString() + " " + convertedEnd.toString() + "] " + nextVector.visible);

                drawBresenhamLine(convertedStart, convertedEnd, outputReadyStream);
            }
        }

        byteArrayOutputStream = createTone(outputReadyStream);
        playAudio();
    }

    public OutputArgs getArgs() {
        return outputArgs;
    }

    public void start() {
        outputThread = new Thread(this);
        outputThread.setName("OUTPUT_THREAD");
        outputThread.run();
    }

    @Override
    public void run() {

    }

    ByteArrayOutputStream byteArrayOutputStream ;
    SourceDataLine sourceDataLine;


    private ByteArrayOutputStream createTone(LinkedList<int[]> outputReadyStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int[] nextPoint : outputReadyStream) {
            byte[] buf = new byte[]{(byte) nextPoint[0], (byte) nextPoint[1]};
            outputStream.write(buf, 0, 2);
        }
        return outputStream;
    }


    private void drawBresenhamLine(Point start, Point end, LinkedList<int[]> outputList) {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

        dx = end.x - start.x;
        dy = end.y - start.y;

        incx = (int) Math.signum(dx);
        incy = (int) Math.signum(dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        if (dx > dy) {
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;
        }

        x = start.x;
        y = start.y;
        outputList.addLast(new int[]{x, y});

        err = el / 2;
        for (int t = 0; t < el; t++) {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;
                y += incy;
            } else {
                x += pdx;
                y += pdy;
            }
            outputList.addLast(new int[]{x, y});
        }
    }


    private void playAudio() {
        try {
            byte[] audioData = byteArrayOutputStream.toByteArray();
            InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
            AudioInputStream audioInputStream = new AudioInputStream(
                    byteArrayInputStream,
                    audioFormat,
                    audioData.length / audioFormat.getFrameSize());

//            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
//            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
//            sourceDataLine.open(audioFormat);
//            sourceDataLine.start();
//            playThread = new PlayThread(audioFormat);
//            Thread playThread = new PlayThread(audioFormat);
//            playThread.start();

            System.out.println(audioInputStream.getFormat().toString());
            clip.stop();
            clip.close();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

}
