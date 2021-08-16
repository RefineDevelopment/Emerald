package io.github.zowpy.emerald.shared.server;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/11/2021
 * Project: Emerald
 */

@Data
public class ServerProperties {

    private UUID uuid;
    private String name, ip;
    private int port, maxPlayers;
    private List<UUID> onlinePlayers, whitelistedPlayers;
    private ServerStatus serverStatus;
    private EmeraldGroup group;
    private double tps;
}
