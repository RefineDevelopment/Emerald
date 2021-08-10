package me.zowpy.emerald.server;

import lombok.Data;
import me.zowpy.emerald.shared.server.ServerStatus;

import java.util.UUID;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: Emerald
 */

@Data
public class ServerProperties {

    private UUID uuid;
    private String name, ip;
    private int port, onlinePlayers, maxPlayers;
    private ServerStatus serverStatus;
}
