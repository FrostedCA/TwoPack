package ca.tristan.evoveepackets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Packet {

    public Object session;

    public Packet(Object session) {
        this.session = session;
    }

    public abstract PacketType getPacketType();

    public abstract String write(ObjectMapper objectMapper) throws JsonProcessingException;

    public abstract void read(JsonNode jsonNode);

}
