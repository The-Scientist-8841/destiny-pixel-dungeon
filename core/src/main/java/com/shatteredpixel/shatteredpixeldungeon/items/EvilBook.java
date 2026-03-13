/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class EvilBook extends Item {

    public static final String AC_RESET = "RESET";

    {
        image = ItemSpriteSheet.EVIL_BOOK_INACTIVE;
        unique = true;
        bones = false;
        identify();
    }

    public int[] resets = {0, 0, 0, 0, 0, //Sewers
                            0, 0, 0, 0, 0, //Dungeon
                            0, 0, 0, 0, 0, //Caves
                            0, 0, 0, 0, 0, //City
                            0, 0, 0, 0, 0}; //Halls

    public static boolean can_reset = true;
    public static boolean active = false;
    public static int[] quest_depths = {0, 0, 0, 0, 0};

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        if (can_reset) actions.add(AC_RESET);
        actions.remove(AC_DROP);
        actions.remove(AC_THROW);
        return actions;
    }

    @Override
    public void execute( final Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_RESET )) {
            switch (Dungeon.depth) {
                case 2: case 3: case 4:
                    for (Mob m: Dungeon.level.mobs) {
                        if (m instanceof Ghost) {
                            quest_depths[0] = Dungeon.depth;
                            Ghost.Quest.reset();
                        }
                    }
                case 7: case 8: case 9:
                    for (Mob m: Dungeon.level.mobs) {
                        if (m instanceof Wandmaker) {
                            quest_depths[1] = Dungeon.depth;
                            Wandmaker.Quest.reset();
                        }
                    }
                case 12: case 13: case 14:
                    for (Mob m: Dungeon.level.mobs) {
                        if (m instanceof Blacksmith) {
                            quest_depths[2] = Dungeon.depth;
                            Blacksmith.Quest.reset();
                        }
                    }
                case 17: case 18: case 19:
                    for (Mob m: Dungeon.level.mobs) {
                        if (m instanceof Imp) {
                            quest_depths[3] = Dungeon.depth;
                            Imp.Quest.reset();
                        }
                    }
            }
            InterlevelScene.mode = InterlevelScene.Mode.RESET;
            resets[Dungeon.depth - 1] += 1;
            Game.switchScene(InterlevelScene.class);
            Catalog.countUse(getClass());
            Sample.INSTANCE.play( Assets.Sounds.DEATH );
            GLog.n(Messages.get(this, "reset_message", resets[Dungeon.depth - 1]));
        }
    }

    @Override
    public String desc() {
        if (active)
            return Messages.get(this, "desc_active");
        else
            return super.desc();
    }

    public boolean isActive(){
        return active;
    }

    public void activate(){
        active = true;
        image = ItemSpriteSheet.EVIL_BOOK_ACTIVE;
    }

    public void deactivate(){
        active = false;
        image = ItemSpriteSheet.EVIL_BOOK_INACTIVE;
    }

    private static final Glowing RED = new Glowing( 0x550000 );

    @Override
    public Glowing glowing() {
        return isActive() ? RED : null;
    }

    private static final String ACTIVE = "active";
    private static final String RESETS = "resets";
    private static final String CAN_RESET = "can_reset";
    private static final String QUEST_DEPTHS = "quest_depths";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ACTIVE, active );
        bundle.put( RESETS, resets );
        bundle.put( CAN_RESET, can_reset );
        bundle.put( QUEST_DEPTHS, quest_depths );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        if (bundle.getBoolean( ACTIVE )) activate();
        resets = bundle.getIntArray(RESETS);
        can_reset = bundle.getBoolean(CAN_RESET);
        quest_depths = bundle.getIntArray(QUEST_DEPTHS);
    }

    @Override
    public int value() {
        return 999;
    }
}
