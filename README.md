<div align="center">

  <img src="https://static.wikia.nocookie.net/minecraft_gamepedia/images/2/26/Emerald_JE3_BE3.png/revision/latest/scale-to-width-down/160?cb=20191229174220"></img>
  ## Emerald

</small></i>

  [![Redis](https://img.shields.io/badge/Redis-in--memory%20data%20structure-red?style=flat-square)](https://github.com/redis/redis)

</div>

**Emerald** is an advanced server manager, using redis. Originally Emerald was developed privately for Refine Development by Zowpy, is now open sourced for anyone to use.
If you find any issues, please report them in our <a href="https://github.com/RefineDevelopment/Emerald/issues">issues tab</a> and don't forget to star the repository!
Please be sure to report any issues in the issues tab and don't forget to star!

## The Emerald Team
+ [Zowpy](https://github.com/Zowpy) | Lead Developer
+ [Refine Development](https://github.com/RefineDevelopment) | Lead Developer
+ [Steamworks](https://github.com/steamworksmc) | Contributor

## Contact
- [Discord](https://dsc.gg/refine)
- [Mail](mailto:refinedevelopment@gmail.com)
- [Twitter](https://twitter.com/RefineDev)

## Installing
1. Download the jar from <a href="https://github.com/RefineDevelopment/Emerald/releases">releases</a>
2. Put the jar in all of your servers
3. Edit the configuration files.

## API

You can use our API by adding this dependency to your maven project.
```xml
<dependency>
  <groupId>me.zowpy</groupId>
  <artifactId>emerald</artifactId>
  <version>1.0-SNAPSHOT</version>
  <scope>compile</scope>
</dependency>
```

## Usage:

```java
ServerManager#getByUUID(UUID serverUuid) : Returns an CompletableFuture<EmeraldServer>.
ServerManager#getByName(String serverName) : Returns an CompletableFuture<EmeraldServer>.
ServerManager#getByConnection(String ipAddress, int serverPort) : Returns an CompletableFuture<EmeraldServer>.
```
  
# EmeraldServer:

An instance of an EmeraldServer contains the following elements in order.
+ Server Uuid
+ Server Name
+ Server Ip + Port
+ Maximum Players
+ Server Status
+ Ticks Per Second
+ Online Player Count & Whitelisted Player Count
