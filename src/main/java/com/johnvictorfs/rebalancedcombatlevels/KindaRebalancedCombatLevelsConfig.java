package com.johnvictorfs.rebalancedcombatlevels;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("kindarebalancedcombatlevels")
public interface KindaRebalancedCombatLevelsConfig extends Config
{
	@ConfigItem(
			keyName = "showRegularCombatLevel",
			name = "Show Regular Combat Level",
			description = "Configures whether the regular (Jagex's) Combat Levels of NPCs should appear"
	)
	default boolean showRegularCombatLevel() {
		return true;
	}
}
