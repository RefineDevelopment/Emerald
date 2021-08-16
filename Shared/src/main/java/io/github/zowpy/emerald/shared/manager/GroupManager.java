package io.github.zowpy.emerald.shared.manager;

import lombok.Getter;
import io.github.zowpy.emerald.shared.server.EmeraldGroup;

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

@Getter
public class GroupManager {

    private final List<EmeraldGroup> groups = new ArrayList<>();

    /**
     *
     * @param name name of the group
     * @return {@link EmeraldGroup}
     */

    public EmeraldGroup getByName(String name) {
        return groups.stream().filter(emeraldGroup -> emeraldGroup.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }


}
