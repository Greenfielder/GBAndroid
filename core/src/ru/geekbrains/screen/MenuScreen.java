package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Star2DGame;
import ru.geekbrains.base.Base2DScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.BigStar;
import ru.geekbrains.sprite.ExitButtom;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.StartButtom;

public class MenuScreen extends Base2DScreen {

    private static final int STAR_COUNT = 256;

    private Vector2 touch;
    private Texture bgTexture;
    private Background background;
    private StartButtom startButtom;
    private ExitButtom exitButtom;

    private TextureAtlas textureAtlas;
    private Star[] stars;
    private BigStar[] bigStars;


    @Override
    public void show() {
        super.show();
        bgTexture = new Texture("bg.jpg");
        background = new Background(new TextureRegion(bgTexture));

        textureAtlas = new TextureAtlas("sw.pack");

        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(textureAtlas);
        }

        bigStars = new BigStar[20];
        for (int i = 0; i < bigStars.length; i++) {
            bigStars[i] = new BigStar(textureAtlas);
        }

        startButtom = new StartButtom(textureAtlas);
        exitButtom = new ExitButtom(textureAtlas);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();

    }

    public void update(float delta) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }

        for (int j = 0; j < bigStars.length; j++) {
            bigStars[j].update(delta);
        }
    }

    public void draw() {
        Gdx.gl.glClearColor(0.128f, 0.53f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        for (int j = 0; j < bigStars.length; j++) {
            bigStars[j].draw(batch);
        }
        exitButtom.draw(batch);
        startButtom.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);

        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }

        for (int j = 0; j < bigStars.length; j++) {
            bigStars[j].resize(worldBounds);
        }

//        exitButtom.resize(worldBounds);  ---! Если ставлю мировые координаты, кнопка почти на весь экран изменение setHeightProportion не помогает
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
        textureAtlas.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {

        if (exitButtom.touchDown(touch, pointer)) {
            exitButtom.setScale(0.8f);
        }

        //        if (touch.x > -0.15785715 || touch.x < 0.15499997 ||
//                touch.y > -0.19714287 || touch.y < -0.10142857) {
//            exitButtom.setScale(0.8f);
//    }
//        if (touch.x > exitButtom.getLeft() || touch.x < exitButtom.getRight() ||
//                touch.y > exitButtom.getBottom() || touch.y < exitButtom.getTop()) {
//            exitButtom.setScale(1f);
//        }
//        Та и не нашёл ни одного метода, который бы определял границы кнопки.

        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (touch.x > exitButtom.getLeft() || touch.x < exitButtom.getRight() ||
                touch.y > exitButtom.getBottom() || touch.y < exitButtom.getTop()) {
            exitButtom.setScale(1f);
        }

        return super.touchUp(touch, pointer);
    }
}
