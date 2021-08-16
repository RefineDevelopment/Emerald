package io.github.zowpy.emerald.shared.server;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: Emerald
 */

@Getter @Setter
public class EmeraldServer {

    private UUID uuid;
    private String name, ip;
    private int port, maxPlayers;
    private ServerStatus status;
    private EmeraldGroup group;

    private double tps;
    private List<UUID> onlinePlayers, whitelistedPlayers;

    public EmeraldServer(UUID uuid) {
        this.uuid = uuid;
    }

    public EmeraldServer(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("name").getAsString();
        this.ip = object.get("ip").getAsString();
        this.port = object.get("port").getAsInt();
       // this.onlinePlayers = object.get("onlinePlayers").getAsInt();
        this.maxPlayers = object.get("maxPlayers").getAsInt();
        this.status = ServerStatus.valueOf(object.get("status").getAsString().toUpperCase());
    }
}
