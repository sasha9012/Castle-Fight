package com.castlefight.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class MainGameClass extends Game {


    private SpriteBatch batch;
    public static BitmapFont font;
    public static BitmapFont logoFont;
    MainMenuScreen mainMenuScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.DARK_GRAY;
        parameter.incremental = true;
        font = new FreeTypeFontGenerator(Gdx.files.internal("font/font.ttf")).generateFont(parameter);
        parameter.size = 200;
        parameter.borderWidth = 10;
        parameter.borderColor = Color.GOLD;
        logoFont = new FreeTypeFontGenerator(Gdx.files.internal("font/font.ttf")).generateFont(parameter);
        mainMenuScreen = new MainMenuScreen(this);
        this.setScreen(mainMenuScreen);


    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        font.dispose();
        logoFont.dispose();
        batch.dispose();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }
    public BitmapFont getLogoFont() {return logoFont;}
}
