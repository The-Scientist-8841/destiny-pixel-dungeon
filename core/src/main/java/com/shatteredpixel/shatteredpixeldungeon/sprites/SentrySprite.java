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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Toolbox;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

public class SentrySprite extends MobSprite {

	private Animation tierIdles[] = new Animation[5];
	private Animation tierAttacks[] = new Animation[5];
	private Animation tierZaps[] = new Animation[5];
	private Animation tierDies[] = new Animation[5];

	public SentrySprite(){
		super();

		texture(Assets.Sprites.SENTRIES);

		TextureFilm frames = new TextureFilm(texture, 16, 16);
		for (int i = 1; i < 5; i += 1) {
			int start = 8*(i-1);
			tierIdles[i] = new Animation(1, true);
			tierIdles[i].frames(frames, start);

			tierAttacks[i] = new Animation(12, false);
			tierAttacks[i].frames(frames, start + 1, start + 2, start + 3);

			tierZaps[i] = tierAttacks[i].clone();

			tierDies[i] = new Animation(12, false);
			tierDies[i].frames(frames, start + 4, start + 5, start + 6, start + 7);
		}
	}

	@Override
	public void zap( int cell ) {
		super.zap( cell );

		MagicMissile.boltFromChar( parent,
				MagicMissile.FIRE,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((Toolbox.Sentry)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}
	
	@Override
	public void turnTo(int from, int to) {
		//do nothing
	}
	
	@Override
	public void die() {
		super.die();
		emitter().burst(Toolbox.SentryParticle.UP, 10);
	}

	public void linkVisuals(Char ch){

		if (ch instanceof Toolbox.Sentry) {
			updateTier(((Toolbox.Sentry) ch).tier);
		} else {
			updateTier(1); //defaults to 1
		}
		
	}

	public void updateTier(int tier){

		idle = tierIdles[tier];
		run = idle.clone();
		attack = tierAttacks[tier];
		die = tierDies[tier];
		zap = tierZaps[tier];

		//always render first
		if (parent != null) {
			parent.sendToBack(this);
		}

		resetColor();
		if (ch != null) place(ch.pos);
		idle();

		shadowWidth     = 1.2f;
		shadowHeight    = 0.25f;
		perspectiveRaise = 6 / 16f; //6 pixels
	}

	@Override
	public int blood() {
		return 0xFFEEEE;
	}
}
