package com.castlefight.server;


import com.castlefight.gameobjects.State;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

public class Network {
    static public int port = 25565;
    static public String localhost = "127.0.0.1";

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(InitializeGame.class);
        kryo.register(CreateUnit.class);
        kryo.register(State.class);
        kryo.register(Login.class);
        kryo.register(Register.class);
        kryo.register(RegistrationRequired.class);
        kryo.register(ClientGameMessage.class);
        kryo.register(ArrayList.class);
        kryo.register(DamageCastle.class);
        kryo.register(CastleDestroyed.class);



    }

    static public class Login {
        public String name;
    }

    static public class Register {
        public String name;
    }

    static public class RegistrationRequired{

    }


    static public class ClientGameMessage{
    }


    static public class CreateUnit extends Network.ClientGameMessage {
        public State state;
        public CreateUnit(){
            state = new State();
        }
    }

    static public class DamageCastle extends ClientGameMessage {
        public int owner;
        public int damage;
        public DamageCastle(){
        }
    }

    static public class CastleDestroyed extends ClientGameMessage {
        public int owner;
        public CastleDestroyed(){}
    }

    static public class InitializeGame{
        InitializeGame(){}
    }


}
