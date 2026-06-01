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
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;

public class SentrySprite extends MobSprite {

	private Animation tierIdles[] = new Animation[5];

	public SentrySprite(){
		super();

		texture(Assets.Sprites.SENTRIES);

		tierIdles[1] = new Animation( 1, true );
		tierIdles[1].frames(texture.uvRect(1, 2, 15, 16));

		tierIdles[2] = new Animation( 1, true );
		tierIdles[2].frames(texture.uvRect(17, 2, 31, 16));

		tierIdles[3] = new Animation( 1, true );
		tierIdles[3].frames(texture.uvRect(33, 2, 47, 16));

		tierIdles[4] = new Animation( 1, true );
		tierIdles[4].frames(texture.uvRect(49, 2, 63, 16));

	}

	@Override
	public void zap( int pos ) {
		idle();
		flash();
		emitter().burst(Toolbox.SentryParticle.UP, 2);
		if (Actor.findChar(pos) != null){
			parent.add(new Beam.SunRay(center(), Actor.findChar(pos).sprite.center()));
		} else {
			parent.add(new Beam.SunRay(center(), DungeonTilemap.raisedTileCenterToWorld(pos)));
		}
		Sample.INSTANCE.play( Assets.Sounds.RAY );
		((Toolbox.Sentry)ch).onZapComplete();
	}
	
	@Override
	public void turnTo(int from, int to) {
		//do nothing
	}
	
	@Override
	public void die() {
		super.die();
		//cancels die animation and fades out immediately
		play(idle, true);
		emitter().burst(Toolbox.SentryParticle.UP, 10);
		parent.add( new AlphaTweener( this, 0, 2f ) {
			@Override
			protected void onComplete() {
				SentrySprite.this.killAndErase();
				parent.erase( this );
			}
		} );
	}

	public void linkVisuals(Char ch ){

		if (ch instanceof Toolbox.Sentry) {
			updateTier(((Toolbox.Sentry) ch).tier);
		} else {
			updateTier(1); //defaults to 1
		}
		
	}

	public void updateTier(int tier){

		idle = tierIdles[tier];
		run = idle.clone();
		attack = idle.clone();
		die = idle.clone();

		//always render first
		if (parent != null) {
			parent.sendToBack(this);
		}

		resetColor();
		if (ch != null) place(ch.pos);
		idle();

		if (tier <= 3){
			shadowWidth     = shadowHeight    = 1f;
			perspectiveRaise = (16 - height()) / 32f; //center of the cell
		} else {
			shadowWidth     = 1.2f;
			shadowHeight    = 0.25f;
			perspectiveRaise = 6 / 16f; //6 pixels
		}

	}

	private float baseY = Float.NaN;

	@Override
	public void place(int cell) {
		super.place(cell);
		baseY = y;
	}

	@Override
	public void update() {
		super.update();
		//if tier is greater than 3
		if (perspectiveRaise >= 6 / 16f && !paused){
			if (Float.isNaN(baseY)) baseY = y;
			y = baseY + (float) Math.sin(Game.timeTotal);
			shadowOffset = 0.25f - 0.8f*(float) Math.sin(Game.timeTotal);
		}
	}

	@Override
	public int blood() {
		return 0xFFCC33FF;
	}
}
