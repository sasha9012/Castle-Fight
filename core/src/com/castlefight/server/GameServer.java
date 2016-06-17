package com.castlefight.server;


import com.badlogic.gdx.Gdx;
import com.castlefight.client.GameClient;
import com.castlefight.client.UIRenderer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;


public class GameServer extends Listener {
    static Server server;


    public GameServer() throws IOException {
        server = new Server();
        server.bind(Network.port, Network.port);

        Network.register(server);

        UIRenderer.setLabel("Waiting for connection");

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                Gdx.app.log("SERVER", "CONNECTED " + connection.getID());
                if (connection.getID() == 2)
                    initializeGame();
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                GameClient.playerTwoConnected = false;
                dispose();
            }

            @Override
            public void received(Connection connection, Object object) {
                if (GameClient.playerTwoConnected) {
                    if (object instanceof Network.ClientGameMessage) {
                        server.sendToAllTCP(object);
                    }
                }

            }
        });


        server.start();


    }

    public static void dispose() {
        if (server!=null)
            server.stop();
    }

    private void initializeGame() {
        server.sendToAllTCP(new Network.InitializeGame());
    }


}
