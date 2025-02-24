package com.example.probabilitysimulator;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // コマンドを登録
        getCommand("probability").setExecutor(new ProbabilityCommand());

        getLogger().info("ProbabilitySimulator プラグインが有効になりました！");
    }

    @Override
    public void onDisable() {
        getLogger().info("ProbabilitySimulator プラグインが無効になりました！");
    }
}
