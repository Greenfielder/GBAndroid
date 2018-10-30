package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class ExitButtom extends Sprite {

    public ExitButtom(TextureAtlas atlas) {
        super(atlas.findRegion("exit"));
        setHeightProportion(0.1f);
        this.setBottom(-0.2f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }

}
