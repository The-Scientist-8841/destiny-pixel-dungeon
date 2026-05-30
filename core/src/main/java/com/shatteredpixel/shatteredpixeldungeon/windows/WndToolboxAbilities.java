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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Toolbox;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndToolboxAbilities extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 180;

	private static final int MARGIN  = 2;

	public WndToolboxAbilities(Toolbox toolbox ){
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		float pos = MARGIN;
		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title")), 9);
		title.hardlight(TITLE_COLOR);
		title.setPos((width-title.width())/2, pos);
		title.maxWidth(width - MARGIN * 2);
		add(title);

		pos = title.bottom() + 3*MARGIN;

		Image icon = new ItemSprite(new Item(){ {image = ItemSpriteSheet.ARTIFACT_TOOLBOX; }});

		for (Toolbox.ToolboxAbilities ability : Toolbox.ToolboxAbilities.values()) {
			if (toolbox.abilityIsApplicable(Dungeon.hero, ability)) {
				float left = 2 * width / 3f + MARGIN;
				if (ability.chargeCost() > 0) {
					Image chargeIcon = new ItemSprite(ItemSpriteSheet.ARTIFACT_TOOLBOX);
					chargeIcon.x = left;
					chargeIcon.y = pos + 1.5f;
					add(chargeIcon);
					RenderedTextBlock txt = PixelScene.renderTextBlock(Messages.decimalFormat("#.#", ability.chargeCost()), 8);
					txt.setRect(left + 16 + MARGIN, pos + 4, width / 4f, 16);
					add(txt);

					left = 5 * width / 6f + 2*MARGIN;
				} else if (ability == Toolbox.ToolboxAbilities.ARTIFACT && toolbox.artifact != null && toolbox.artifact.status() != null) {
					Image artifactIcon = new ItemSprite(toolbox.artifact.image);
					artifactIcon.x = left;
					artifactIcon.y = pos + (16 - artifactIcon.height())/2;
					add(artifactIcon);
					RenderedTextBlock txt = PixelScene.renderTextBlock(toolbox.artifact.status(), 8);
					txt.setRect(left + artifactIcon.width() + MARGIN, pos + 4, width / 4f, 16);
					add(txt);

					left = 5 * width / 6f;
				} else if (ability == Toolbox.ToolboxAbilities.ARTIFACT2 && toolbox.artifact2 != null && toolbox.artifact2.status() != null) {
					Image artifactIcon = new ItemSprite(toolbox.artifact2.image);
					artifactIcon.x = left;
					artifactIcon.y = pos + (16 - artifactIcon.height())/2;
					add(artifactIcon);
					RenderedTextBlock txt = PixelScene.renderTextBlock(toolbox.artifact2.status(), 8);
					txt.setRect(left + artifactIcon.width() + MARGIN, pos + 4, width / 4f, 16);
					add(txt);

					left = 5 * width / 6f;
				}

				if (ability.materialsCost() > 0) {
					Image materialsIcon = new ItemSprite(ItemSpriteSheet.ARCANE_MATERIAL);
					materialsIcon.x = left;
					materialsIcon.y = pos;
					add(materialsIcon);
					RenderedTextBlock materialsCost = PixelScene.renderTextBlock(Integer.toString(ability.materialsCost()), 8);
					materialsCost.setRect(left + 16 + MARGIN, pos + 4, width / 4f - MARGIN, 16);
					add(materialsCost);
				}

				RedButton abilityBtn = new RedButton(toolbox.getAbilityTitle(ability), 8) {
					@Override
					protected void onClick() {
						super.onClick();
						hide();
						toolbox.useAbility(ability);
					}
				};
				float L = 0f;
				float W = 2* width/3f - MARGIN;
				if (ability == Toolbox.ToolboxAbilities.DECONSTRUCT_ARTIFACT) {
					Image artifactIcon = new ItemSprite(toolbox.artifact.image);
					artifactIcon.x = 0;
					artifactIcon.y = pos + (16 - artifactIcon.height())/2f;
					add(artifactIcon);

					L += artifactIcon.width() + MARGIN;
					W -= artifactIcon.width() + MARGIN;
				} else if (ability == Toolbox.ToolboxAbilities.DECONSTRUCT_ARTIFACT2) {
					Image artifactIcon = new ItemSprite(toolbox.artifact2.image);
					artifactIcon.x = 0;
					artifactIcon.y = pos + (16 - artifactIcon.height())/2f;
					add(artifactIcon);

					L += artifactIcon.width() + MARGIN;
					W -= artifactIcon.width() + MARGIN;
				} else if (ability == Toolbox.ToolboxAbilities.RETURN_ARTIFACT) {
					Image artifactIcon = new ItemSprite(toolbox.artifact.image);
					artifactIcon.x = 0;
					artifactIcon.y = pos + (16 - artifactIcon.height())/2f;
					add(artifactIcon);

					L += artifactIcon.width() + MARGIN;
					W -= artifactIcon.width() + MARGIN;
				} else if (ability == Toolbox.ToolboxAbilities.RETURN_ARTIFACT2) {
					Image artifactIcon = new ItemSprite(toolbox.artifact2.image);
					artifactIcon.x = 0;
					artifactIcon.y = pos + (16 - artifactIcon.height())/2f;
					add(artifactIcon);

					L += artifactIcon.width() + MARGIN;
					W -= artifactIcon.width() + MARGIN;
				}
				abilityBtn.setSize(W, 16);
				abilityBtn.setRect(L, pos, W, 16);
				abilityBtn.enable(toolbox.canUseAbility(Dungeon.hero, ability));

				if (ability == Toolbox.ToolboxAbilities.ARTIFACT && toolbox.artifact != null) {
					abilityBtn.icon(new ItemSprite(toolbox.artifact.image()));
				} else if (ability == Toolbox.ToolboxAbilities.ARTIFACT2 && toolbox.artifact2 != null) {
					abilityBtn.icon(new ItemSprite(toolbox.artifact2.image()));
				}

				add(abilityBtn);

				pos = abilityBtn.bottom() + MARGIN;
			}
		}

		resize(width, (int)pos);

	}


}
