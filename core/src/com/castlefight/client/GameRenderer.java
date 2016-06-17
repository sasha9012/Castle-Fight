package com.castlefight.client;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.castlefight.game.MainGameClass;

public class GameRenderer {
    private MainGameClass game;
    private OrthographicCamera camera;

    Vector3 targetCamPos;

    GameClient client;

    Texture forest = new Texture("mapres/forest.gif");
    UIRenderer uiRenderer;
    static boolean uiVisibility = false;
    Animator animator;


    public GameRenderer(MainGameClass game, GameClient client) {


        this.game = game;
        this.client = client;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1080, 720);
        targetCamPos = camera.position;


        game.getBatch().setProjectionMatrix(camera.combined);
        uiRenderer = new UIRenderer(game);
        animator = new Animator();


    }

    public void moveCamera(Vector3 vector3) {
        targetCamPos = vector3;

    }

    public void render(float delta) {


        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.position.lerp(targetCamPos, 0.1f);
        if (camera.position.x < camera.viewportWidth / 2)
            camera.position.x = camera.viewportWidth / 2;
        if (camera.position.x > 3840 - camera.viewportWidth / 2)
            camera.position.x = 3840 - camera.viewportWidth / 2;

        camera.update();

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();


        for (int i = 0, n = LocalGameWorld.WORLD_WIDTH / ((int) camera.viewportWidth / 2) + 1; i < n; i++) {
            game.getBatch().draw(forest, (camera.viewportWidth / 2) * i, 0, camera.viewportWidth / 2, camera.viewportHeight);
        }

        //TODO
        uiRenderer.act(delta);

        GameClient.gameWorld.act(delta);
        GameClient.gameWorld.draw(game.getBatch());


        game.getBatch().end();

        if (uiVisibility) {
            uiRenderer.draw();
        }

    }

    public static void setUiVisibility(boolean uiVisibility1) {
        uiVisibility = uiVisibility1;
    }

    public void dispose() {
        uiRenderer.dispose();
        forest.dispose();
        animator.skin.dispose();
    }

    //GETTERS

    public Camera getCamera() {
        return camera;
    }

    public UIRenderer getUiRenderer() {
        return uiRenderer;
    }
}

