package me.zowpy.emerald.shared.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

@Getter
@RequiredArgsConstructor
public class EmeraldGroup {

    private final String name;

    @Setter
    private List<EmeraldServer> servers = new ArrayList<>();

}
