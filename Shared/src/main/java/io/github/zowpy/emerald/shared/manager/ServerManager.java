package io.github.zowpy.emerald.shared.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.zowpy.emerald.shared.SharedEmerald;
import io.github.zowpy.emerald.shared.server.EmeraldServer;
import io.github.zowpy.emerald.shared.server.ServerProperties;
import io.github.zowpy.emerald.shared.server.ServerStatus;
import io.github.zowpy.emerald.shared.util.TPSUtility;
import lombok.Getter;
import io.github.zowpy.emerald.shared.server.EmeraldGroup;
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


    /**
     * Updates all the servers from redis cache
     */

    public void updateServers() {
        Jedis jedis = null;
        try {

            jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }

            for (String key : jedis.keys("server*")) {
                if (key.startsWith("server-")) {
                    UUID uuid = UUID.fromString(key.replace("server-", ""));
                    if (jedis.hget(key, "status").equalsIgnoreCase(ServerStatus.OFFLINE.name()) && getByUUID(uuid) != null) {
                        return;
                    }

                    Map<String, String> data = jedis.hgetAll(key);


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
                    List<UUID> onlinePlayers = (List<UUID>) SharedEmerald.GSON.fromJson(data.get("onlinePlayers"), List.class);
                    List<UUID> whitelistedPlayers = (List<UUID>) SharedEmerald.GSON.fromJson(data.get("whitelistedPlayers"), List.class);
                    server.setOnlinePlayers(onlinePlayers);
                    server.setWhitelistedPlayers(whitelistedPlayers);
                    server.setMaxPlayers(Integer.parseInt(data.get("maxPlayers")));
                    server.setGroup(emerald.getGroupManager().getByName(data.get("group")));
                    server.setTps(TPSUtility.round(Double.parseDouble(data.get("tps"))));

                    EmeraldGroup group = emerald.getGroupManager().getByName(data.get("group"));

                    if (!group.getServers().contains(server)) {
                        group.getServers().add(server);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
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
        List<UUID> online = new ArrayList<>();
        List<UUID> whitelist = new ArrayList<>();
        for (JsonElement e : object.get("onlinePlayers").getAsJsonArray()) {
            online.add(UUID.fromString(e.getAsString()));
        }

        for (JsonElement e : object.get("whitelistedPlayers").getAsJsonArray()) {
            whitelist.add(UUID.fromString(e.getAsString()));
        }
        server.setOnlinePlayers(online);
        server.setWhitelistedPlayers(whitelist);
        server.setMaxPlayers(object.get("maxPlayer").getAsInt());
        server.setGroup(emerald.getGroupManager().getByName(object.get("group").getAsString()));
        server.setTps(object.get("tps").getAsDouble());

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
        server.setGroup(serverProperties.getGroup());
        server.setTps(serverProperties.getTps());


        updateServer(server);
    }

    /**
     * Update a specific server to redis cache
     *
     * @param server server to update
     */

    public void updateServer(EmeraldServer server) {
        Jedis jedis = null;
        try {
            Map<String, String> data = new HashMap<>();
            data.put("name", server.getName());
            data.put("ip", server.getIp());
            data.put("port", server.getPort() + "");
            data.put("status", server.getStatus().name());
            data.put("maxPlayers", server.getMaxPlayers() + "");
            data.put("onlinePlayers", SharedEmerald.GSON.toJson(server.getOnlinePlayers()));
            data.put("whitelistedPlayers", SharedEmerald.GSON.toJson(server.getWhitelistedPlayers()));
            data.put("group", server.getGroup().getName());
            data.put("tps", server.getTps() + "");

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
        Jedis jedis = null;
        try {

            jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

            Map<String, String> data = new HashMap<>();
            data.put("name", emerald.getServerProperties().getName());
            data.put("ip", emerald.getServerProperties().getIp());
            data.put("port", emerald.getServerProperties().getPort() + "");
            data.put("status", emerald.getServerProperties().getServerStatus().name());
            data.put("onlinePlayers", SharedEmerald.GSON.toJson(emerald.getServerProperties().getOnlinePlayers()));
            data.put("whitelistedPlayers", SharedEmerald.GSON.toJson(emerald.getServerProperties().getWhitelistedPlayers()));
            data.put("maxPlayers", emerald.getServerProperties().getMaxPlayers() + "");
            data.put("group", emerald.getServerProperties().getGroup().getName());
            data.put("tps", emerald.getServerProperties().getTps() + "");

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }

            jedis.hset("server-" + emerald.getUuid().toString(), data);

            updateServers();

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * Saves the current server to redis cache
     */

    public void saveServer() {
        Jedis jedis = null;
        try {

            jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

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
            data.put("onlinePlayers", SharedEmerald.GSON.toJson(server.getOnlinePlayers()));
            data.put("whitelistedPlayers", SharedEmerald.GSON.toJson(server.getWhitelistedPlayers()));
            data.put("maxPlayers", server.getMaxPlayers() + "");
            data.put("group", server.getGroup().getName());
            data.put("tps", server.getTps() + "");

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }

            jedis.hset("server-" + server.getUuid().toString(), data);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * Sets a server offline
     *
     * @param server server to set offline
     */

    public void setOffline(EmeraldServer server) {
        Jedis jedis = null;
        try {

            jedis = emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

            server.setStatus(ServerStatus.OFFLINE);

            if (emerald.getJedisAPI().getJedisHandler().getCredentials().isAuth()) {
                jedis.auth(emerald.getJedisAPI().getJedisHandler().getCredentials().getPassword());
            }

            Map<String, String> data = jedis.hgetAll("server-" + server.getUuid().toString());
            data.put("status", ServerStatus.OFFLINE.name());
            jedis.hset("server-" + server.getUuid().toString(), data);

            JsonObject object = new JsonObject();
            object.addProperty("name", server.getName());

            jedis.publish(emerald.getJedisAPI().getCredentials().getChannel(), "shutdown###" + object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
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

    /**
     * Returns a server matching the ip
     *
     * @param ip ip of the server
     * @return {@link EmeraldServer}
     */

    public EmeraldServer getByIp(String ip) {
        return emeraldServers.stream()
                .filter(emeraldServer -> emeraldServer.getIp().equalsIgnoreCase(ip)).findFirst().orElse(null);
    }

    /**
     * Returns a server matching the ip and port
     *
     * @param ip ip of the server
     * @param port port of the server
     * @return {@link EmeraldServer}
     */

    public EmeraldServer getByConnection(String ip, int port) {
        return emeraldServers.stream()
                .filter(emeraldServer -> emeraldServer.getIp().equalsIgnoreCase(ip) && emeraldServer.getPort() == port).findFirst().orElse(null);
    }

    /**
     * Returns a server containing the player
     *
     * @param uuid uuid of the player
     * @return {@link EmeraldServer}
     */

    public EmeraldServer getByPlayer(UUID uuid) {
        return emeraldServers.stream()
                .filter(emeraldServer -> emeraldServer.getOnlinePlayers().contains(uuid)).findFirst().orElse(null);
    }

}
