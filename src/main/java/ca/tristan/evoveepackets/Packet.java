package ca.tristan.evoveepackets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Packet {

    public ISession session;

    public Packet(ISession session) {
        this.session = session;
    }

    public String getPacketType() {
        String str = getClass().getSimpleName();
        if(getClass().getSimpleName().contains("SPacket")) {
            str = str.replace("SPacket", "");
        }
        if(getClass().getSimpleName().contains("CPacket")) {
            str = str.replace("CPacket", "");
        }
        return str;
    }

    public abstract String write(ObjectMapper objectMapper) throws JsonProcessingException;

    public abstract void read(JsonNode jsonNode, ObjectMapper objectMapper) throws JsonProcessingException;

}
