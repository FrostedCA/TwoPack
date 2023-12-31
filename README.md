# Summary
- <a href="#example-client-side-classpacket-child">Client Side Packet Example</a>
- <a href="#example-server-side-classpacket-child">Server Side Packet Example</a>
- <a href="#register-object-example">PObject Example</a>
- <a href="#how-to-setup-packet-manager-client-and-server-side">How to setup packet manager client and server side</a>
- <a href="#example-varpacket">VarPacket example</a>
- <a href="#download">Download</a>

# TwoPack
Small packet library made for Java SSLSockets. You can use and modify it as you wish. It uses Json so you will need to have json dependencies (Jackson Core).
For more details contact me on discord: **landryman**.
Version 2.00+ stable.

## Example client-side ClassPacket child
### With Update v2.11+
```java
public class CPacketRegister extends ClassPacket {

    public CPacketRegister(ISession session) {
        super(session);
    }

    public CPacketRegister(ISession session, RegisterObject registerObject) {
        super(session);
        this.pObject = registerObject;
    }

    @Override
    public void read(JsonNode jsonNode, ObjectMapper objectMapper) throws JsonProcessingException {
        if(!jsonNode.isNull() && !jsonNode.isEmpty()) {
            RegisterObject registerObject = objectMapper.treeToValue(jsonNode, RegisterObject.class);
            App.getSession().getPacketsManager().writePacket(new CPacketLogin(App.getSession(), registerObject));
        }
    }
}
```

## Example server-side ClassPacket child
### With Update v2.11+
```java
public class SPacketRegister extends ClassPacket {

    public SPacketRegister(ISession session) {
        super(session);
    }

    public SPacketRegister(ISession session, RegisterObject registerObject) {
        super(session);
        this.pObject = registerObject;
    }

    @Override
    public void read(JsonNode jsonNode, ObjectMapper objectMapper) throws JsonProcessingException {
        boolean result = false;

        RegisterObject registerObject = objectMapper.treeToValue(jsonNode, RegisterObject.class);

        boolean valid = InputValidator.validatePassword(registerObject.getPassword(), registerObject.getConfPassword());
        if(valid) {
            result = DBQueries.registerAccount(registerObject);
        }
        registerObject.setResult(result);
        ((ClientSession) session).getPacketsManager().writePacket(new SPacketRegister(session, registerObject));
    }
}
```
## Register Object Example
```java
public class RegisterObject extends PObject {

    private String email;
    private String password;
    private String confPassword;
    private String firstName;
    private String lastName;

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
...
}
```
## How to setup packet manager client and server side
Simply add this code right after where you are starting the handshake between the client and server.
```java
try{
    this.getSslSocket().startHandshake();
    this.packetsManager = new PacketsManager(this.packetList, this);
    this.packetsManager.writePacket(new CPacketHandshake(this));
    while (!this.getSslSocket().isClosed()) {
        this.packetsManager.listenForPackets();
    }
} catch(IOException e) {
// log/code here
}
```

## Example VarPacket
### With Update v2.15+
VarPacket or Variable Packet is a new way of creating your packets! As the name implies, you can create a packet inside a variable. Old packets have received there new class ClassPacket.java. The old Packet.java is now protected. Packets now also implements an interface, more onto this on future releases.
```java
public VarPacket varPacket = new VarPacket(session) {
        @Override
        public String getPacketType() {
            return "Example";
        }

        @Override
        public void write(PrintWriter printWriter, ObjectMapper objectMapper) throws JsonProcessingException {
            /**
             * Process your data...
             */

            // Create your object
            this.pObject = new ExampleObject(); // -> (public class ExampleObject extends PObject)
            this.pObject.setPacketType(getPacketType()); // -> Set the packet type.
            printWriter.println(objectMapper.writeValueAsString(pObject)); // -> Write the packet.
            printWriter.flush();
        }

        @Override
        public void read(JsonNode jsonNode, ObjectMapper objectMapper) throws JsonProcessingException {
            /**
             * Process your data... (Same as ClassPacket see Summary inside ReadMe)
             * You can also access your 'session' this.session and cast it to your actual session class if needed (server side especially)
             */
        }
};
```

## Download
[![](https://www.jitpack.io/v/FrostedCA/TwoPack.svg)](https://www.jitpack.io/#FrostedCA/TwoPack)
### Gradle
```gradle
allprojects {
   repositories {
      ...
      maven { url 'https://www.jitpack.io' }
   }
}

dependencies {
   implementation 'com.github.FrostedCA:TwoPack:VERSION'
}
```
### Maven
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://www.jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.FrostedCA</groupId>
  <artifactId>TwoPack</artifactId>
  <version>Tag</version>
</dependency>
```
