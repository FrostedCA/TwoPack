package ca.tristan.evoveepackets;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.UUID;

public abstract class ISession extends Thread {

    private final UUID sessionUUID;
    private final SSLSocket sslSocket;

    public ISession(SSLSocket sslSocket) {
        this.sessionUUID = UUID.randomUUID();
        this.sslSocket = sslSocket;
    }

    public void closeSession() throws IOException {
        this.sslSocket.close();
        System.out.println("Client '" + sessionUUID + "' disconnected.");
    }

    public UUID getSessionUUID() {
        return sessionUUID;
    }

    public SSLSocket getSslSocket() {
        return sslSocket;
    }
}
