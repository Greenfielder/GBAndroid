package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.List;

import ru.geekbrains.base.Base2DScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.GameOverMsg;
import ru.geekbrains.utils.EnemiesEmmiter;


public class GameScreen extends Base2DScreen {

    private static final int STAR_COUNT = 64;
    private enum Сondition {PLAYING, GAME_OVER}

    private Texture bgTexture;
    private Background background;

    private TextureAtlas textureAtlas;
    private TextureAtlas textureAtlas2;
    private TextureAtlas textureAtlas3;
    private Star[] stars;
    private Сondition сondition;

    private Music bgmusic;
    private Sound soundShoot;
    private Sound enemyShootSound;
    private Sound explosionSound;

    private MainShip mainShip;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemiesEmmiter enemiesEmmiter;
    private ExplosionPool explosionPool;
    private GameOverMsg gameOverMsg;

    @Override
    public void show() {
        super.show();
        bgTexture = new Texture("bg.jpg");
        background = new Background(new TextureRegion(bgTexture));
        textureAtlas = new TextureAtlas("sw.pack");
        textureAtlas2 = new TextureAtlas("mainAtlas.tpack");
        textureAtlas3 = new TextureAtlas("gametx.pack");

        bgmusic = Gdx.audio.newMusic(Gdx.files.internal("mp3/level1.mp3"));
        bgmusic.setLooping(true);
        bgmusic.play();

        soundShoot = Gdx.audio.newSound(Gdx.files.internal("mp3/fire.mp3"));
        enemyShootSound = Gdx.audio.newSound(Gdx.files.internal("mp3/fireEnemy.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("mp3/boom.wav"));

        stars =new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(textureAtlas);
        }

        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(textureAtlas2, explosionSound);
        mainShip = new MainShip(textureAtlas3, bulletPool, explosionPool, soundShoot);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, enemyShootSound);
        enemiesEmmiter = new EnemiesEmmiter(enemyPool, worldBounds, textureAtlas3);
        gameOverMsg = new GameOverMsg(textureAtlas2);
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
//        for (int i = 0; i < stars.length; i++) {
//            stars[i].update(delta);
//        }
//
//        mainShip.update(delta);
//        bulletPool.updateActiveObjects(delta);
//        enemyPool.updateActiveObjects(delta);
//        explosionPool.updateActiveObjects(delta);
//        enemiesEmmiter.generate(delta);

        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }

        if (!mainShip.isDestroyed()) {
            сondition = Сondition.PLAYING;
        }else {
            сondition = Сondition.GAME_OVER;
        }
        switch (сondition) {
            case PLAYING:
        mainShip.update(delta);
        bulletPool.updateActiveObjects(delta);
        enemyPool.updateActiveObjects(delta);
        explosionPool.updateActiveObjects(delta);
        enemiesEmmiter.generate(delta);
                break;
            case GAME_OVER:
                break;
        }
    }

    public void checkCollisions() {
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst2(mainShip.pos) < minDist * minDist) {
                enemy.destroy();
                mainShip.destroy();
                return;
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed() || bullet.getOwner() == mainShip) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                bullet.destroy();
                mainShip.damage(bullet.getDamage());
                return;
            }
        }

        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.isDestroyed() || bullet.getOwner() != mainShip) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    bullet.destroy();
                    enemy.damage(bullet.getDamage());
                    return;
                }
            }
        }
    }

    public void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    public void draw() {
        Gdx.gl.glClearColor(0.128f, 0.53f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }

        if (!mainShip.isDestroyed()) {
            mainShip.draw(batch);
        }
        bulletPool.drawActiveObjects(batch);
        enemyPool.drawActiveObjects(batch);
        explosionPool.drawActiveObjects(batch);
        if (сondition == Сondition.GAME_OVER) {
            batch.setColor(1, 1, 1, 1);
            gameOverMsg.draw(batch);
            batch.setColor(1, 1, 1, 0.45f);
        } else {
            batch.setColor(1, 1, 1, 1);
        }
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