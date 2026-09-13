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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.exotic;

import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryFlameBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryPotionBullet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.LinkedHashMap;

public class InventoryExoticPotionBullet extends InventoryPotionBullet {
	public static final LinkedHashMap<String, Integer> colors = new LinkedHashMap<String, Integer>(){
		{
			put("crimson",ItemSpriteSheet.EXOTIC_BULLET_CRIMSON);
			put("amber",ItemSpriteSheet.EXOTIC_BULLET_AMBER);
			put("golden",ItemSpriteSheet.EXOTIC_BULLET_GOLDEN);
			put("jade",ItemSpriteSheet.EXOTIC_BULLET_JADE);
			put("turquoise",ItemSpriteSheet.EXOTIC_BULLET_TURQUOISE);
			put("azure",ItemSpriteSheet.EXOTIC_BULLET_AZURE);
			put("indigo",ItemSpriteSheet.EXOTIC_BULLET_INDIGO);
			put("magenta",ItemSpriteSheet.EXOTIC_BULLET_MAGENTA);
			put("bistre",ItemSpriteSheet.EXOTIC_BULLET_BISTRE);
			put("charcoal",ItemSpriteSheet.EXOTIC_BULLET_CHARCOAL);
			put("silver",ItemSpriteSheet.EXOTIC_BULLET_SILVER);
			put("ivory",ItemSpriteSheet.EXOTIC_BULLET_IVORY);
		}
	};

	public InventoryExoticPotionBullet() {
		super();
		reset();
	}

	@Override
	public void reset() {
		super.reset();
		image = colors.get(color());
	}

	protected boolean anonymous = false;

	@Override
	public String name() {
		if (isKnown()) return super.name();
		else return Messages.get(this, color());
	}
}
