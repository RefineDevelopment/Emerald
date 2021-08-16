package io.github.zowpy.emerald.shared.server;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/13/2021
 * Project: Emerald
 */

@Getter @Setter
public class EmeraldGroup {

    private final String name;
    private List<EmeraldServer> servers;

    public EmeraldGroup(String name) {
        this.name = name;
        this.servers = new ArrayList<>();
    }
}
