package ca.tristan.evoveepackets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Packet {

    public EvoSession session;

    public Packet(EvoSession session) {
        this.session = session;
    }

    public abstract PType getPacketType();

    public abstract String write(ObjectMapper objectMapper) throws JsonProcessingException;

    public abstract void read(JsonNode jsonNode);

}
