# EvoveePackets
Small packet library made for Evovee. You can use and modify it as you wish. It uses Json so you will need to have json dependencies (Jackson Core).
For more details contact me on discord: **landryman**.
Version 2.00+ stable.

## Download
[![](https://www.jitpack.io/v/FrostedCA/EvoveePackets.svg)](https://www.jitpack.io/#FrostedCA/EvoveePackets)

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
    public String write(ObjectMapper objectMapper) throws JsonProcessingException {
        registerObj.setPacketType(getPacketType().name());
        return objectMapper.writeValueAsString(registerObj);
    }

    @Override
    public void read(JsonNode jsonNode, ObjectMapper objectMapper) throws JsonProcessingException {
        if(jsonNode.get("email") != null && !jsonNode.get("email").isNull()) {
            App.getSession().getPacketsManager().writePacket(new CPacketLogin(App.getSession(), jsonNode.get("email").asText(), jsonNode.get("password").asText()));
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
    public String write(ObjectMapper objectMapper) throws JsonProcessingException {
        RegisterObject registerObject = new RegisterObject();
        registerObject.setResult(result);
        registerObject.setPacketType(getPacketType().name());
        return objectMapper.writeValueAsString(registerObject);
    }

    @Override
    public void read(JsonNode jsonNode, ObjectMapper objectMapper) throws JsonProcessingException {
        boolean result = false;

        RegisterObject registerObject = objectMapper.treeToValue(jsonNode, RegisterObject.class);

        boolean valid = InputValidator.validatePassword(registerObject.getPassword(), registerObject.getConfPassword());
        if(valid) {
            result = DBQueries.registerAccount(registerObject);
        }
        if(result) {
            ((ClientSession) session).getPacketsManager().writePacket(new SPacketRegister(session, registerObject));
        }
    }
}
```
