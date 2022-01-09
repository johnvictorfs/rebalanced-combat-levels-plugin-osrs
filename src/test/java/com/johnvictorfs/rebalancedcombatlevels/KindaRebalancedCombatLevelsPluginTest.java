package com.johnvictorfs.rebalancedcombatlevels;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class KindaRebalancedCombatLevelsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(KindaRebalancedCombatLevelsPlugin.class);
		RuneLite.main(args);
	}
}