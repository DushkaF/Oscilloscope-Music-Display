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
        System.out.println("k " + kFactor);
        for (Vector nextVector : map) {
            if (nextVector.visible) {
                int startX = (int) ((double) nextVector.start.x * kFactor - Math.pow(2, outputArgs.sampleBits) / 2.0);
                int startY = (int) (-(double) nextVector.start.y * kFactor + Math.pow(2, outputArgs.sampleBits) / 2.0);
                int endX = (int) ((double) nextVector.end.x * kFactor - Math.pow(2, outputArgs.sampleBits) / 2.0);
                int endY = (int) (-(double) nextVector.end.y * kFactor + Math.pow(2, outputArgs.sampleBits) / 2.0);

                Point convertedStart = new Point(startX, startY, 0, 0);
                Point convertedEnd = new Point(endX, endY, 0, 0);
                System.out.println("[" + convertedStart.toString() + " " + convertedEnd.toString() + "] " + nextVector.visible);

                drawBresenhamLine(  convertedStart, convertedEnd);
            }
        }

        createTone(50);
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

    private AudioFormat getAudioFormat() {
        float sampleRate = 192000;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 8; //TODO 16 bit
        //8,16
        int channels = 2;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void createTone(float time) {
        float rate = getAudioFormat().getSampleRate();
        System.out.println(rate);
        System.out.println("Time " + (rate * time / outputReadyStream.size()));
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

        dx = end.x - start.x;//проекция на ось икс
        dy = end.y - start.y;//проекция на ось игрек

        incx = (int) Math.signum(dx);
        incy = (int) Math.signum(dy);

        if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
        if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
        //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

        if (dx > dy) {
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;//тогда в цикле будем двигаться по y
        }

        x = start.x;
        y = start.y;
        err = el / 2;
        //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла
        outputReadyStream.addLast(new int[]{x, y});
        for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
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

            AudioFormat audioFormat = getAudioFormat();
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
