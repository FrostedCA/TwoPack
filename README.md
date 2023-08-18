# Summary
- <a href="#example-client-side-child-packet-class">Client Side Packet Example</a>
- <a href="#example-server-side-child-packet-class">Server Side Packet Example</a>
- <a href="#download">Download</a>

# TwoPack
Small packet library made for Evovee. You can use and modify it as you wish. It uses Json so you will need to have json dependencies (Jackson Core).
For more details contact me on discord: **landryman**.
Version 2.00+ stable.

## Example client-side child packet class
### With Update v2.11
```java
public class CPacketRegister extends Packet {

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

## Example server-side child packet class
### With Update v2.11
```java
public class SPacketRegister extends Packet {

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
        if(result) {
            ((ClientSession) session).getPacketsManager().writePacket(new SPacketRegister(session, registerObject));
        }
    }
}
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
