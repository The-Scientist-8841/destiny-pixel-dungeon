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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class InventoryStrongBullet extends InventoryPotionBullet {

	{
		icon = ItemSpriteSheet.Icons.POTION_STRENGTH;
	}

	@Override
	public Potion getPotion() { return new PotionOfStrength(); }

	@Override
	public ArcaneFirearm.Bullet get_bullet() {
		return new Bullet();
	}

	public static class Bullet extends ArcaneFirearm.Bullet {
		{
			baseDmg = 8;
			scalingFactorMin = 3f;
			scalingFactorMax = 5f;
			maxFactor = 5f;
			parentClass = InventoryStrongBullet.class;
		}

		@Override
		public InventoryBullet get_inventory_bullet() {
			return new InventoryStrongBullet();
		}

		@Override
		public void onHit(Char attacker, Char defender) {
			if (defender != null) {
				if (attacker instanceof Hero) {
					PotionOfStrength p = new PotionOfStrength();
					p.identify(true);
				}
			}
		}
	}

	public static class Craft extends ToolboxRecipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0).getClass().equals(PotionOfStrength.class);
        }

		@Override
		public int cost(ArrayList<Item> ingredients) { return 1; }

		@Override
		public Item craft(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item i : ingredients) { i.quantity(i.quantity() - 1); }

			InventoryStrongBullet bullets = new InventoryStrongBullet();
			bullets.quantity(5);
			return bullets;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			InventoryStrongBullet bullets = new InventoryStrongBullet();
			bullets.quantity(5);
			return bullets;
		}
	}
}
