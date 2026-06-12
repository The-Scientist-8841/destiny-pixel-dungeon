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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ItemStatusHandler;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class InventoryPotionBullet extends InventoryBullet {
	public static final LinkedHashMap<String, Integer> colors = new LinkedHashMap<String, Integer>(){
		{
			put("crimson",ItemSpriteSheet.BULLET_CRIMSON);
			put("amber",ItemSpriteSheet.BULLET_AMBER);
			put("golden",ItemSpriteSheet.BULLET_GOLDEN);
			put("jade",ItemSpriteSheet.BULLET_JADE);
			put("turquoise",ItemSpriteSheet.BULLET_TURQUOISE);
			put("azure",ItemSpriteSheet.BULLET_AZURE);
			put("indigo",ItemSpriteSheet.BULLET_INDIGO);
			put("magenta",ItemSpriteSheet.BULLET_MAGENTA);
			put("bistre",ItemSpriteSheet.BULLET_BISTRE);
			put("charcoal",ItemSpriteSheet.BULLET_CHARCOAL);
			put("silver",ItemSpriteSheet.BULLET_SILVER);
			put("ivory",ItemSpriteSheet.BULLET_IVORY);
		}
	};
	public static final LinkedHashMap<Class<? extends InventoryPotionBullet>, Class<? extends Potion>> bulletToPotion = new LinkedHashMap<>();
	public static final LinkedHashMap<Class<? extends Potion>, Class<? extends InventoryPotionBullet>> potionToBullet = new LinkedHashMap<>();
	static {
		bulletToPotion.put(InventoryFlameBullet.class, PotionOfLiquidFlame.class);
		potionToBullet.put(PotionOfLiquidFlame.class, InventoryFlameBullet.class);
	}

	public InventoryPotionBullet() {
		super();
		reset();
	}

	public Potion getPotion() { return null; }
	public String color() {
		Potion p = getPotion();
		if (p != null) return p.getColor();
		else return "crimson";
	}

	@Override
	public void reset() {
		super.reset();
		image = colors.get(color());
	}

	protected boolean anonymous = false;
	public boolean isKnown() {
		Potion p = getPotion();
		if (p != null) return p.isKnown() || anonymous;
		else return anonymous;
	}

	public void anonymize() {
		if (!isKnown()) image = ItemSpriteSheet.BULLET_HOLDER;
		anonymous = true;
	}

	@Override
	public String desc() {
		if (isKnown()) return super.desc();
		else {
			Potion p = getPotion();
			if (p != null) return Messages.get(this, "unknown_desc", p.name());
			else return Messages.get(this, "unknown_desc_nopotion");
		}
	}

	@Override
	public String name() {
		if (isKnown()) return super.name();
		else return Messages.get(this, color());
	}

	@Override
	public String info() {
		//skip custom notes if anonymized and un-Ided
		Potion p = getPotion();
		return (anonymous && (p == null || !p.isKnown())) ? desc() : super.info();
	}

	@Override
	public boolean isIdentified() {
		return isKnown();
	}
}
