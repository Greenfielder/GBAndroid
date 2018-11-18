package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.math.Rect;
import ru.geekbrains.base.ActionListener;
import ru.geekbrains.base.ScaledTouchUpButton;

public class ExitButtom extends ScaledTouchUpButton {

    public ExitButtom(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("exit"), actionListener);
        setHeightProportion(0.25f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.setLeft(0.05f);
    }
}
