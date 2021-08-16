package io.github.zowpy.emerald.utils.menu;

import lombok.Getter;
import org.bukkit.Sound;

public enum ButtonSound {

    CLICK(Sound.CLICK),
    SUCCESS(Sound.SUCCESSFUL_HIT),
    FAIL(Sound.DIG_GRASS);

    @Getter
    private Sound sound;

    ButtonSound(Sound sound) {
        this.sound = sound;
    }
}
