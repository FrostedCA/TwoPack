package ca.tristan.twopack.packets;

import ca.tristan.twopack.ISession;
import ca.tristan.twopack.json.PObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.iSession.getSslSocket().getInputStream()));

        String receivedData;
        while ((receivedData = reader.readLine()) != null) {
            try {
                JsonNode jsonNode = objectMapper.readTree(receivedData);
                iSession.log(jsonNode);

                for (Packet cPacket : packets) {
                    if(!jsonNode.hasNonNull("packetType") && cPacket.getPacketType().equals(jsonNode.get("packetType").asText())) {
                        cPacket.read(jsonNode, objectMapper);
                        break;
                    }
                }
            } catch (JsonProcessingException e) {
                System.out.println("Invalid JSON received: " + receivedData);
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
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
        try {
            packet.write(printWriter, objectMapper);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
