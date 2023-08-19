package ca.tristan.twopack.packets;

import ca.tristan.twopack.ISession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;

public abstract class VarPacket extends Packet {

    public VarPacket(ISession session) {
        super(session);
    }

    public abstract String getPacketType();

    public abstract void write(PrintWriter printWriter, ObjectMapper objectMapper) throws JsonProcessingException;

}
