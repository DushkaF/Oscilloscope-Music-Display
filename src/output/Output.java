package output;

import input.InputArgs;
import map.Map;

public class Output {
    private OutputArgs outputArgs;
    public Output(OutputArgs outputArgs) {
        this.outputArgs=outputArgs;
    }

    public void draw(Map map) {
    }

    public OutputArgs getArgs() {
        return outputArgs;
    }
}
