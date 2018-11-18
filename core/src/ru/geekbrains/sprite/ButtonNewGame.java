package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ActionListener;
import ru.geekbrains.base.ScaledTouchUpButton;
import ru.geekbrains.math.Rect;


public class ButtonNewGame extends ScaledTouchUpButton {
    public ButtonNewGame(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("new_game"), actionListener);
        setHeightProportion(0.25f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.setRight(-0.05f);
    }
}