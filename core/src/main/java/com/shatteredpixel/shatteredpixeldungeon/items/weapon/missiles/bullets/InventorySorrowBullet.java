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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class InventorySorrowBullet extends InventoryBullet {

	{
		image = ItemSpriteSheet.BULLET_SORROWMOSS;
	}

	@Override
	public ArcaneFirearm.Bullet get_bullet() {
		return new SorrowBullet();
	}

	public static class SorrowBullet extends ArcaneFirearm.Bullet {
		{
			baseDmg = 3;
			scalingFactorMin = 1f;
			scalingFactorMax = 2f;
			maxFactor = 2f;
			parentClass = InventorySorrowBullet.class;
		}

		@Override
		public InventoryBullet get_inventory_bullet() {
			return new InventorySorrowBullet();
		}

		@Override
		public void onHit(Char attacker, Char defender) {
			int lvl = gun != null ? gun.buffedLvl() : 1;
			if (defender != null && !defender.isImmune(Poison.class)) {
				Buff.affect(defender, Poison.class).set(3+1.5f*lvl);
			}
		}
	}

	public static class SorrowBulletCraft extends ToolboxRecipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0).getClass().equals(Sorrowmoss.Seed.class);
        }

		@Override
		public int cost(ArrayList<Item> ingredients) { return 1; }

		@Override
		public Item craft(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item i : ingredients) { i.quantity(i.quantity() - 1); }

			InventorySorrowBullet bullets = new InventorySorrowBullet();
			bullets.quantity(3);
			return bullets;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			InventorySorrowBullet bullets = new InventorySorrowBullet();
			bullets.quantity(3);
			return bullets;
		}
	}
}
