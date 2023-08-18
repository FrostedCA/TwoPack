package ca.tristan.evoveepackets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.util.HashMap;

public class PacketsManager {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Packet[] packets;
    private final HashMap<PType, PObject> standbyPackets = new HashMap<>();
    private final PrintWriter printWriter;
    public final int MAX_BUFFER_SIZE = 4096;

    public PacketsManager(Packet[] packets, PrintWriter printWriter) {
        this.packets = packets;
        this.printWriter = printWriter;
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

    public void writePacket(Packet packet) {
        try {
            getPrintWriter().println(packet.write(objectMapper));
            getPrintWriter().flush();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet[] getPackets() {
        return packets;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public HashMap<PType, PObject> getStandbyPackets() {
        return standbyPackets;
    }


}