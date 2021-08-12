package me.zowpy.emerald.shared.manager;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonObject;
import lombok.Getter;
import me.zowpy.emerald.shared.SharedEmerald;
import me.zowpy.emerald.shared.server.EmeraldServer;
import me.zowpy.emerald.shared.server.ServerProperties;
import me.zowpy.emerald.shared.server.ServerStatus;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: Emerald
 */

@Getter
public class ServerManager {

    private final SharedEmerald emerald;

    public ServerManager(SharedEmerald emerald) {
        this.emerald = emerald;
    }

    private final List<EmeraldServer> emeraldServers = new ArrayList<>();
    private final Table<UUID, String, String> emeraldServerData = HashBasedTable.create();


    /**
     * Updates all the servers from redis cache
     */

    public void updateServers() {
        try {
            Jedis jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

           /* for (EmeraldServer server : emeraldServers) {
                if (jedis.exists("server-" + server.getUuid().toString())) {
                    Map<String, String> data = jedis.hgetAll("server-" + server.getUuid().toString());
                    server.setName(data.get("name"));
                    server.setIp(data.get("ip"));
                    server.setPort(Integer.parseInt(data.get("port")));
                    server.setStatus(ServerStatus.valueOf(data.get("status")));
                    server.setOnlinePlayers(Integer.parseInt(data.get("onlinePlayers")));
                    server.setMaxPlayers(Integer.parseInt(data.get("maxPlayers")));

                    data.forEach((s, s2) -> emeraldServerData.put(server.getUuid(), s, s2));
                }
            }  */

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }

            for (String key : jedis.keys("server*")) {
                if (key.startsWith("server-")) {
                    Map<String, String> data = jedis.hgetAll(key);

                    UUID uuid = UUID.fromString(key.replace("server-", ""));

                    EmeraldServer server;

                    if (getByUUID(uuid) == null) {
                        server = new EmeraldServer(uuid);
                        emeraldServers.add(server);
                    } else {
                        server = getByUUID(uuid);
                    }


                    server.setName(data.get("name"));
                    server.setIp(data.get("ip"));
                    server.setPort(Integer.parseInt(data.get("port")));
                    server.setStatus(ServerStatus.valueOf(data.get("status")));
                    server.setOnlinePlayers(Integer.parseInt(data.get("onlinePlayers")));
                    server.setMaxPlayers(Integer.parseInt(data.get("maxPlayers")));

                    data.forEach((s, s2) -> emeraldServerData.put(uuid, s, s2));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Update a specific server
     *
     * @param server server to update
     * @param object data used to update
     */

    public void updateServer(EmeraldServer server, JsonObject object) {
        server.setName(object.get("name").getAsString());
        server.setIp(object.get("ip").getAsString());
        server.setPort(object.get("port").getAsInt());
        server.setStatus(ServerStatus.valueOf(object.get("status").getAsString().toUpperCase()));
        server.setOnlinePlayers(object.get("onlinePlayers").getAsInt());
        server.setMaxPlayers(object.get("maxPlayer").getAsInt());


        updateServer(server);
    }

    /**
     * Update a specific server
     *
     * @param server server to update
     * @param serverProperties data used to update
     */

    public void updateServer(EmeraldServer server, ServerProperties serverProperties) {
        server.setName(serverProperties.getName());
        server.setIp(serverProperties.getIp());
        server.setPort(serverProperties.getPort());
        server.setStatus(serverProperties.getServerStatus());
        server.setOnlinePlayers(serverProperties.getOnlinePlayers());
        server.setMaxPlayers(serverProperties.getMaxPlayers());


        updateServer(server);
    }

    /**
     * Update a specific server to redis cache
     *
     * @param server server to update
     */

    public void updateServer(EmeraldServer server) {
        try {
            Jedis jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

            Map<String, String> data = new HashMap<>();
            data.put("name", server.getName());
            data.put("ip", server.getIp());
            data.put("port", server.getPort() + "");
            data.put("status", server.getStatus().name());
            data.put("maxPlayers", server.getMaxPlayers() + "");
            data.put("onlinePlayers", server.getOnlinePlayers() + "");

            data.forEach((s, s2) -> emeraldServerData.put(server.getUuid(), s, s2));

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }
            jedis.hset("server-" + server.getUuid().toString(), data);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a server to redis cache
     */

    public void createServer() {
        try {
            Jedis jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

            Map<String, String> data = new HashMap<>();
            data.put("name", emerald.getServerProperties().getName());
            data.put("ip", emerald.getServerProperties().getIp());
            data.put("port", emerald.getServerProperties().getPort() + "");
            data.put("status", emerald.getServerProperties().getServerStatus().name());
            data.put("onlinePlayers", emerald.getServerProperties().getOnlinePlayers() + "");
            data.put("maxPlayers", emerald.getServerProperties().getMaxPlayers() + "");

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }

            jedis.hset("server-" + emerald.getUuid().toString(), data);

            emerald.getJedisAPI().getJedisHandler().write("updateservers###a");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current server to redis cache
     */

    public void saveServer() {
        try {
            Jedis jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

            EmeraldServer server = getByUUID(emerald.getUuid());

            if (server == null) {
                createServer();
                return;
            }

            Map<String, String> data = new HashMap<>();
            data.put("name", server.getName());
            data.put("ip", server.getIp());
            data.put("port", server.getPort() + "");
            data.put("status", server.getStatus().name());
            data.put("onlinePlayers", server.getOnlinePlayers() + "");
            data.put("maxPlayers", server.getMaxPlayers() + "");

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }

            jedis.hset("server-" + server.getUuid().toString(), data);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOffline(EmeraldServer server) {
        server.setStatus(ServerStatus.OFFLINE);

        try {
            Jedis jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }

            jedis.hset("server-" + server.getUuid().toString(), "status", ServerStatus.OFFLINE.name());

            emeraldServerData.put(server.getUuid(), "status", ServerStatus.OFFLINE.name());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns a server matching the name
     *
     * @param name name of the server
     * @return {@link EmeraldServer}
     */

    public EmeraldServer getByName(String name) {

        return emeraldServers.stream()
                .filter(emeraldServer -> emeraldServer.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Returns a server matching the uuid
     *
     * @param uuid uuid of the server
     * @return {@link EmeraldServer}
     */

    public EmeraldServer getByUUID(UUID uuid) {
        return emeraldServers.stream()
                .filter(emeraldServer -> emeraldServer.getUuid().equals(uuid)).findFirst().orElse(null);
    }

}
