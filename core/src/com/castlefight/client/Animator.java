package com.castlefight.client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.castlefight.gameobjects.GameConstants;

//USED TO STORE ANIMATIONS

public class Animator {


    Skin skin = new Skin();
    public static Animation knightWalk, knightAttack, knightDeath, rangerWalk, rangerAttack, rangerDeath, mageWalk, mageAttack, mageDeath, magicBall;
    public static Texture arrow;

    public Animator() {
        skin.addRegions(new TextureAtlas("units/knight.pack"));
        skin.addRegions(new TextureAtlas("units/ranger.pack"));
        skin.addRegions(new TextureAtlas("units/mage.pack"));
        skin.addRegions(new TextureAtlas("units/magic.pack"));
        knightWalk = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("kw0"), skin.getRegion("kw1"), skin.getRegion("kw2"));
        knightAttack = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("ka0"), skin.getRegion("ka1"), skin.getRegion("ka2"));
        knightDeath = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("kd0"), skin.getRegion("kd1"), skin.getRegion("kd2"), skin.getRegion("kd3"), skin.getRegion("kd4"));
        knightDeath.setPlayMode(Animation.PlayMode.NORMAL);
        rangerWalk = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("rw0"), skin.getRegion("rw1"), skin.getRegion("rw2"), skin.getRegion("rw3"), skin.getRegion("rw4"));
        rangerAttack = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("ra0"), skin.getRegion("ra1"), skin.getRegion("ra2"));
        rangerDeath = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("rd0"), skin.getRegion("rd1"), skin.getRegion("rd2"), skin.getRegion("rd3"));
        rangerDeath.setPlayMode(Animation.PlayMode.NORMAL);
        mageWalk = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("mw0"), skin.getRegion("mw1"), skin.getRegion("mw2"), skin.getRegion("mw3"));
        mageAttack = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("ma0"), skin.getRegion("ma1"), skin.getRegion("ma2"), skin.getRegion("ma3"), skin.getRegion("ma4"));
        mageDeath = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("md0"), skin.getRegion("md1"), skin.getRegion("md2"), skin.getRegion("md3"), skin.getRegion("md4"));
        mageDeath.setPlayMode(Animation.PlayMode.NORMAL);
        magicBall = new Animation(GameConstants.ANIMATION_SPEED, skin.getRegion("b0"), skin.getRegion("b1"), skin.getRegion("b2"));
        arrow = new Texture("units/arrow.png");
    }




}
