<div align="center">

  ## Emerald | Server Manager API

</small></i>

  [![Discord.js](https://img.shields.io/badge/Redis-in--memory%20data%20structure-red?style=flat-square)](https://github.com/redis/redis)

</div>

**Emerald** is an advanced redis-based server manager api. Originally developed privately for Refine Development by Zowpy, is now open sourced for your use.
Please be sure to report any issues in the issues tab and don't forget to star!

## Developed By
Refine Development & Zowpy

## Contact
- [Discord](https://dsc.gg/refine)
- refinedevelopment@gmail.com
- [Twitter](https://twitter.com/RefineDev)

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

## Usage:

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
  
# EmeraldServer:
  
  EmeraldServer is the server's object
  with this you can get the following:
  
    1. UUID
    2. Name
    3. IP & Port
    4. MaxPlayers
    5. ServerStatus
    6. TPS
    7. OnlinePlayers & WhitelistedPlayers (both are lists of uuids)

