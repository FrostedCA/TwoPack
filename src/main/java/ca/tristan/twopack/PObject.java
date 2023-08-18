package ca.tristan.twopack;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PObject {

    private String packetType;

    @JsonProperty("packetType")
    public String getPacketType() {
        return packetType;
    }

    public void setPacketType(String packetType) {
        this.packetType = packetType;
    }

}
