package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.base.Base2DScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.utils.EnemiesEmmiter;


public class GameScreen extends Base2DScreen {

    private static final int STAR_COUNT = 64;

    private Texture bgTexture;
    private Background background;

    private TextureAtlas textureAtlas;
    private TextureAtlas textureAtlas2;
    private TextureAtlas textureAtlas3;
    private Star[] stars;

    private Music bgmusic;
    private Sound soundShoot;
    private Sound enemyShootSound;

    private MainShip mainShip;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemiesEmmiter enemiesEmmiter;

    @Override
    public void show() {
        super.show();
        bgTexture = new Texture("bg.jpg");
        background = new Background(new TextureRegion(bgTexture));
        textureAtlas = new TextureAtlas("sw.pack");
//        textureAtlas2 = new TextureAtlas("mainAtlas.tpack");
        textureAtlas3 = new TextureAtlas("gametx.pack");

        bgmusic = Gdx.audio.newMusic(Gdx.files.internal("mp3/level1.mp3"));
        bgmusic.setLooping(true);
        bgmusic.play();

        soundShoot = Gdx.audio.newSound(Gdx.files.internal("mp3/fire.mp3"));
        enemyShootSound = Gdx.audio.newSound(Gdx.files.internal("mp3/fireEnemy.mp3"));

        stars =new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(textureAtlas);
        }

        bulletPool = new BulletPool();
        mainShip = new MainShip(textureAtlas3, bulletPool, soundShoot);
        enemyPool = new EnemyPool(bulletPool, worldBounds, enemyShootSound);
        enemiesEmmiter = new EnemiesEmmiter(enemyPool, worldBounds, textureAtlas3);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        deleteAllDestroyed();
        draw();
    }

    public void update(float delta) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }

        mainShip.update(delta);
        bulletPool.updateActiveObjects(delta);
        enemyPool.updateActiveObjects(delta);
        enemiesEmmiter.generate(delta);
    }

    public void checkCollisions() {

    }

    public void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
    }

    public void draw() {
        Gdx.gl.glClearColor(0.128f, 0.53f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveObjects(batch);
        enemyPool.drawActiveObjects(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
        textureAtlas.dispose();
        bgmusic.dispose();
        soundShoot.dispose();
        enemyShootSound.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
        return super.touchUp(touch, pointer);
    }
}