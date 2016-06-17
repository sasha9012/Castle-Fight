package com.castlefight.client;

import com.castlefight.game.GameplayScreen;
import com.castlefight.gameobjects.State;
import com.castlefight.server.Network;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;


public class GameClient {
    public static boolean playerTwoConnected = false;
    static Client client;
    static LocalGameWorld gameWorld;

    public GameClient() throws IOException {
        client = new Client();
        gameWorld = new LocalGameWorld();


        Network.register(client);

        client.addListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                GameplayScreen.toMainMenu();
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Network.InitializeGame) {
                    playerTwoConnected = true;
                    GameRenderer.setUiVisibility(true);
                    UIRenderer.setLabel("");
                }


                if (object instanceof Network.CreateUnit) {
                    gameWorld.add(((Network.CreateUnit) object).state);
                }
                if (object instanceof Network.DamageCastle)
                    gameWorld.damageCastle(((Network.DamageCastle) object).owner, ((Network.DamageCastle) object).damage);
                if (object instanceof Network.CastleDestroyed) {
                    gameWorld.endGame(((Network.CastleDestroyed) object).owner);
                }


            }
        });

        client.start();

    }


    public void connect(String IP) {
        try {
            client.connect(5000, IP, Network.port, Network.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        client.close();
    }

    public static void dispose() {
        client.close();
        gameWorld.dispose();
    }

    public Object[] findServers() {
        Object[] array = client.discoverHosts(Network.port, 600).toArray();
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].toString().substring(1);
        }
        return array;

    }


    public static void createUnit(int line, int type, int owner) {
        Network.CreateUnit createUnit = new Network.CreateUnit();
        State state = new State();
        state.line = line;
        state.type = type;
        state.owner = owner;
        createUnit.state = state;
        client.sendTCP(createUnit);
    }


    public static void damageCastle(int owner, int damage) {
        Network.DamageCastle damageCastle = new Network.DamageCastle();
        damageCastle.owner = owner;
        damageCastle.damage = damage;
        client.sendTCP(damageCastle);
    }

    public static int getID() {
        return client.getID();
    }

    public static LocalGameWorld getGameWorld() {
        return gameWorld;
    }


    public static void notifyCastleDestroyed() {
        Network.CastleDestroyed castleDestroyed = new Network.CastleDestroyed();
        castleDestroyed.owner = getID();
        client.sendTCP(castleDestroyed);
    }


}
