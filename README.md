# EvoveePackets
Small packet library made for Evovee. You can use and modify it as you wish. It uses Json so you will need to have json dependencies (Jackson Core).

Example client-side child packet class:
```
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
