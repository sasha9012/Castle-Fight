package com.castlefight.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.castlefight.client.GameClient;
import com.castlefight.client.GameRenderer;
import com.castlefight.helpers.InputHandler;

public class GameplayScreen implements Screen {
    static MainGameClass game;
    static MainMenuScreen mainMenuScreen;

    static GameRenderer renderer;
    GameClient client;

    InputMultiplexer inputMultiplexer;


    public GameplayScreen(MainGameClass game, GameRenderer renderer, MainMenuScreen mainMenuScreen) {


        GameplayScreen.game = game;
        GameplayScreen.mainMenuScreen = mainMenuScreen;
        client = MainMenuScreen.client;
        GameplayScreen.renderer = renderer;


        inputMultiplexer = new InputMultiplexer(new GestureDetector(new InputHandler(renderer)), renderer.getUiRenderer(), GameClient.getGameWorld());


    }

    public static void toMainMenu() {
        GameClient.disconnect();
        GameRenderer.setUiVisibility(false);
        game.setScreen(mainMenuScreen);
    }


    @Override
    public void show() {
        GameRenderer.setUiVisibility(true);
        Gdx.input.setInputProcessor(inputMultiplexer);
        GameClient.getGameWorld().initialize();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        renderer.render(delta);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        Gdx.app.log("GameplayScreen", "pause called");
    }

    @Override
    public void resume() {
        Gdx.app.log("GameplayScreen", "resume called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameplayScreen", "hide called");
    }

    @Override
    public void dispose() {
    }


}
