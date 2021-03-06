package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ActionListener;
import ru.geekbrains.base.ScaledTouchUpButton;
import ru.geekbrains.math.Rect;

public class StartButtom extends ScaledTouchUpButton {

    public StartButtom(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("start"), actionListener);
        setHeightProportion(0.25f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.setRight(-0.05f);
    }

}