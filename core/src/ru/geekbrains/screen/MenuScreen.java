package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Music;

import ru.geekbrains.Star2DGame;
import ru.geekbrains.base.ActionListener;
import ru.geekbrains.base.Base2DScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.BigStar;
import ru.geekbrains.sprite.ExitButtom;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.StartButtom;

public class MenuScreen extends Base2DScreen implements ActionListener {

    private static final int STAR_COUNT = 256;
    private Game game;
    private Vector2 touch;
    private Texture bgTexture;
    private Background background;
    private StartButtom startButtom;
    private ExitButtom exitButtom;
    private Music mainmusic;

    private TextureAtlas textureAtlas;
    private Star[] stars;
    private BigStar[] bigStars;

    public MenuScreen(Game game) {
        super();
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bgTexture = new Texture("bg.jpg");
        background = new Background(new TextureRegion(bgTexture));
        textureAtlas = new TextureAtlas("sw.pack");

        mainmusic = Gdx.audio.newMusic(Gdx.files.internal("mp3/main.mp3"));
        mainmusic.setLooping(true);
        mainmusic.play();

        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(textureAtlas);
        }

        bigStars = new BigStar[20];
        for (int i = 0; i < bigStars.length; i++) {
            bigStars[i] = new BigStar(textureAtlas);
        }

        startButtom = new StartButtom(textureAtlas, this);
        exitButtom = new ExitButtom(textureAtlas, this );

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

        exitButtom.resize(worldBounds);
        startButtom.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
        textureAtlas.dispose();
        mainmusic.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        exitButtom.touchDown(touch, pointer);
        startButtom.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        exitButtom.touchUp(touch, pointer);
        startButtom.touchUp(touch, pointer);
        return super.touchUp(touch, pointer);
    }
    @Override
    public void actionPerformed(Object src) {
        if (src == exitButtom) {
            Gdx.app.exit();
        } else if (src == startButtom) {
            game.setScreen(new GameScreen());
        }
    }
}
