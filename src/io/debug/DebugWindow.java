package io.debug;

import factory.Picture;
import io.args.DebugArgs;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import java.awt.*;
import java.io.IOException;

public class DebugWindow implements Runnable{
    private RenderWindow window;
    private Picture picture;
    public DebugArgs debugArgs;

    public DebugWindow(Picture picture, DebugArgs debugArgs) {
        this.picture=picture;
        this.debugArgs=debugArgs;
    }

    @Override
    public void run() {
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        window = new RenderWindow();
        window.create(new VideoMode(toolkit.getScreenSize().width-320,toolkit.getScreenSize().height-180), "Oscilloscope_Music_Display_Debug");
        window.setFramerateLimit(30);
        Vector2i size = window.getSize();
        while (window.isOpen()) {
            // drawing part (redrawing for next frame)
            window.clear();
            try {
                Drawer.draw(picture, window, debugArgs, size);
            } catch (TextureCreationException | IOException e) {
                e.printStackTrace();
            }
            window.display();
            for(Event e: window.pollEvents()){
                switch (e.type) {
                    case CLOSED:
                        window.close();
                        break;
                }
            }
        }
    }
}
