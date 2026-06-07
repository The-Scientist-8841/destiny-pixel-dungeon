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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class InventoryIceBullet extends InventoryBullet {

	{
		image = ItemSpriteSheet.BULLET_ICECAP;
	}

	@Override
	public ArcaneFirearm.Bullet get_bullet() {
		return new IceBullet();
	}

	public static class IceBullet extends ArcaneFirearm.Bullet {
		{
			baseDmg = 3;
			scalingFactorMin = 1f;
			scalingFactorMax = 2f;
			maxFactor = 2f;
			parentClass = InventoryIceBullet.class;
		}

		@Override
		public InventoryBullet get_inventory_bullet() {
			return new InventoryIceBullet();
		}

		@Override
		public void onHit(Char attacker, Char defender) {
			if (defender != null && !defender.isImmune(Chill.class)) {
				if (defender.buff(Frost.class) != null){
					Buff.affect(defender, Frost.class, 3f);
				} else {
					Chill chill = defender.buff(Chill.class);
					float turnsToAdd = Dungeon.level.water[defender.pos] ? Chill.DURATION / 2 + 1f : Chill.DURATION;
					if (chill != null){
						float chillToCap = Chill.DURATION - chill.cooldown();
						chillToCap /= defender.resist(Chill.class); //account for resistance to chill
						turnsToAdd = Math.min(turnsToAdd, chillToCap);
					}
					if (turnsToAdd > 0f) {
						Buff.affect(defender, Chill.class, turnsToAdd);
					}
					if (chill != null
							&& chill.cooldown() >= Chill.DURATION &&
							!defender.isImmune(Frost.class)){
						Buff.affect(defender, Frost.class, Frost.DURATION);
					}
				}
			}
		}
	}

	public static class IceBulletCraft extends ToolboxRecipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0).getClass().equals(Icecap.Seed.class);
        }

		@Override
		public int cost(ArrayList<Item> ingredients) { return 1; }

		@Override
		public Item craft(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item i : ingredients) { i.quantity(i.quantity() - 1); }

			InventoryIceBullet bullets = new InventoryIceBullet();
			bullets.quantity(3);
			return bullets;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			InventoryIceBullet bullets = new InventoryIceBullet();
			bullets.quantity(3);
			return bullets;
		}
	}
}
