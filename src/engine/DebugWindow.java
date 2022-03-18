package engine;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import java.awt.*;

public class DebugWindow implements Runnable{
    private RenderWindow window;
    private Picture picture;
    @Override
    public void run() {
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        window = new RenderWindow();
        window.create(new VideoMode(toolkit.getScreenSize().width/2,toolkit.getScreenSize().height/2), "Oscilloscope_Music_Display_Debug");
        window.setFramerateLimit(30);
        while (window.isOpen()) {
            window.clear();
            window.draw(picture.getDrawable());
            window.display();
            // drawing part (redrawing for next frame)
            for(Event e: window.pollEvents()){
                switch (e.type) {
                    case CLOSED:
                        window.close();
                        break;
                }
            }
        }
    }

    public void setPicture(Picture picture) {
        this.picture=picture;
    }
}
