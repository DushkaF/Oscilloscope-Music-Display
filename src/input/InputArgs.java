package input;

public class InputArgs {

    public InputArgs(String path) {

    }

    public String help() {
        return "To change input args print: '-o_COMMAND', where COMMAND is one from the list: "+getListOfComands();
    }

    private String getListOfComands(){
        return null;
    }
    public void command(String inputMessage) {
    }
}
