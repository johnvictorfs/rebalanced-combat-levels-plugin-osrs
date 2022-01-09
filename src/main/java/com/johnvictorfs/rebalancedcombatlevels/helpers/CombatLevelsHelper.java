package com.johnvictorfs.rebalancedcombatlevels.helpers;

import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;

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

    public static int combatLevelFromNPC(String npcName, int originalLevel) {
        setNpcData();

        for (OsrsBoxDBMonster monster : monsters) {
            if (Objects.equals(monster.name, npcName) && monster.combat_level == originalLevel) {
                // TODO: Json should be a single object with keys being "name_combat_level" to be more efficient
                int usedStylesCount = 0;
                int totalLevelsSum = 0;

                if (monster.attack_level > 1) {
                    usedStylesCount++;
                    totalLevelsSum += monster.attack_level;
                }

                if (monster.defence_level > 1) {
                    usedStylesCount++;
                    totalLevelsSum += monster.defence_level;
                }

                if (monster.magic_level > 1) {
                    usedStylesCount++;
                    totalLevelsSum += monster.magic_level;
                }

                if (monster.ranged_level > 1) {
                    usedStylesCount++;
                    totalLevelsSum += monster.ranged_level;
                }

                if (monster.strength_level > 1) {
                    usedStylesCount++;
                    totalLevelsSum += monster.strength_level;
                }

                if (usedStylesCount == 0) {
                    return -1;
                }

                return totalLevelsSum / usedStylesCount;
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
}
