package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public enum BulletType {
    BULLET;

    public String title() {
        return Messages.get(this, name() + ".title");
    }
}
