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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class InventoryRotBullet extends InventoryBullet {

	{
		image = ItemSpriteSheet.BULLET_ROTBERRY;
	}

	@Override
	public ArcaneFirearm.Bullet get_bullet() {
		return new RotBullet();
	}

	@Override
	public String desc() {
		ArcaneFirearm gun = Dungeon.hero != null ? Dungeon.hero.belongings.getItem(ArcaneFirearm.class) : null;
		int lvl = gun != null ? gun.buffedLvl() : 1;

		return super.desc() + "\n\n" + Messages.get(this, "corrosion_desc", 5+2*lvl);
	}

	public static class RotBullet extends ArcaneFirearm.Bullet {
		{
			baseDmg = 10;
			scalingFactorMin = 3f;
			scalingFactorMax = 5f;
			maxFactor = 5f;
			parentClass = InventoryRotBullet.class;
		}

		@Override
		public InventoryBullet get_inventory_bullet() {
			return new InventoryRotBullet();
		}

		@Override
		public void onHit(Char attacker, Char defender) {
			if (defender != null && !defender.isImmune(Corrosion.class)) {
				Buff.affect(defender, Corrosion.class).set(5 + 2*gun.buffedLvl(), 5 + 2*gun.buffedLvl());
			}
		}
	}

	public static class RotBulletCraft extends ToolboxRecipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0).getClass().equals(Rotberry.Seed.class);
        }

		@Override
		public int cost(ArrayList<Item> ingredients) { return 2; }

		@Override
		public Item craft(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item i : ingredients) { i.quantity(i.quantity() - 1); }

			InventoryRotBullet bullets = new InventoryRotBullet();
			bullets.quantity(4);
			return bullets;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			InventoryRotBullet bullets = new InventoryRotBullet();
			bullets.quantity(4);
			return bullets;
		}
	}
}
