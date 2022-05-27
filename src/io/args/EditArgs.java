package io.args;

import static java.lang.Math.toRadians;

public class EditArgs {

    public int k;
    public double sigma;
    public double highThresholdRatio;
    public double lowThresholdRatio;
    public byte numOfHyst;
    public double tau;
    public int regMinSize;
    public short numOfTries;
    public int radius;

    public EditArgs(){
        k=2;
        sigma=1;
        highThresholdRatio=0.7;
        lowThresholdRatio=0.3;
        numOfHyst=1;
        tau=toRadians(11.25);
        regMinSize=100;
        numOfTries=0;
        radius=5;
    }

    public byte command(String message) {
        if (message.length() < 4) {
            return -1;
        } else {
            String[] ms = message.substring(3).split(" ");
            if (ms.length != 2) return -1;
            switch (ms[0]) {
                case "k":
                    short sh = -1;
                    try {
                        sh = Short.parseShort(ms[1]);
                        if (sh < 0 || sh > 100) return -1;
                        k = sh;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "highThrRat":
                    double high = -1;
                    try {
                        high = Double.parseDouble(ms[1]);
                        if ( high < 0 ||  high > 100) return -1;
                        highThresholdRatio =  high;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "lowThrRat":
                    double low = -1;
                    try {
                        low = Double.parseDouble(ms[1]);
                        if (low< 0 || low > 100) return -1;
                        lowThresholdRatio = low;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "sigma":
                    double sig = -1;
                    try {
                        sig = Double.parseDouble(ms[1]);
                        if (sig < 0 || sig > 100) return -1;
                        sigma = sig;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "numOfHyst":
                    byte num = -1;
                    try {
                        num = Byte.parseByte(ms[1]);
                        if (num < 0 || num > 100) return -1;
                        numOfHyst = num;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "tau":
                    double t = -1;
                    try {
                        t = Double.parseDouble(ms[1]);
                        if (t < 0 || t > 45) return -1;
                        tau = toRadians(t);
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "regMinSize":
                    int rgm = -1;
                    try {
                        rgm = Integer.parseInt(ms[1]);
                        if (rgm < 0 || rgm > 100000) return -1;
                        regMinSize = rgm;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "numOfTries":
                    short not = -1;
                    try {
                        not = Short.parseShort(ms[1]);
                        if (not < 0 || not > 1000) return -1;
                        numOfTries = not;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
                case "radius":
                    int r = -1;
                    try {
                        r = Integer.parseInt(ms[1]);
                        if (r <= 0 || r > 1000) return -1;
                        radius = r;
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                    break;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditArgs editArgs = (EditArgs) o;
        return k == editArgs.k && Double.compare(editArgs.sigma, sigma) == 0 && Double.compare(editArgs.highThresholdRatio, highThresholdRatio) == 0 && Double.compare(editArgs.lowThresholdRatio, lowThresholdRatio) == 0 && numOfHyst == editArgs.numOfHyst && Double.compare(editArgs.tau, tau) == 0;
    }

    @Override
    public String toString() {
        return "EditArgs{" +
                "k=" + k +
                ", sigma=" + sigma +
                ", highThresholdRatio=" + highThresholdRatio +
                ", lowThresholdRatio=" + lowThresholdRatio +
                ", numOfHyst=" + numOfHyst +
                ", tau=" + tau +
                ", regMinSize=" + regMinSize +
                ", numOfTries=" + numOfTries +
                '}';
    }
}