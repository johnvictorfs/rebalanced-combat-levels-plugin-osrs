package com.johnvictorfs.rebalancedcombatlevels;

import com.google.inject.Provides;

import javax.inject.Inject;

import com.johnvictorfs.rebalancedcombatlevels.helpers.CombatLevelsHelper;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
        name = "Rebalanced combat levels"
)
public class KindaRebalancedCombatLevelsPlugin extends Plugin {
    Pattern NPC_NAME_PATTERN = Pattern.compile("<col=ffff00>(.*)<col=ff00> +\\(level-(\\d+)\\)");

    @Inject
    private Client client;

    @Inject
    private KindaRebalancedCombatLevelsConfig config;

    @Override
    protected void startUp() throws Exception {
        log.info("Rebalanced combat levels started!");
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Rebalanced combat levels stopped!");
    }

    @Subscribe
    public void onMenuOpened(MenuOpened event) {
        MenuEntry[] entries = event.getMenuEntries();
        MenuEntry firstEntry = event.getFirstEntry();
        Matcher matcher = NPC_NAME_PATTERN.matcher(firstEntry.getTarget());

        if (matcher.matches()) {
            String npcName = matcher.group(1);
            String oldCombatLevel = matcher.group(2);
            String oldLevelString = "level-" + oldCombatLevel;

            for (MenuEntry entry : entries) {
                if (!entry.getTarget().contains(oldLevelString)) continue;

                int newCombatLevel = CombatLevelsHelper.combatLevelFromNPC(npcName, Integer.parseInt(oldCombatLevel));

                if (newCombatLevel == -1) {
                    System.out.println("Could not find combat level for " + npcName + "(" + oldCombatLevel + ")");
                    // Could not find data to calculate new combat level, do nothing
                    return;
                }

                String newLevelString = "new-level-" + newCombatLevel;

                if (config.showRegularCombatLevel()) {
                    // Show both new and old combat levels
                    entry.setTarget(entry.getTarget() + " (" + newLevelString + ")");
                } else {
                    // Show only new combat levels
                    entry.setTarget(entry.getTarget().replaceAll(oldLevelString, newLevelString));
                }
            }
        }
    }

    @Provides
    KindaRebalancedCombatLevelsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(KindaRebalancedCombatLevelsConfig.class);
    }
}
