# EvoveePackets
Small packet library made for Evovee. You can use and modify it as you wish. It uses Json so you will need to have json dependencies (Jackson Core).

## Download
[![](https://www.jitpack.io/v/FrostedCA/EvoveePackets.svg)](https://www.jitpack.io/#FrostedCA/EvoveePackets)
### JitPack is broken. The project is available with JitPack on version 1.8

Example client-side child packet class:
```java
public class CPacketRegister extends Packet {

    private RegisterObject registerObj;

    public CPacketRegister(Object session) {
        super(session);
    }

    public CPacketRegister(Object session, RegisterObject registerObject) {
        super(session);
        this.registerObj = registerObject;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.Register;
    }

    @Override
    public String write(ObjectMapper objectMapper) throws JsonProcessingException {
        registerObj.setPacketType(getPacketType().name());
        return objectMapper.writeValueAsString(registerObj);
    }

    @Override
    public void read(JsonNode jsonNode) {
        if(jsonNode.get("result").asBoolean()) {
            App.getSession().getWindowOnScreen().setUserInterface(VDL.registerUI);
        }
    }

}
```

Example server-side child packet class:
```java
public class SPacketRegister extends Packet {

    private boolean result;

    public SPacketRegister(Object session) {
        super(session);
    }

    public SPacketRegister(Object session, boolean result) {
        super(session);
        this.result = result;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.Register;
    }

    @Override
    public String write(ObjectMapper objectMapper) throws JsonProcessingException {
        RegisterObject registerObject = new RegisterObject();
        registerObject.setResult(result);
        registerObject.setPacketType(getPacketType().name());
        return objectMapper.writeValueAsString(registerObject);
    }

    @Override
    public void read(JsonNode jsonNode) {
        boolean result = false;

        boolean valid = InputValidation.validatePasswords(jsonNode.get("password").asText(), jsonNode.get("confPassword").asText());
        if(valid) {
            result = DBQueries.registerAccount(jsonNode.get("email").asText(), jsonNode.get("password").asText());
        }
        ((ClientSession) session).getPacketsManager().writePacket(new SPacketRegister(session, result));
    }
}
```
