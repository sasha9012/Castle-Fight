package com.castlefight.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.castlefight.client.GameClient;
import com.castlefight.client.GameRenderer;
import com.castlefight.server.GameServer;
import com.castlefight.server.Network;

import java.io.IOException;


public class MainMenuScreen implements Screen {


    static int width = 1920;
    static int height = 1080;
    Vector3 moveCam = new Vector3();
    int camMoveSpeed = -10;

    private MainGameClass game;
    public static GameClient client;
    public GameServer server;
    private OrthographicCamera camera;
    GameRenderer renderer;
    private Stage stage;
    private Viewport viewport = new FitViewport(width, height);
    Skin menuSkin;
    public static TextButton.TextButtonStyle tbStyle;
    private TextureAtlas atlas;


    public MainMenuScreen(final MainGameClass game) {
        this.game = game;
        createClient();
        stage = new Stage(viewport);
        createButtons();


        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        renderer = new GameRenderer(game, client);

        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        renderer.render(delta);


        if (renderer.getCamera().position.x <= renderer.getCamera().viewportWidth / 2) {
            camMoveSpeed = -camMoveSpeed;
        }
        if (renderer.getCamera().position.x >= 3840 - renderer.getCamera().viewportWidth / 2) {
            camMoveSpeed = -camMoveSpeed;
        }

        moveCam.set(renderer.getCamera().position.x + camMoveSpeed, renderer.getCamera().position.y, 0);

        renderer.moveCamera(moveCam);

        camera.update();
        stage.act(delta);
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().end();
        stage.draw();

    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        Gdx.app.log("MainMenu", "pause");
    }

    @Override
    public void resume() {
        Gdx.app.log("MainMenu", "resume");
    }

    @Override
    public void hide() {
        Gdx.app.log("MainMenu", "hide");
    }

    @Override
    public void dispose() {
        renderer.dispose();
        atlas.dispose();
        stage.dispose();
        menuSkin.dispose();
        GameClient.dispose();
        //GameServer.dispose();
    }


    public void createClient() {
        try {

            client = new GameClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createServer() {
        try {
            Gdx.app.log("Server", "Creating");
            server = new GameServer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createButtons() {
        atlas = new TextureAtlas("gameui/buttons.pack");
        menuSkin = new Skin();
        menuSkin.addRegions(atlas);


        tbStyle = new TextButton.TextButtonStyle(menuSkin.getDrawable("normal"), menuSkin.getDrawable("clicked"), menuSkin.getDrawable("normal"), game.getFont());
        List.ListStyle lStyle = new List.ListStyle(game.getFont(), Color.YELLOW, Color.WHITE, menuSkin.getDrawable("normal"));
        ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle(menuSkin.getDrawable("normal"), null, null, null, null);


        final List list = new List(lStyle);
        list.setItems(new String[]{});
        final ScrollPane scrollPane = new ScrollPane(list, spStyle);
        scrollPane.setSize(800, 500);


        final Group connectGroup = new Group();
        TextButton connectButton = new TextButton("Connect", tbStyle);
        connectButton.setPosition(scrollPane.getX() + scrollPane.getWidth() - connectButton.getWidth(), scrollPane.getY() - connectButton.getHeight() + 1);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (list.getSelected()!=null) {
                    client.connect(list.getSelected().toString());
                    connectGroup.setVisible(false);
                    game.setScreen(new GameplayScreen(game, renderer, MainMenuScreen.this));
                    Gdx.app.log("Client",(GameClient.getID()+""));
                    renderer.moveCamera(new Vector3(3840,renderer.getCamera().position.y,0));
                }
            }
        });
        TextButton backButton = new TextButton("Back", tbStyle);
        backButton.setPosition(scrollPane.getX(), scrollPane.getY() - backButton.getHeight() + 1);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                connectGroup.setVisible(false);
            }
        });


        connectGroup.addActor(scrollPane);
        connectGroup.addActor(connectButton);
        connectGroup.addActor(backButton);
        connectGroup.setPosition(width / 2 - scrollPane.getWidth() / 2, height / 2 - scrollPane.getHeight() / 2);
        connectGroup.setVisible(false);

        final Group playGroup = new Group();
        final Group mpGroup = new Group();

        final TextButton playButton = new TextButton("PLAY", tbStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createServer();
                client.connect(Network.localhost);
                game.setScreen(new GameplayScreen(game, renderer, MainMenuScreen.this));
            }
        });
        playButton.setPosition(width / 2 - playButton.getWidth() / 2, height / 2 - playButton.getHeight() / 2);


        final TextButton hostButton = new TextButton("HOST", tbStyle);
        hostButton.setPosition(playButton.getX(), playButton.getY());
        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createServer();
                client.connect(Network.localhost);
                game.setScreen(new GameplayScreen(game, renderer, MainMenuScreen.this));
                renderer.moveCamera(new Vector3(0,renderer.getCamera().position.y,0));
            }
        });

        TextButton serverListButton = new TextButton("Servers", tbStyle);
        serverListButton.setPosition(width / 2 - serverListButton.getWidth() / 2, hostButton.getY() - 200);
        serverListButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connectGroup.setVisible(true);
                        list.setItems(client.findServers());
                    }
                }).start();

            }
        });

        TextButton backBtn = new TextButton("Back", tbStyle);
        backBtn.setPosition(width / 2 - backBtn.getWidth() / 2, serverListButton.getY() - 200);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mpGroup.setVisible(false);
                playGroup.setVisible(true);

            }
        });


        mpGroup.addActor(hostButton);
        mpGroup.addActor(serverListButton);
        mpGroup.addActor(backBtn);
        mpGroup.setVisible(false);


        TextButton multiPlayerButton = new TextButton("Multiplayer", tbStyle);
        multiPlayerButton.setPosition(width / 2 - multiPlayerButton.getWidth() / 2, playButton.getY() - 200);
        multiPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mpGroup.setVisible(true);
                playGroup.setVisible(false);
            }
        });

        TextButton exitButton = new TextButton("Exit", tbStyle);
        exitButton.setPosition((width / 2 - exitButton.getWidth() / 2), multiPlayerButton.getY() - 200);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Label.LabelStyle logoStyle = new Label.LabelStyle(game.getLogoFont(), Color.WHITE);
        Label logo = new Label("Castle\n Fight",logoStyle);
        logo.setPosition(width/2-logo.getWidth()/2,height/2+100);


        playGroup.addActor(multiPlayerButton);
        playGroup.addActor(exitButton);


        stage.addActor(logo);
        stage.addActor(playGroup);
        stage.addActor(mpGroup);
        stage.addActor(connectGroup);
    }

}

