package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.ActionListener;
import ru.geekbrains.base.Base2DScreen;
import ru.geekbrains.base.Font;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.ButtonNewGame;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.ExitButtom;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.GameOverMsg;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.utils.EnemiesEmmiter;

public class GameScreen extends Base2DScreen implements ActionListener {

    private static final int STAR_COUNT = 64;

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHP = new StringBuilder();
    private StringBuilder sbLevel = new StringBuilder();

    private enum GameСondition {PLAYING, GAME_OVER}

    private Texture bgTexture;
    private Background background;

    private TextureAtlas textureAtlas;
    private TextureAtlas textureAtlas2;
    private TextureAtlas textureAtlas3;
    private Star[] stars;
    private GameСondition gameСondition;
    private int frags;

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
    private ButtonNewGame buttonNewGame;
    private ExitButtom exitButtom;
    private Font font;

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

        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(textureAtlas);
        }

        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(textureAtlas2, explosionSound);
        mainShip = new MainShip(textureAtlas3, bulletPool, explosionPool, soundShoot);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, enemyShootSound);
        enemiesEmmiter = new EnemiesEmmiter(enemyPool, worldBounds, textureAtlas3);
        gameOverMsg = new GameOverMsg(textureAtlas3);
        buttonNewGame = new ButtonNewGame(textureAtlas3, this);
        exitButtom = new ExitButtom(textureAtlas, this);

        font = new Font("font/font.fnt", "font/font.png");
        font.setFontSize(0.02f);
        startNewGame();
    }

    //    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        if (gameСondition == GameСondition.PLAYING) {
            checkCollisions();
        }
        deleteAllDestroyed();
        draw();
    }

    public void update(float delta) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }

        explosionPool.updateActiveObjects(delta);

        if (gameСondition == GameСondition.PLAYING) {
            bulletPool.updateActiveObjects(delta);
            enemyPool.updateActiveObjects(delta);
            mainShip.update(delta);
            enemiesEmmiter.generate(delta, frags);
            if (mainShip.isDestroyed()) {
                gameСondition = GameСondition.GAME_OVER;
            }
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
                gameСondition = GameСondition.GAME_OVER;
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
                if (mainShip.isDestroyed()) {
                    gameСondition = GameСondition.GAME_OVER;
                }
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
                    if (enemy.isDestroyed()) {
                        frags++;
                    }
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

        explosionPool.drawActiveObjects(batch);
        if (gameСondition == GameСondition.GAME_OVER) {
            gameOverMsg.draw(batch);
            buttonNewGame.draw(batch);
            exitButtom.draw(batch);
        } else {
            mainShip.draw(batch);
            bulletPool.drawActiveObjects(batch);
            enemyPool.drawActiveObjects(batch);
        }
        printInfo();
        batch.end();
    }

    public void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop());
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop(), Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemiesEmmiter.getLevel()), worldBounds.getRight(), worldBounds.getTop(), Align.right);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        exitButtom.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
        textureAtlas.dispose();
        bgmusic.dispose();
        soundShoot.dispose();
        enemyShootSound.dispose();
        font.dispose();
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
        if (gameСondition == GameСondition.PLAYING) {
            mainShip.touchDown(touch, pointer);
        } else {
            buttonNewGame.touchDown(touch, pointer);
            exitButtom.touchDown(touch, pointer);
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (gameСondition == GameСondition.PLAYING) {
            mainShip.touchUp(touch, pointer);
        } else {
            buttonNewGame.touchUp(touch, pointer);
            exitButtom.touchUp(touch, pointer);
        }
        return super.touchUp(touch, pointer);
    }


    private void startNewGame() {
        gameСondition = GameСondition.PLAYING;
        enemiesEmmiter.setLevel(1);
        frags = 0;
        mainShip.startNewGame();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == buttonNewGame) {
            startNewGame();
        } else if (src == exitButtom) {
            Gdx.app.exit();
        }
    }
}