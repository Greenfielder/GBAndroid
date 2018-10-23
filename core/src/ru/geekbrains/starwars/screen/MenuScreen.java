package ru.geekbrains.starwars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.starwars.base.Base2DScreen;

public class MenuScreen extends Base2DScreen {

    private SpriteBatch batch;
    private Texture img;
    private Texture bg;
    private TextureRegion region;
    private Vector2 pos;
    private Vector2 v;
    private Vector2 direction;
    private float speed = 0.2F;

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        bg = new Texture("bg.jpg");
        region = new TextureRegion(bg, 0, 0, 900, 400);
        pos = new Vector2(0, 0);
        v = new Vector2(0f, 0f);
//        v = new Vector2(0.5f,0.3f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0.128f, 0.53f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(region, 0, 0);
        batch.draw(img, pos.x, pos.y);
        batch.end();
        pos.add(v);
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        return super.touchDown(screenX, screenY, pointer, button);
//Не получилось реализовать перемешение картинки в движении

                pos.x = screenX - img.getWidth()/2;
                pos.y = Gdx.graphics.getHeight() - screenY - img.getHeight()/2;
                direction = new Vector2(pos.x, pos.y);
                direction.nor();
//               v = direction.cpy().add(pos);
//                v = direction.cpy().scl(speed * Gdx.graphics.getDeltaTime());

        return false;
    }
}