package output;

public class OutputArgs {

    public OutputArgs(String path) {

    }

    /**
     * Метод вывода в консоль сообщения о неправильном формате ввода команды
     * @return список доступных команд и их формат
     */
    public String help() {
        return "To change output args print: '-o_COMMAND', where COMMAND is one from the list: "+getListOfComands();
    }

    private String getListOfComands(){
        return null;
    }

    public void command(String inputMessage) {

    }
}
