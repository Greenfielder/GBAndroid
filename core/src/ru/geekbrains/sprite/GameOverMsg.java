package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;

public class GameOverMsg extends Sprite {

    public GameOverMsg(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
        setHeightProportion(0.1f);
        setBottom(0.15f);
    }

}
