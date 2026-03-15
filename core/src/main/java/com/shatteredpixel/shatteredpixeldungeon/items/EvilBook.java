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
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FlashBangBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ExoticCrystals;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class EvilBook extends Item {

    public static final String AC_RESET = "RESET";

    {
        image = ItemSpriteSheet.EVIL_BOOK_INACTIVE;
        unique = true;
        bones = false;
        identify();
    }

    public static int[] resets = {0, 0, 0, 0, 0, //Sewers
                            0, 0, 0, 0, 0, //Dungeon
                            0, 0, 0, 0, 0, //Caves
                            0, 0, 0, 0, 0, //City
                            0, 0, 0, 0, 0}; //Halls

    public static boolean can_reset = true;
    public static boolean active = false;
    public static int[] quest_depths = {0, 0, 0, 0, 0};

    private int lvlsToUpgrade = 4;

    public void levelUp() {
        lvlsToUpgrade -= 1;
        if (lvlsToUpgrade <= 0) {
            new ScrollOfUpgrade().collect();

            Flare flare = new Flare(6, 20).color(0xFF0000, true).show(Dungeon.hero.sprite, 3f);
            Sample.INSTANCE.play( Assets.Sounds.DEATH );
            GLog.n(Messages.get(this, "upgrade_message"));

            lvlsToUpgrade = 5;
        }
    }

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
//            switch (Dungeon.depth) {
//                case 2: case 3: case 4:
//                    for (Mob m: Dungeon.level.mobs) {
//                        if (m instanceof Ghost) {
//                            quest_depths[0] = Dungeon.depth;
//                            Ghost.Quest.reset();
//                        }
//                    }
//                case 7: case 8: case 9:
//                    for (Mob m: Dungeon.level.mobs) {
//                        if (m instanceof Wandmaker) {
//                            quest_depths[1] = Dungeon.depth;
//                            Wandmaker.Quest.reset();
//                        }
//                    }
//                case 12: case 13: case 14:
//                    for (Mob m: Dungeon.level.mobs) {
//                        if (m instanceof Blacksmith) {
//                            quest_depths[2] = Dungeon.depth;
//                            Blacksmith.Quest.reset();
//                        }
//                    }
//                case 17: case 18: case 19:
//                    for (Mob m: Dungeon.level.mobs) {
//                        if (m instanceof Imp) {
//                            quest_depths[3] = Dungeon.depth;
//                            Imp.Quest.reset();
//                        }
//                    }
//            }
            if (Dungeon.depth == quest_depths[0]) Ghost.Quest.reset();
            if (Dungeon.depth == quest_depths[1]) Wandmaker.Quest.reset();
            if (Dungeon.depth == quest_depths[2]) Blacksmith.Quest.reset();
            if (Dungeon.depth == quest_depths[3]) Imp.Quest.reset();

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

    public static void showFlareForBonusDrop( Visual vis ){
        if (vis == null || vis.parent == null) return;

        new Flare(6, 32).color(0xFF0000, true).show(vis, 4f);
    }

    public static class TriesToDropTracker extends CounterBuff {
        {
            revivePersists = true;
        }
    }

    public static Item genItemDrop(int level) {
        float roll = Random.Float();
        //60% chance - 4% per level. Starting from +15: 0%
        if (roll < (0.6f - 0.04f * level)) {
            return genLowValueItem();
            //30% chance + 2% per level. Starting from +15: 60%-2%*(lvl-15)
        } else if (roll < (0.9f - 0.02f * level)) {
            return genMidValueItem();
            //10% chance + 2% per level. Starting from +15: 40%+2%*(lvl-15)
        } else {
            return genHighValueItem();
        }
    }

    private static Item genLowValueItem(){
        switch (Random.Int(7)){
            case 0: default:
                Item i = new Gold().random();
                return i.quantity(i.quantity()/2);
            case 1:
                return Generator.randomUsingDefaults(Generator.Category.STONE);
            case 2:
                return Generator.randomUsingDefaults(Generator.Category.POTION);
            case 3:
                return Generator.randomUsingDefaults(Generator.Category.SCROLL);
            case 4:
                return new EnergyCrystal(Random.Int(2, 7));
            case 5:
                return new MysteryMeat();
            case 6:
                return new Bomb();
        }
    }

    private static Item genMidValueItem(){
        switch (Random.Int(10)){
            case 0: default:
                Item i = genLowValueItem();
                if (i instanceof Bomb){
                    return new Bomb.DoubleBomb();
                } else {
                    return i.quantity(i.quantity()*2);
                }
            case 1:
                i = Generator.randomUsingDefaults(Generator.Category.POTION);
                if (!(i instanceof ExoticPotion)) {
                    return Reflection.newInstance(ExoticPotion.regToExo.get(i.getClass()));
                } else {
                    return Reflection.newInstance(i.getClass());
                }
            case 2:
                i = Generator.randomUsingDefaults(Generator.Category.SCROLL);
                if (!(i instanceof ExoticScroll)){
                    return Reflection.newInstance(ExoticScroll.regToExo.get(i.getClass()));
                } else {
                    return Reflection.newInstance(i.getClass());
                }
            case 3:
                return Random.Int(2) == 0 ? new UnstableBrew() : new UnstableSpell();
            case 4:
                return new SmokeBomb();
            case 5:
                return new Honeypot();
            case 6:
                return new Food();
            case 7:
                return new Noisemaker();
            case 8:
                return new FlashBangBomb();
            case 9:
                return new Stylus();
        }
    }

    private static Item genHighValueItem(){
        switch (Random.Int(11)){
            case 0: default:
                Item i = genMidValueItem();
                if (i instanceof Bomb.DoubleBomb){
                    return new HolyBomb();
                } else {
                    return i.quantity(i.quantity()*2);
                }
            case 1:
                return new StoneOfEnchantment();
            case 2:
                return Random.Float() < ExoticCrystals.consumableExoticChance() ? new PotionOfDivineInspiration() : new PotionOfExperience();
            case 3:
                return Random.Float() < ExoticCrystals.consumableExoticChance() ? new ScrollOfMetamorphosis() : new ScrollOfTransmutation();
            case 4:
                return new Pasty();
            case 5:
                return new Firebomb();
            case 6:
                return new FrostBomb();
            case 7:
                return new ScrollOfUpgrade();
            case 8:
                return new GooBlob();
            case 9:
                return new MetalShard();
            case 10:
                return Generator.random(Generator.Category.ELIXIR);
        }
    }

    public static ArrayList<Item> tryForBonusDrop(Char target, int tries ) {
        int bonus = 5 - resets[Dungeon.depth - 1];

        if (bonus <= 0) return null;

        CounterBuff triesToDrop = target.buff(TriesToDropTracker.class);
        if (triesToDrop == null){
            triesToDrop = Buff.affect(target, TriesToDropTracker.class);
            triesToDrop.countUp( Random.NormalIntRange(0, 10) );
        }

        //now handle reward logic
        ArrayList<Item> drops = new ArrayList<>();

        triesToDrop.countDown(tries);
        while ( triesToDrop.count() <= 0 ){
            Item i;
            do {
                i = genItemDrop(bonus - 1);
            } while (Challenges.isItemBlocked(i));
            drops.add(i);
            triesToDrop.countUp( Random.NormalIntRange(0, 10) );
        }

        return drops;
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
    private static final String LEVELS_TO_UPGRADE = "levels_to_upgrade";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ACTIVE, active );
        bundle.put( RESETS, resets );
        bundle.put( CAN_RESET, can_reset );
        bundle.put( QUEST_DEPTHS, quest_depths );
        bundle.put(LEVELS_TO_UPGRADE, lvlsToUpgrade);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        if (bundle.getBoolean( ACTIVE )) activate();
        resets = bundle.getIntArray(RESETS);
        can_reset = bundle.getBoolean(CAN_RESET);
        quest_depths = bundle.getIntArray(QUEST_DEPTHS);
        lvlsToUpgrade = bundle.getInt(LEVELS_TO_UPGRADE);
    }

    @Override
    public int value() {
        return 999;
    }
}
