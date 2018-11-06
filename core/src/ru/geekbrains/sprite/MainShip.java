package ru.geekbrains.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;


public class MainShip extends Sprite {

    private static final int DEAD_END = -1;

    private Vector2 v0 = new Vector2(0.5f, 0);
    private Vector2 v = new Vector2();

    private boolean pressedLeft;
    private boolean pressedRight;

    private int left = DEAD_END;
    private int right = DEAD_END;
    private BulletPool bulletPool;

    private TextureAtlas atlas;

    private Rect worldBounds;

//    public MainShip(TextureAtlas atlas, BulletPool bulletPool) {
//        super(atlas.findRegion("main_ship"), 1, 2, 2);
//        this.atlas = atlas;
//        setHeightProportion(0.15f);
//        this.bulletPool = bulletPool;
//    }

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, Sound sound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.atlas = atlas;
        setHeightProportion(0.15f);
        this.bulletPool = bulletPool;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);

        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }

    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + 0.05f);
    }


    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.pos.x) {
            if (left != DEAD_END) return false;
            left = pointer;
            moveLeft();
        } else {
            if (right != DEAD_END) return false;
            right = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (pointer == left) {
            left = DEAD_END;
            if (right != DEAD_END) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == right) {
            right = DEAD_END;
            if (left != DEAD_END) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }


    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, atlas.findRegion("bulletMainShip"), pos, new Vector2(0, 0.5f), 0.01f, worldBounds, 1);
    }
}