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
    private Thread outputThread;

    public Output(OutputArgs outputArgs) {
        this.outputArgs = outputArgs;
    }

    public void draw(LinkedList<Vector> map, double kFactor) {
        //System.out.println("k " + kFactor);
        for (Vector nextVector : map) {
            if (nextVector.visible) {
                int startX = (int) ((double) nextVector.start.x * kFactor - Math.pow(2, outputArgs.sampleSizeInBits) / 2.0);
                int startY = (int) (-(double) nextVector.start.y * kFactor + Math.pow(2, outputArgs.sampleSizeInBits) / 2.0);
                int endX = (int) ((double) nextVector.end.x * kFactor - Math.pow(2, outputArgs.sampleSizeInBits) / 2.0);
                int endY = (int) (-(double) nextVector.end.y * kFactor + Math.pow(2, outputArgs.sampleSizeInBits) / 2.0);

                Point convertedStart = new Point(startX, startY, 0, 0);
                Point convertedEnd = new Point(endX, endY, 0, 0);
//                System.out.println("[" + convertedStart.toString() + " " + convertedEnd.toString() + "] " + nextVector.visible);

                drawBresenhamLine(  convertedStart, convertedEnd);
            }
        }

        createTone(1);
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

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
    LinkedList<int[]> outputReadyStream = new LinkedList<>();



    private void createTone(float time) {
        float rate = outputArgs.getAudioFormat().getSampleRate();
        for (int t = 0; t < (rate * time / outputReadyStream.size()); t++) {
            for (int[] nextPoint : outputReadyStream) {
                byte[] buf = new byte[]{(byte) nextPoint[0], (byte) nextPoint[1]};
                byteArrayOutputStream.write(buf, 0, 2);
            }
        }
        System.out.println("End form");
    }


    private void drawBresenhamLine(Point start, Point end) {
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
        outputReadyStream.addLast(new int[]{x, y});

        err = el / 2;
        for (int t = 0; t < el; t++)
        {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;
                y += incy;
            } else {
                x += pdx;
                y += pdy;
            }
            outputReadyStream.addLast(new int[]{x, y});
        }
    }


    private void playAudio() {
        try {
            byte audioData[] = byteArrayOutputStream.toByteArray();
            InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);

            AudioFormat audioFormat = outputArgs.getAudioFormat();
            audioInputStream = new AudioInputStream(
                    byteArrayInputStream,
                    audioFormat,
                    audioData.length / audioFormat.getFrameSize());

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            Thread playThread = new Thread(new PlayThread(sourceDataLine, audioInputStream));
            playThread.start();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

}
