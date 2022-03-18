package engine;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import java.util.Random;

public class Picture {
    RectangleShape rec = new RectangleShape(new Vector2f(100, 100));
    Random random = new Random();
    public Drawable getDrawable() {
        rec.setFillColor(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256),random.nextInt(256)));

        return rec;
    }
}
