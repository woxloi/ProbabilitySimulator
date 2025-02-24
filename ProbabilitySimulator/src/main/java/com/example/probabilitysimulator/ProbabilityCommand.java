package com.example.probabilitysimulator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class ProbabilityCommand implements CommandExecutor {

    // プラグインのON/OFF状態を管理するフラグ
    private static boolean isPluginEnabled = true;

    // プレフィックスの定義（ここにカラーコードも含める）
    private static final String PREFIX = "§b[ProbabilitySimulator]§r ";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /probability on コマンド
        if (args.length == 1 && args[0].equalsIgnoreCase("on")) {
            isPluginEnabled = true;
            sender.sendMessage(PREFIX + "§aプラグインがONになりました。試行コマンドが使用できます。");
            return true;
        }

        // /probability off コマンド
        if (args.length == 1 && args[0].equalsIgnoreCase("off")) {
            isPluginEnabled = false;
            sender.sendMessage(PREFIX + "§cプラグインがOFFになりました。試行コマンドは使用できません。");
            return true;
        }

        // /probability help コマンド
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender);
            return true;
        }

        // プラグインがOFFの場合、試行コマンドを使わせない
        if (!isPluginEnabled) {
            sender.sendMessage(PREFIX + "§cプラグインがOFFのため、試行コマンドは使用できません。");
            return false;
        }

        // コマンドを実行したのがプレイヤーであることを確認
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // 引数が2つ以上必要
            if (args.length != 2) {
                player.sendMessage(PREFIX + "§e正しいコマンド形式は /probability simulator <確率(0.00001〜1)> <試行回数> です。");
                return false;
            }

            try {
                // 確率を0〜1の小数で受け取る（例: 0.00001 → 0.00001%）
                double probability = Double.parseDouble(args[0]);
                int trials = Integer.parseInt(args[1]);

                // 確率の範囲チェック（0.00001〜1の間）
                if (probability < 0.00001 || probability > 1) {
                    player.sendMessage(PREFIX + "§c確率は0.00001から1の間で指定してください。");
                    return false;
                }

                // 試行回数の範囲チェック
                if (trials <= 0) {
                    player.sendMessage(PREFIX + "§c試行回数は1回以上で指定してください。");
                    return false;
                }

                // 結果のカウント
                int successCount = 0;
                int failureCount = 0;

                Random random = new Random();

                // 試行を指定回数分繰り返し
                for (int i = 0; i < trials; i++) {
                    // ランダムに成功/失敗を判定
                    boolean isSuccess = random.nextDouble() < probability; // 0〜1のランダム値で比較

                    if (isSuccess) {
                        successCount++;
                    } else {
                        failureCount++;
                    }
                }

                // 実際の成功率を計算
                double successRate = (double) successCount / trials * 100;
                double expectedSuccessRate = probability * 100;  // 期待される成功率（確率 × 100）
                double deviation = successRate - expectedSuccessRate;

                // 結果の表示
                player.sendMessage(PREFIX + "§eシミュレーション結果:");
                player.sendMessage(PREFIX + "成功回数: §a" + successCount + "回");
                player.sendMessage(PREFIX + "失敗回数: §c" + failureCount + "回");
                player.sendMessage(PREFIX + "成功率: §e" + String.format("%.5f", successRate) + "%");
                player.sendMessage(PREFIX + "期待成功率: §e" + String.format("%.5f", expectedSuccessRate) + "%");

                // 上振れ/下振れのメッセージ
                if (deviation > 0) {
                    player.sendMessage(PREFIX + "§a確率の" + String.format("%.5f", deviation) + "% 上振れました！");
                } else if (deviation < 0) {
                    player.sendMessage(PREFIX + "§c確率の" + String.format("%.5f", -deviation) + "% 下振れました！");
                } else {
                    player.sendMessage(PREFIX + "§e確率に対して差異はありませんでした。");
                }

            } catch (NumberFormatException e) {
                player.sendMessage(PREFIX + "§c確率と試行回数は正しい形式で指定してください。");
            }
        } else {
            sender.sendMessage(PREFIX + "§cプレイヤーのみコマンドを実行できます！");
        }
        return true;
    }

    // ヘルプメッセージを送信するメソッド
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(PREFIX + "§e[ProbabilitySimulator] プラグインの使い方:");
        sender.sendMessage(PREFIX + "§b/probability on §e- プラグインをONにします。");
        sender.sendMessage(PREFIX + "§b/probability off §e- プラグインをOFFにします。");
        sender.sendMessage(PREFIX + "§b/probability simulator <確率(0.00001〜1)> <試行回数> §e- 指定した確率で試行を行い、結果を表示します。");
        sender.sendMessage(PREFIX + "§b/probability help §e- このヘルプを表示します。");
    }

    // プラグインのON/OFF状態を取得するメソッド
    public static boolean isPluginEnabled() {
        return isPluginEnabled;
    }
}
