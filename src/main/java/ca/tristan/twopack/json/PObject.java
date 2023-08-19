package ca.tristan.twopack.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PObject {

    private String packetType;
    private boolean result;

    @JsonProperty("packetType")
    public String getPacketType() {
        return packetType;
    }

    public void setPacketType(String packetType) {
        this.packetType = packetType;
    }

    @JsonProperty("result")
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
