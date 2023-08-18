package ca.tristan.twopack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Packet {

    public ISession session;
    public PObject pObject;

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

    public String write(ObjectMapper objectMapper) throws JsonProcessingException {
        pObject.setPacketType(getPacketType());
        return objectMapper.writeValueAsString(pObject);
    }

    public abstract void read(JsonNode jsonNode, ObjectMapper objectMapper) throws JsonProcessingException;

}
