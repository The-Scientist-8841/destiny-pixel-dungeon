package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.mythical;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public abstract class MythicalScroll extends Scroll {
    {
        unique = true;

        talentFactor = 3f;

        icon = -1;
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        identify();
        return super.doPickUp(hero, pos);
    }

    @Override
    public boolean isIdentified() { return true; }

    @Override
    public int energyVal() { return 24 * quantity; }

    @Override
    public int value() { return 100 * quantity; }

    @Override
    public String name() { return Messages.get(this, "name"); };

    @Override
    public String desc() { return Messages.get(this, "desc"); }
}
