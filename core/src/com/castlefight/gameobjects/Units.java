package com.castlefight.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.castlefight.client.Animator;
import com.castlefight.client.GameClient;

import java.util.concurrent.TimeUnit;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


public class Units {


    public static class Knight extends Unit {

        public Knight(State state) {
            super(state);
            type = UnitType.Knight;
            initialize(Animator.knightWalk, Animator.knightAttack, Animator.knightDeath, GameConstants.KNIGHT_HP, GameConstants.KNIGHT_DAMAGE, GameConstants.KNIGHT_RANGE, GameConstants.KNIGHT_SPEED,GameConstants.KNIGHT_DELAY);
        }

        @Override
        public Actor hit(float x, float y, boolean touchable) {
            return super.hit(x, y, touchable);
        }
    }

    public static class Ranger extends Unit {

        public Ranger(State state) {
            super(state);
            type = UnitType.Ranger;
            initialize(Animator.rangerWalk, Animator.rangerAttack, Animator.rangerDeath, GameConstants.RANGER_HP, GameConstants.RANGER_DAMAGE, GameConstants.RANGER_RANGE, GameConstants.RANGER_SPEED,GameConstants.RANGER_DELAY);
        }
    }

    public static class Mage extends Unit {

        public Mage(State state) {
            super(state);
            type = UnitType.Mage;
            initialize(Animator.mageWalk, Animator.mageAttack, Animator.mageDeath, GameConstants.MAGE_HP, GameConstants.MAGE_DAMAGE, GameConstants.MAGE_RANGE, GameConstants.MAGE_SPEED,GameConstants.MAGE_DELAY);
        }
    }


    public static class Unit extends Actor {
        public int line = 0;
        public int owner = 0;
        public int type = 0;
        private Animation animation;
        Animation walk, attack, death;
        int multiplier = 1;
        private float stateTime = 0;
        int damage, range, hp,delay = 0;
        int speed = 1;
        Mode mode = Mode.move;
        Unit target;

        boolean temp = false;
        float tempTime = 0;

        boolean targetAble = true;

        public Unit(State state) {
            this.setName(random.nextInt() + "");
            owner = state.owner;
            line = state.line;
            if (owner == 2)
                multiplier = -multiplier;
        }

        void initialize(Animation walk, Animation attack, Animation death, int hp, int damage, int range, int speed,int delay) {
            this.walk = walk;
            this.attack = attack;
            this.death = death;
            this.hp = hp;
            this.damage = damage;
            this.range = range;
            this.speed = speed;
            this.delay = delay;

        }

        @Override
        public void act(float delta) {

            stateTime += delta;
            tempTime += delta;
            if (hp <= 0) {
                setMode(Mode.death);
            } else if (mode != Mode.death) {
                if (target == null) {
                    setMode(Mode.move);
                    for (Actor actor : getParent().getChildren()) {
                        if ((actor instanceof Unit) && (actor != this) && ((Unit) actor).owner != owner && ((Unit) actor).targetAble) {
                            if (owner == 1)
                                temp = actor.getX() - getX() < range & actor.getX() - getX() > 0;
                            else temp = getX() - actor.getX() < range & getX() - actor.getX() > 0;
                            if (temp) {
                                target = (Unit) actor;
                                setMode(Mode.attack);
                                break;
                            }
                        }
                    }
                } else if (!target.targetAble)
                    target = null;
            }
            super.act(delta);
        }


        void setMode(Mode mode) {
            if (this.mode != mode) {
                clearActions();
                this.mode = mode;
                switch (mode) {
                    case move:
                        move();
                        break;
                    case attack:
                        attack();
                        break;
                    case death:
                        death();
                }
            }
        }

        public void move() {
            animation = walk;
            switch (owner) {
                case 1:
                    addAction(sequence(moveTo(getParent().getWidth(), 0, (getParent().getWidth()-getX()) / speed), new Action() {
                        @Override
                        public boolean act(float delta) {
                            if (GameClient.getID() == 1)
                                GameClient.damageCastle(2, damage);
                            setMode(Mode.death);
                            return true;
                        }
                    }));
                    break;
                case 2:
                    addAction(sequence(moveTo(0, 0, getX() / speed), new Action() {
                        @Override
                        public boolean act(float delta) {
                            if (GameClient.getID() == 2)
                                GameClient.damageCastle(1, damage);
                            setMode(Mode.death);
                            return true;
                        }
                    }));
                    break;
            }
        }

        public void attack() {
            animation = attack;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mode == Mode.attack) {
                        try {
                            createBullet();
                            TimeUnit.MILLISECONDS.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        public void createBullet() {
            if (target != null) {
                Bullet bullet;
                switch (type){
                    case UnitType.Knight:
                        bullet = new Bullet(owner, target, range, damage);
                        break;
                    case UnitType.Ranger:
                        bullet = new Arrow(owner,target,range,damage);
                        break;
                    case UnitType.Mage:
                        bullet = new MagicBall(owner,target,range,damage);
                        break;
                    default:
                        bullet = new Bullet(owner, target, range, damage);
                }
                bullet.setPosition(owner == 1 ? getX() : getX() - 100, getY()+50);
                getParent().addActor(bullet);
                bullet.shot();
            }
        }

        public void death() {
            targetAble = false;
            animation = death;
            stateTime = 0;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Gdx.app.log("Thread", "removing " + this);
                        TimeUnit.MILLISECONDS.sleep(2000);
                        addAction(removeActor());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public void getDamage(int damage) {
            hp -= damage;
        }

        public boolean overlaps(float x) {
            return owner == 1 ? x < getX() : x > getX() - 100;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(animation.getKeyFrame(stateTime, mode != Mode.death), owner == 2 ? getX() : getX() - 100, getY(), multiplier * 100, 100);
        }

    }

    public static class UnitType {
        public static final int Knight = 1;
        public static final int Ranger = 2;
        public static final int Mage = 3;
    }

    enum Mode {
        move,
        attack,
        death
    }

    private static class Bullet extends Actor {
        int owner, range, damage,speed = 0;

        public Bullet(int owner, Unit target, int range, int damage) {
            this.owner = owner;
            this.range = range;
            this.damage = damage;
            this.speed = speed;
            Gdx.app.log("bullet", "creating");
        }

        public void shot() {
            addAction(sequence(moveTo(owner == 1 ? getX() + range : getX() - range, getY(), 2), removeActor()));
        }

        @Override
        public void act(float delta) {
            if (getParent().getChildren() != null) {
                for (Actor actor : getParent().getChildren()) {
                    if (actor instanceof Unit && actor != this) {
                        if (((Unit) actor).owner != owner && ((Unit) actor).targetAble)
                            if (((Unit) actor).overlaps(getX())) {
                                Gdx.app.log("bullet", "removing");
                                clearActions();
                                addAction(removeActor());
                                ((Unit) actor).getDamage(damage);
                                break;
                            }
                    }
                }

            }
            super.act(delta);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {

        }
    }


    private static class MagicBall extends Bullet{
        float stateTime = 0;
        public MagicBall(int owner, Unit target, int range, int damage) {
            super(owner, target, range, damage);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            stateTime+=delta;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(Animator.magicBall.getKeyFrame(stateTime), getX(), getY());
        }
    }

    private static class Arrow extends Bullet{
        private int multiplier;
        public Arrow(int owner, Unit target, int range, int damage) {
            super(owner, target, range, damage);
            if (owner == 1) multiplier = 1; else multiplier = -1;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(Animator.arrow, owner == 2 ? getX() : getX() - 36, getY(), multiplier * 36, 36);
        }
    }
}
