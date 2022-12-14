/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
 * Copyright (c) 2022, osrsterrabl <https://github.com/osrsterrabl>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.playerattacktimer;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

import java.awt.Color;


@ConfigGroup("playerattacktimer")
public interface PlayerAttackTimerConfig extends Config
{
	@ConfigSection(
		name = "Settings",
		description = "",
		position = 0
	)
	String settings = "Settings";

	@ConfigSection(
		name = "Font Settings",
		description = "",
		position = 1
	)
	String fontSettings = "Font Settings";

	@ConfigSection(
		name = "Prayer Settings",
		description = "configure whether to draw tick counters in the prayer tab",
		position = 2
	)
	String prayerSettings = "Prayer Settings";

	//------------------------------------------------------------//
	// Settings
	//------------------------------------------------------------//

	@ConfigItem(
		name = "Debug animation ids",
		description = "Show your player's current animation ID."
			+ "<br>Animation IDs can be viewed by wielding a weapon and attacking an NPC.",
		position = 0,
		keyName = "debugAnimationIds",
		section = settings
	)
	default boolean debugAnimationIds()
	{
		return false;
	}

	@ConfigItem(
		name = "Custom animations (one per line)",
		description = "Syntax AnimationID:TickDelay"
			+ "<br>e.g. Abyssal whip would be 1658:4"
			+ "<br>optionally include one of (r, a, p, e, m, c, h) to draw prayer helpers."
			+ "<br> supported prayers: Rigour, Augury, Piety, Eagle Eye, Mystic Might, Chivalry, Hawk Eye"
			+ "<br>e.g. Abyssal whip with piety would be 1658:4:p"
			+ "<br>Animation Ids can be obtained by enabling the above debug setting."
			+ "<br>Weapon tick delays can be found on the wiki.",
		position = 1,
		keyName = "customAnimations",
		section = settings
	)
	default String customAnimations()
	{
		return "";
	}

	//------------------------------------------------------------//
	// Font Settings
	//------------------------------------------------------------//

	@ConfigItem(
		name = "Font shadow",
		description = "Toggle font shadow.",
		position = 0,
		keyName = "fontShadow",
		section = fontSettings
	)
	default boolean fontShadow()
	{
		return true;
	}

	@Range(
		min = 8,
		max = 64
	)
	@ConfigItem(
		name = "Font size",
		description = "Adjust font size.",
		position = 1,
		keyName = "fontSize",
		section = fontSettings
	)
	default int fontSize()
	{
		return 20;
	}

	@Alpha
	@ConfigItem(
		name = "Font color",
		description = "Adjust font color.",
		position = 2,
		keyName = "fontColor",
		section = fontSettings
	)
	default Color fontColor()
	{
		return new Color(255, 0, 0, 255);
	}

	@Alpha
	@ConfigItem(
			name = "Flick color",
			description = "Adjust flick color.",
			position = 3,
			keyName = "flickColor",
			section = fontSettings
	)
	default Color flickColor()
	{
		return new Color(255, 255, 255, 255);
	}

	@Range(
		min = -100,
		max = 100
	)
	@ConfigItem(
		name = "Font zOffset",
		description = "Adjust the Z coordinate offset.",
		position = 4,
		keyName = "fontZOffset",
		section = fontSettings
	)
	default int fontZOffset()
	{
		return 0;
	}

	//------------------------------------------------------------//
	// Prayer Settings
	//------------------------------------------------------------//

	@ConfigItem(
		name = "Draw prayer helper",
		description = "Draws ticks on attack prayers. Must be configured in animations by appending a :a.",
		position = 0,
		keyName = "drawPrayerHelper",
		section = prayerSettings
	)
	default boolean drawPrayerHelper()
	{
		return false;
	}

	@ConfigItem(
		name = "Draw bounding box",
		description = "",
		position = 1,
		keyName = "drawBoundingBox",
		section = prayerSettings
	)
	default boolean drawBoundingBox()
	{
		return true;
	}

	@ConfigItem(
		name = "Always show",
		description = "Ignores what inventory tab you have selected",
		position = 2,
		keyName = "alwaysShow",
		section = prayerSettings
	)
	default boolean alwaysShow()
	{
		return false;
	}
}
