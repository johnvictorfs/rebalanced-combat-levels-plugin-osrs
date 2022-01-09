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

	@ConfigItem(
			keyName = "recalculatedCombatLevelText",
			name = "Text of recalculated combat level",
			description = "This sets the text that will appear together with the new combat level. Example \"(some-text-44)\""
	)
	default String recalculatedCombatLevelText() {
		return "*level-";
	}

	@ConfigItem(
			keyName = "excludeZeroStats",
			name = "Exclude Level Zero/One Stats",
			description = "If this is true, the calculation will exclude level 1/0 stats from being included in the calculations and will only make an average out of the non-zero/one stats"
	)
	default boolean excludeZeroStats() {
		return true;
	}
}
