/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
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

import com.google.common.base.Splitter;
import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import static com.playerattacktimer.AttackTimerMap.ATTACK_TIMER_MAP;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Player Attack Timer",
	enabledByDefault = false,
	description = "Display the tick delay for your current weapon.<br>Helps with lazy prayer flicking and flinching.",
	tags = {"player", "attack", "tick", "timer", "delay"}
)
public class PlayerAttackTimerPlugin extends Plugin
{
	private final Map<Integer, AnimationTickMapEntry> customAnimationTickMap = new HashMap<>();

	private static final Splitter NEWLINE_SPLITTER = Splitter
		.on("\n")
		.omitEmptyStrings()
		.trimResults();

	@Inject
	private Client client;

	@Inject
	private PlayerAttackTimerConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PlayerOverlay playerOverlay;

	@Inject
	private PrayerOverlay prayerOverlay;

	@Getter(AccessLevel.PACKAGE)
	private int ticksUntilNextAnimation;

	@Getter(AccessLevel.PACKAGE)
	private AttackPrayer currentPrayer;

	@Provides
	PlayerAttackTimerConfig getConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(PlayerAttackTimerConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(playerOverlay);
		overlayManager.add(prayerOverlay);

		parseCustomAnimationConfig(config.customAnimations());
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(playerOverlay);
		overlayManager.remove(prayerOverlay);

		customAnimationTickMap.clear();
	}

	@Subscribe
	private void onConfigChanged(final ConfigChanged event)
	{
		if (!event.getGroup().equals("playerattacktimer"))
		{
			return;
		}

		switch (event.getKey())
		{
			case "customAnimations":
				parseCustomAnimationConfig(config.customAnimations());
				break;
			default:
				break;
		}
	}

	@Subscribe
	private void onGameTick(final GameTick event)
	{
		if (ticksUntilNextAnimation > 0)
		{
			--ticksUntilNextAnimation;
		}

		final Player player = client.getLocalPlayer();

		if (ticksUntilNextAnimation > 0 || player == null)
		{
			return;
		}

		final int animationId = player.getAnimation();

		var entry = customAnimationTickMap.getOrDefault(animationId, ATTACK_TIMER_MAP.get(animationId));
		if (entry != null)
		{
			ticksUntilNextAnimation = entry.delay;
			currentPrayer = entry.prayer;
		}
	}

	private void parseCustomAnimationConfig(final String config)
	{
		ConfigParser.parse(config).ifPresent(map -> {
			customAnimationTickMap.clear();
			customAnimationTickMap.putAll(map);
		});
	}
}
