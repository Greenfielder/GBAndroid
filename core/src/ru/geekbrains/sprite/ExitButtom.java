package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.math.Rect;
import ru.geekbrains.base.ActionListener;
import ru.geekbrains.base.ScaledTouchUpButton;

public class ExitButtom extends ScaledTouchUpButton {

    public ExitButtom(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("exit"), actionListener);
        setHeightProportion(0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.setBottom(-0.07f);
    }
}
