package ca.tristan.twopack.packets;

import ca.tristan.twopack.ISession;

public abstract class ClassPacket extends Packet {

    public ClassPacket(ISession session) {
        super(session);
    }

}
