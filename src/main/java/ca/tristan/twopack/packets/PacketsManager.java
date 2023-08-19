package ca.tristan.twopack.packets;

import ca.tristan.twopack.ISession;
import ca.tristan.twopack.json.PObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class PacketsManager {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Packet[] packets;
    private final HashMap<String, PObject> standbyPackets = new HashMap<>();
    private final ISession iSession;
    private final PrintWriter printWriter;

    public PacketsManager(Packet[] packets, ISession iSession) throws IOException {
        this.packets = packets;
        this.iSession = iSession;
        this.printWriter = new PrintWriter(new OutputStreamWriter(this.iSession.getSslSocket().getOutputStream()));
    }

    public void listenForPackets() throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead = this.iSession.getSslSocket().getInputStream().read(buffer);
        if(bytesRead == -1) {
            iSession.closeSession();
            return;
        }
        String receivedData = new String(buffer, 0, bytesRead);
        try {
            JsonNode jsonNode = objectMapper.readTree(receivedData);
            iSession.log(jsonNode);
            for (Packet cPacket : packets) {
                if(!jsonNode.get("packetType").isNull() && cPacket.getPacketType().equals(jsonNode.get("packetType").asText())) {
                    cPacket.read(jsonNode, objectMapper);
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

    public void writePacket(VarPacket packet) {
        packet.write(printWriter, objectMapper);
    }

    public void writePacket(ClassPacket packet) {
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

    public HashMap<String, PObject> getStandbyPackets() {
        return standbyPackets;
    }

    public void addStandbyPObject(PObject pObject) {
        standbyPackets.put(pObject.getClass().getSimpleName(), pObject);
    }

    public PObject getStandbyPObject(String pType) {
        if(standbyPackets.containsKey(pType)) {
            PObject object = standbyPackets.get(pType);
            standbyPackets.remove(pType);
            return object;
        }
        return null;
    }

    public void clearStandbyPackets() {
        standbyPackets.clear();
    }

}
