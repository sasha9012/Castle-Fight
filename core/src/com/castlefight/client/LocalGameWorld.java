package com.castlefight.client;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.castlefight.game.GameplayScreen;
import com.castlefight.game.MainGameClass;
import com.castlefight.gameobjects.GameConstants;
import com.castlefight.gameobjects.State;
import com.castlefight.gameobjects.Units;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.concurrent.TimeUnit;


public class LocalGameWorld extends Stage {

    static final int WORLD_WIDTH = 3840;


    Texture castleTexture = new Texture("units/castle.png");
    Texture heart = new Texture("gameui/heart.png");
    Group units;
    Line[] lines = new Line[]{new Line(220), new Line(140), new Line(180)};
    Castle[] castles = new Castle[]{new Castle(0, 100, 1), new Castle(WORLD_WIDTH - castleTexture.getWidth(), 100, 2)};

    public static final float startX = 740;
    public static final float endX = WORLD_WIDTH - startX * 2;

    public LocalGameWorld() {
        units = new Group();
        addActor(castles[0]);
        addActor(castles[1]);

        for (int i = 1, n = lines.length + 1; i < n; i++) {
            units.addActorAt(n - i, lines[i - 1]);
        }
        addActor(units);

    }

    @Override
    public void dispose() {
        super.dispose();
        castleTexture.dispose();
        heart.dispose();
    }

    public void initialize() {
        castles[0].init();
        castles[1].init();

    }

    public void add(State state) {

        Units.Unit unit = null;
        switch (state.type) {
            case Units.UnitType.Knight:
                unit = new Units.Knight(state);
                break;
            case Units.UnitType.Ranger:
                unit = new Units.Ranger(state);
                break;
            case Units.UnitType.Mage:
                unit = new Units.Mage(state);
                break;
        }

        if (unit != null) {
            addToLine(state.line, unit);
        }

    }


    public void addToLine(int index, Units.Unit unit) {
        lines[index].add(unit);
    }

    public void damageCastle(int owner, int damage) {
        Gdx.app.log("Castle", "damaged " + damage);
        castles[owner - 1].getDamage(damage);
    }

    public void endGame(int owner) {
        UIRenderer.setLabel("Player " + (owner == 1 ? 2 : 1) + " won");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(4);
                    GameplayScreen.toMainMenu();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public void draw(Batch batch) {
        getRoot().draw(batch, 1);
    }


    private class Line extends Group {
        public Line(int y) {
            setX(startX);
            setY(y);
            setTouchable(Touchable.enabled);
            setBounds(getX(), getY(), endX, 50);

        }


        void add(Units.Unit unit) {
            switch (unit.owner) {
                case 1:
                    addActor(unit);
                    unit.move();
                    break;
                case 2:
                    unit.setPosition(endX, 0);
                    addActor(unit);
                    unit.move();
                    break;
            }
        }


    }


    private class Castle extends Actor {
        int owner = 0;
        int multiplier = 1;
        int hp = GameConstants.CASTLE_HP;
        Label label;
        private boolean alive = true;

        public Castle(int x, int y, int owner) {
            setX(x);
            setY(y);
            this.owner = owner;
            Label.LabelStyle style = new Label.LabelStyle(MainGameClass.font, Color.WHITE);
            label = new Label(hp + "", style);
            label.setPosition(getX() + castleTexture.getWidth() / 2, getY() + castleTexture.getHeight() / 2);
            addActor(label);

            if (owner == 2)
                multiplier = -multiplier;
        }

        void init() {
            hp = GameConstants.CASTLE_HP;
            alive = true;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            label.setText(hp + "");
            if (hp <= 0) {
                hp = 0;
                destroy();
            }

        }

        private void destroy() {
            if (alive & owner == GameClient.getID()) {
                alive = false;
                GameClient.notifyCastleDestroyed();
            }
        }


        void getDamage(int damage) {
            addAction(sequence(moveTo(getX()-20*multiplier,getY(),.1f),moveTo(getX(),getY(),.1f)));
            hp -= 1;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            switch (owner) {
                case 1:
                    batch.draw(castleTexture, getX(), getY());
                    break;
                case 2:
                    batch.draw(castleTexture, getX()+castleTexture.getWidth(), getY(),-castleTexture.getWidth(),castleTexture.getHeight());
                    break;
            }
            label.draw(batch, 1);
            batch.draw(heart,label.getX()+label.getWidth(),label.getY());

        }
    }
}
