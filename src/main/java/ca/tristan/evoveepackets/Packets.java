package ca.tristan.evoveepackets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Packets {

    public ObjectMapper objectMapper = new ObjectMapper();
    private final Packet[] packets;
    private final Object session;

    public Packets(Packet[] packets, Object session) {
        this.packets = packets;
        this.session = session;
    }

    public void listenForPackets(String jsonString) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            for (Packet cPacket : packets) {
                if(cPacket.getPacketType().name().equals(jsonNode.get("packetType").asText())) {
                    cPacket.read(jsonNode);
                    return;
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void writePacketToClient(Packet packet) {}

    public void writePacketToServer(Packet packet) {}

    public Object getSession() {
        return session;
    }

    public Packet[] getPackets() {
        return packets;
    }
}
