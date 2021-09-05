# Emerald

Emerald is a Redis based ServerAPI.

## Installing:
1. Download the jar from releases
2. Put the jar in all of your servers
3. Modify the server name & redis credentials

## API:

add the dependency to your maven project:
```xml
<dependency>
  <groupId>me.zowpy</groupId>
  <artifactId>emerald</artifactId>
  <version>1.0-SNAPSHOT</version>
  <scope>compile</scope>
</dependency>
```

# Usage:

getting a server by uuid:
```Java
EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getByUUID(youruuid);
```
this returns CompletableFuture<EmeraldServer>

getting a server by name:
```Java
EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getByName(yourServerName);
```
this returns CompletableFuture<EmeraldServer>
  
You can get a server by ip & port
```Java
EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getByConnection(ip, port);
``` 
this returns CompletableFuture<EmeraldServer> 

EmeraldServer:
  EmeraldServer is the server's object
  with this you can get the following:
    1. UUID
    2. Name
    3. IP & Port
    4. MaxPlayers
    5. ServerStatus
    6. TPS
    7. OnlinePlayer & WhitelistedPlayer (both are lists of uuids)
