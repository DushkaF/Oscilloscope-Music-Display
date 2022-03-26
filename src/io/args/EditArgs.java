package io.args;

public class EditArgs {

    public int k;
    public double sigma;
    public double highThresholdRatio;
    public double lowThresholdRatio;

    public EditArgs(){
        k=2;
        sigma=1;
        highThresholdRatio=0.7;
        lowThresholdRatio=0.3;
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
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "EditArgs{" +
                "k=" + k +
                ", sigma=" + sigma +
                ", highThresholdRatio=" + highThresholdRatio +
                ", lowThresholdRatio=" + lowThresholdRatio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditArgs editArgs = (EditArgs) o;
        return k == editArgs.k && Double.compare(editArgs.sigma, sigma) == 0;
    }

}