package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Enemy;

public class EnemiesEmmiter {

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY = -0.3f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL =3f;
    private static final int ENEMY_SMALL_HP = 1;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.12f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.03f;
    private static final float ENEMY_MEDIUM_BULLET_VY = -0.3f;
    private static final int ENEMY_MEDIUM_BULLET_DAMAGE = 3;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL =3f;
    private static final int ENEMY_MEDIUM_HP = 3;

    private static final float ENEMY_MEDIUM1_HEIGHT = 0.14f;
    private static final float ENEMY_MEDIUM1_BULLET_HEIGHT = 0.04f;
    private static final float ENEMY_MEDIUM1_BULLET_VY = -0.27f;
    private static final int ENEMY_MEDIUM1_BULLET_DAMAGE = 5;
    private static final float ENEMY_MEDIUM1_RELOAD_INTERVAL =3f;
    private static final int ENEMY_MEDIUM1_HP = 5;

    private static final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.06f;
    private static final float ENEMY_BIG_BULLET_VY = -0.25f;
    private static final int ENEMY_BIG_BULLET_DAMAGE = 12;
    private static final float ENEMY_BIG_RELOAD_INTERVAL =4f;
    private static final int ENEMY_BIG_HP = 20;

    private TextureRegion[] enemySmallRegion;
    private TextureRegion[] enemyMediumOneRegion;
    private TextureRegion[] enemyMediumTwoRegion;
    private TextureRegion[] enemyBigRegion;

    private Vector2 enemySmallV = new Vector2(0, -0.2f);
    private Vector2 enemyMediumV = new Vector2(0, -0.03f);
    private Vector2 enemyBigV = new Vector2(0, -0.005f);

    private EnemyPool enemyPool;
    private Rect worldBounds;
    private TextureRegion bulletRegion;

    private float generateInterval = 4f;
    private float generateTimer;

    private int level;

    public EnemiesEmmiter(EnemyPool enemyPool, Rect worldBounds, TextureAtlas atlas) {

        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;

        TextureRegion textureRegion0 = atlas.findRegion("enemy_ship0");
        this.enemySmallRegion = Regions.split(textureRegion0, 1, 2, 2);

        TextureRegion textureRegion1 = atlas.findRegion("enemy_ship1");
        this.enemyMediumOneRegion = Regions.split(textureRegion1, 1, 2, 2);

        TextureRegion textureRegion3 = atlas.findRegion("enemy_ship3");
        this.enemyMediumTwoRegion = Regions.split(textureRegion3, 1, 2, 2);

        TextureRegion textureRegion2 = atlas.findRegion("enemy_ship2");
        this.enemyBigRegion = Regions.split(textureRegion2, 1, 2, 2);

        this.bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta,  int frags) {
        level = frags / 10 + 1;
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            Enemy enemy = enemyPool.obtain();
            float type = (float) Math.random();
            if (type < 0.4f) {
                enemy.set(
                        enemySmallRegion,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_BULLET_DAMAGE * level,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP * level
                );
            } else if (type < 0.6) {
                enemy.set(
                        enemyMediumOneRegion,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_VY,
                        ENEMY_MEDIUM_BULLET_DAMAGE * level,
                        ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY_MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_HP * level
                );
            } else if (type < 0.8) {
                enemy.set(
                        enemyMediumTwoRegion,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_MEDIUM1_BULLET_HEIGHT,
                        ENEMY_MEDIUM1_BULLET_VY,
                        ENEMY_MEDIUM1_BULLET_DAMAGE * level,
                        ENEMY_MEDIUM1_RELOAD_INTERVAL,
                        ENEMY_MEDIUM1_HEIGHT,
                        ENEMY_MEDIUM1_HP * level
                );
            }
            else {
                enemy.set(
                        enemyBigRegion,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_BULLET_DAMAGE,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP
                );
            }
            enemy.setBottom(worldBounds.getTop());
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfWidth());
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}