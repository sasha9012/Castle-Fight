package com.castlefight.client;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.castlefight.game.GameplayScreen;
import com.castlefight.game.MainGameClass;

import java.util.concurrent.TimeUnit;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class UIRenderer extends Stage {



    Skin gameBtnSkin;
    TextureAtlas atlas = new TextureAtlas("gameui/gameBtns.pack");
    TextureAtlas atlas1 = new TextureAtlas("gameui/buttons.pack");
    ImageButton knightBtn, rangerBtn, mageBtn;
    static ButtonGroup<ImageButton> btnGroup;
    static Group btnGroup2;
    static Label status;
    float cooldown = 3;
    boolean onCooldown = false;
    static Viewport viewport = new StretchViewport(1024,576);


    public UIRenderer(final MainGameClass game) {
        super(viewport,game.getBatch());





        gameBtnSkin = new Skin();
        gameBtnSkin.addRegions(atlas);
        gameBtnSkin.add("btn", new Texture("gameui/btn.png"));
        gameBtnSkin.add("exit", new Texture("gameui/exitbtn.png"));
        gameBtnSkin.add("exitcl", new Texture("gameui/exitbtncl.png"));
        gameBtnSkin.addRegions(atlas1);

        btnGroup = new ButtonGroup<ImageButton>();
        btnGroup2 = new Group();

        knightBtn = new ImageButton(gameBtnSkin.getDrawable("k1"), null, gameBtnSkin.getDrawable("k2"));
        rangerBtn = new ImageButton(gameBtnSkin.getDrawable("b1"), null, gameBtnSkin.getDrawable("b2"));
        rangerBtn.setPosition(knightBtn.getWidth(), 0);
        mageBtn = new ImageButton(gameBtnSkin.getDrawable("m1"), null, gameBtnSkin.getDrawable("m2"));
        mageBtn.setPosition(200, 0);


        TextButton.TextButtonStyle tbStyle = new TextButton.TextButtonStyle(gameBtnSkin.getDrawable("btn"), null, null, game.getFont());

        TextButton one = new TextButton("1", tbStyle);
        one.setPosition(getWidth() - one.getWidth(), getHeight() / 2);
        one.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!onCooldown) {
                    setOnCoolDown();
                    GameClient.createUnit(0, getCheckedIndex(), GameClient.getID());
                }
            }
        });
        TextButton two = new TextButton("2", tbStyle);
        two.setPosition(one.getX(), one.getY() - 100);
        two.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!onCooldown) {
                    setOnCoolDown();
                    GameClient.createUnit(2, getCheckedIndex(), GameClient.getID());
                }
            }
        });

        TextButton three = new TextButton("3", tbStyle);
        three.setPosition(two.getX(), two.getY() - 100);
        three.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!onCooldown) {
                    setOnCoolDown();
                    GameClient.createUnit(1, getCheckedIndex(), GameClient.getID());
                }
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getFont(), Color.YELLOW);

        status = new Label("Waiting for connection", labelStyle);
        status.setPosition(getWidth() / 2 - status.getWidth() / 2, getHeight() / 2 - status.getHeight() / 2);

        ImageButton exitButton = new ImageButton(gameBtnSkin.getDrawable("exit"), gameBtnSkin.getDrawable("exitcl"));
        exitButton.setPosition(getWidth() - exitButton.getWidth(), getHeight() - exitButton.getHeight());
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameClient.disconnect();
                GameplayScreen.toMainMenu();
            }
        });


        btnGroup.add(knightBtn, rangerBtn, mageBtn);

        addActor(knightBtn);
        addActor(rangerBtn);
        addActor(mageBtn);
        btnGroup2.addActor(one);
        btnGroup2.addActor(two);
        btnGroup2.addActor(three);
        addActor(btnGroup2);
        addActor(status);
        addActor(exitButton);
    }

    void setOnCoolDown() {
        for (Actor button : btnGroup2.getChildren()) {
            button.addAction(sequence(fadeOut(cooldown / 2), fadeIn(cooldown / 2)));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        onCooldown = true;
                        TimeUnit.SECONDS.sleep((long) cooldown);
                        onCooldown = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }



    public static void setLabel(String string) {
        status.setText(string);
    }

    public static int getCheckedIndex() {
        return btnGroup.getCheckedIndex() + 1;
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        atlas1.dispose();
        gameBtnSkin.dispose();
    }



    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
