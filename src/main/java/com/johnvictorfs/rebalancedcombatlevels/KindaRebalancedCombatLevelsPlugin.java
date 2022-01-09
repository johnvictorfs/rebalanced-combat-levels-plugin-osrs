package com.johnvictorfs.rebalancedcombatlevels;

import com.google.inject.Provides;

import javax.inject.Inject;

import com.johnvictorfs.rebalancedcombatlevels.helpers.CombatLevelsHelper;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.NPCManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
        name = "Rebalanced combat levels"
)
public class KindaRebalancedCombatLevelsPlugin extends Plugin {
    Pattern NPC_NAME_PATTERN = Pattern.compile("<col=f+0+>(.*)<col=f+0+> +\\(level-(\\d+)\\)");

    @Inject
    private Client client;

    @Inject
    private KindaRebalancedCombatLevelsConfig config;

    @Inject
    NPCManager npcManager;

    private int playerCombatLevel() {
        if (config.changePlayerCombatLevel()) {
            int usedStylesCount = 0;
            int totalLevelsSum = 0;

            if (client.getRealSkillLevel(Skill.ATTACK) > 1 || !config.excludeZeroStats()) {
                usedStylesCount++;
                totalLevelsSum += client.getRealSkillLevel(Skill.ATTACK);
            }

            if (client.getRealSkillLevel(Skill.DEFENCE) > 1 || !config.excludeZeroStats()) {
                usedStylesCount++;
                totalLevelsSum += client.getRealSkillLevel(Skill.DEFENCE);
            }

            if (client.getRealSkillLevel(Skill.MAGIC) > 1 || !config.excludeZeroStats()) {
                usedStylesCount++;
                totalLevelsSum += client.getRealSkillLevel(Skill.MAGIC);
            }

            if (client.getRealSkillLevel(Skill.RANGED) > 1 || !config.excludeZeroStats()) {
                usedStylesCount++;
                totalLevelsSum += client.getRealSkillLevel(Skill.RANGED);
            }

            if (client.getRealSkillLevel(Skill.STRENGTH) > 1 || !config.excludeZeroStats()) {
                usedStylesCount++;
                totalLevelsSum += client.getRealSkillLevel(Skill.STRENGTH);
            }

            if (usedStylesCount == 0 && config.excludeZeroStats()) {
                return -1;
            }

            return totalLevelsSum / (config.excludeZeroStats() ? usedStylesCount : 5);
        } else {
            Player player = client.getLocalPlayer();

            if (player == null) return -1;

            return player.getCombatLevel();
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        Widget combatLevelWidget = client.getWidget(WidgetInfo.COMBAT_LEVEL);
        if (combatLevelWidget == null || !config.changePlayerCombatLevel()) {
            return;
        }

        combatLevelWidget.setText("Combat Lvl: " + playerCombatLevel());
    }

    @Subscribe
    public void onMenuOpened(MenuOpened event) {
        MenuEntry firstEntry = event.getFirstEntry();

        if (firstEntry == null) return;

        MenuEntry[] entries = event.getMenuEntries();

        Matcher matcher = NPC_NAME_PATTERN.matcher(firstEntry.getTarget());

        System.out.println(firstEntry.getTarget());

        if (matcher.matches()) {
            String npcName = matcher.group(1);
            String oldCombatLevel = matcher.group(2);
            String oldLevelString = "level-" + oldCombatLevel;

            for (MenuEntry entry : entries) {
                if (!entry.getTarget().contains(oldLevelString)) continue;

                int newCombatLevel = CombatLevelsHelper.combatLevelFromNPC(npcName, Integer.parseInt(oldCombatLevel), config);

                if (newCombatLevel == -1) {
                    System.out.println("Could not find combat level for " + npcName + "(" + oldCombatLevel + ")");
                    // Could not find data to calculate new combat level, do nothing
                    return;
                }

                String newLevelString = config.recalculatedCombatLevelText() + newCombatLevel;

                if (config.showRegularCombatLevel()) {
                    // Show both new and old combat levels
                    String newTarget = entry.getTarget() + "  " + CombatLevelsHelper.coloredFromCombatLevel(newCombatLevel, "(" + newLevelString + ")", playerCombatLevel());
                    entry.setTarget(newTarget);
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
