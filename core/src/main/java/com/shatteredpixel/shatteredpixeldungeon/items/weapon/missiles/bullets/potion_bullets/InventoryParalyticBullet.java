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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class InventoryParalyticBullet extends InventoryPotionBullet {

	{
		icon = ItemSpriteSheet.Icons.POTION_PARAGAS;
	}

	@Override
	public Potion getPotion() { return new PotionOfParalyticGas(); }

	@Override
	public ArcaneFirearm.Bullet get_bullet() {
		return new ParalyticBullet();
	}

	public static class ParalyticBullet extends ArcaneFirearm.Bullet {
		{
			baseDmg = 4;
			scalingFactorMin = 1.25f;
			scalingFactorMax = 1.75f;
			maxFactor = 1.5f;
			parentClass = InventoryParalyticBullet.class;
		}

		@Override
		public InventoryBullet get_inventory_bullet() {
			return new InventoryParalyticBullet();
		}

		@Override
		public void onHit(Char attacker, Char defender) {
			if (defender != null && !defender.isImmune(Paralysis.class)) {
				Buff.prolong(defender, Paralysis.class, 3f);

				if (attacker instanceof Hero) {
					PotionOfParalyticGas p = new PotionOfParalyticGas();
					p.identify(true);
				}
			}
		}
	}

	public static class ParalyticBulletCraft extends ToolboxRecipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0).getClass().equals(PotionOfParalyticGas.class);
        }

		@Override
		public int cost(ArrayList<Item> ingredients) { return 1; }

		@Override
		public Item craft(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item i : ingredients) { i.quantity(i.quantity() - 1); }

			InventoryParalyticBullet bullets = new InventoryParalyticBullet();
			bullets.quantity(5);
			return bullets;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			InventoryParalyticBullet bullets = new InventoryParalyticBullet();
			bullets.quantity(5);
			return bullets;
		}
	}
}
