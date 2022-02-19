package io.args;

public class InputArgs {
    @Override
    public String toString() {
        return "InputArgs{" +
                "picturePath='" + picturePath + '\'' +
                ", inPicture=" + inPicture +
                ", someClass=" + someClass +
                '}';
    }

    public String picturePath;
    public boolean inPicture;
    public SomeClass someClass;
}
class SomeClass{
    @Override
    public String toString() {
        return "SomeClass{" +
                "blabla='" + blabla + '\'' +
                '}';
    }

    public String blabla;
}