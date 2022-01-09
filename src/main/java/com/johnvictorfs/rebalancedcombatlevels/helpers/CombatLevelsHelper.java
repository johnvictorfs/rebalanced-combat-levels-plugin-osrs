package com.johnvictorfs.rebalancedcombatlevels.helpers;

import com.google.gson.Gson;
import com.johnvictorfs.rebalancedcombatlevels.KindaRebalancedCombatLevelsConfig;
import jdk.nashorn.internal.parser.JSONParser;
import net.runelite.api.Client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class CombatLevelsHelper {
    private final Gson gson = new Gson();
    private static OsrsBoxDBMonster[] monsters = null;

    public static int combatLevelFromNPC(String npcName, int originalLevel, KindaRebalancedCombatLevelsConfig config) {
        setNpcData();

        for (OsrsBoxDBMonster monster : monsters) {
            if (Objects.equals(monster.name.trim(), npcName.trim()) && monster.combat_level == originalLevel) {
                // TODO: Json should be a single object with keys being "name_combat_level" to be more efficient
                int usedStylesCount = 0;
                int totalLevelsSum = 0;

                if (monster.attack_level > 1 || !config.excludeZeroStats()) {
                    usedStylesCount++;
                    totalLevelsSum += monster.attack_level;
                }

                if (monster.defence_level > 1 || !config.excludeZeroStats()) {
                    usedStylesCount++;
                    totalLevelsSum += monster.defence_level;
                }

                if (monster.magic_level > 1 || !config.excludeZeroStats()) {
                    usedStylesCount++;
                    totalLevelsSum += monster.magic_level;
                }

                if (monster.ranged_level > 1 || !config.excludeZeroStats()) {
                    usedStylesCount++;
                    totalLevelsSum += monster.ranged_level;
                }

                if (monster.strength_level > 1 || !config.excludeZeroStats()) {
                    usedStylesCount++;
                    totalLevelsSum += monster.strength_level;
                }

                if (usedStylesCount == 0 && config.excludeZeroStats()) {
                    return -1;
                }

                return totalLevelsSum / (config.excludeZeroStats() ? usedStylesCount : 5);
            }
        }

        return -1;
    }

    private static void setNpcData() {
        if (monsters == null) {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(Objects.requireNonNull(CombatLevelsHelper.class.getResourceAsStream("monsters-complete.json")), StandardCharsets.UTF_8);

            monsters = gson.fromJson(reader, OsrsBoxDBMonster[].class);
        }
    }

    public static String coloredFromCombatLevel(int combatLevel, String text, int playerLevel) {
        String color = null;

        try {
            // https://oldschool.runescape.wiki/w/Combat_level#Colours
            if (combatLevel == playerLevel) {
                color = "ffff00";
            } else if (combatLevel >= (playerLevel + 10)) {
                color = "ff3000";
            } else if (combatLevel >= (playerLevel + 9)) {
                color = "ff3000";
            } else if (combatLevel >= (playerLevel + 8)) {
                color = "ff3000";
            } else if (combatLevel >= (playerLevel + 7)) {
                color = "ff3000";
            } else if (combatLevel >= (playerLevel + 6)) {
                color = "ff7000";
            } else if (combatLevel >= (playerLevel + 5)) {
                color = "ff7000";
            } else if (combatLevel >= (playerLevel + 4)) {
                color = "ff7000";
            } else if (combatLevel >= (playerLevel + 3)) {
                color = "ffb000";
            } else if (combatLevel >= (playerLevel + 2)) {
                color = "ffb000";
            } else if (combatLevel >= (playerLevel + 1)) {
                color = "ffb000";
            } else if (combatLevel <= (playerLevel - 10)) {
                color = "00ff00";
            } else if (combatLevel <= (playerLevel - 9)) {
                color = "40ff00";
            } else if (combatLevel <= (playerLevel - 8)) {
                color = "40ff00";
            } else if (combatLevel <= (playerLevel - 7)) {
                color = "40ff00";
            } else if (combatLevel <= (playerLevel - 6)) {
                color = "80ff00";
            } else if (combatLevel <= (playerLevel - 5)) {
                color = "80ff00";
            } else if (combatLevel <= (playerLevel - 4)) {
                color = "80ff00";
            } else if (combatLevel <= (playerLevel - 3)) {
                color = "c0ff00";
            } else if (combatLevel <= (playerLevel - 2)) {
                color = "c0ff00";
            } else if (combatLevel <= (playerLevel - 1)) {
                color = "c0ff00";
            }
        } catch (Exception exception) {
            return text;
        }

        if (color != null) {
            return "<col=" + color + ">" + text + "<col=" + color + ">";
        }

        return text;
    }
}
