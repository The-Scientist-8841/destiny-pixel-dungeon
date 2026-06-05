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

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MiniBomb extends Bomb {
	
	{
		image = ItemSpriteSheet.MINI_BOMB;
	}

	@Override
	protected int explosionRange() {
		return 0;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(Dungeon.scalingDepth(), 5 + 2*Dungeon.scalingDepth());
	}

	@Override
	public int fuseDelay() { return 0; }

	@Override
	protected void onThrow( int cell ) {
		super.onThrow( cell );
	}
	
	@Override
	public int value() {
		return quantity * 10;
	}

	@Override
	public int particleAmt() { return 15; }

	@Override
	public String desc() {
		int depth = Dungeon.hero == null ? 1 : Dungeon.scalingDepth();
		String desc = Messages.get(this, "desc", depth, 5 + 2*depth);
		if (fuse == null) {
			return desc + "\n\n" + Messages.get(this, "desc_fuse");
		} else {
			return desc + "\n\n" + Messages.get(this, "desc_burning");
		}
	}

	public static class MiniBombCraft extends ToolboxRecipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			return ingredients.size() == 1 && ingredients.get(0).getClass().equals(Bomb.class);
		}

		@Override
		public int cost(ArrayList<Item> ingredients) { return 1; }

		@Override
		public Item craft(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item i : ingredients) { i.quantity(i.quantity() - 1); }

			return new MiniBomb().quantity(3);
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			return new MiniBomb().quantity(3);
		}
	}
}
