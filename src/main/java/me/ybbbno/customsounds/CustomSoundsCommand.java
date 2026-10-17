package me.ybbbno.customsounds;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.deadybbb.ybmj.PluginProvider;
import me.ybbbno.customsounds.lavaplayer.types.EntityAP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSoundsCommand implements BasicCommand {
    private final PluginProvider plugin;
    private final VoicechatManager voicechatManager;

    public CustomSoundsCommand(PluginProvider plugin, VoicechatManager voicechatManager) {
        this.plugin = plugin;
        this.voicechatManager = voicechatManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();

        Location executionLocation = null;
        if (sender instanceof Player player) {
            executionLocation = player.getLocation();
        } else if (sender instanceof BlockCommandSender commandBlock) {
            executionLocation = commandBlock.getBlock().getLocation();
        }

        if (args.length < 1) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Использование: /customsounds <play3d|play|stopsound> [args...]");
            return;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "playentity" -> handlePlayEntity(sender, args);
            case "play3d" -> handlePlay3D(sender, executionLocation, args);
            case "play" -> handlePlay(sender, args);
            case "stopsound" -> handleStopSound(sender, executionLocation, args);
            default -> LegacyTextHandler.sendFormattedMessage(sender, "<red>Неизвестная подкоманда. Используйте: play3d, play, stopsound");
        }
    }

    private void handlePlayEntity(CommandSender sender, String[] args) {
        if (args.length < 5) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Использование: /customsounds play <filename> <player> <distance> <volume>");
            return;
        }

        String filename = args[1];
        Path soundFile = plugin.getDataFolder().toPath().resolve(filename);

        if (!Files.exists(soundFile)) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Файл не найден или недействителен: " + filename);
            return;
        }

        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Такого игрока нет.");
            return;
        }

        float distance;
        try {
            distance = Float.parseFloat(args[3]);
            if (distance <= 0) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Дистанция должна быть положительной.");
                return;
            }
        } catch (NumberFormatException e) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Неверный формат дистанции.");
            return;
        }

        int volume;
        try {
            volume = Integer.parseInt(args[4]);
            if (volume <= 0) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Громкость должна быть положительной.");
                return;
            }
        } catch (NumberFormatException e) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Неверный формат громкости.");
            return;
        }

        try {
            if (voicechatManager.getEntity(player) == null) {
                voicechatManager.createAudioPlayer(player, distance);
            }

            String filePath = soundFile.toAbsolutePath().toString();
            voicechatManager.loadTrack(filePath, (Entity) player, volume);

            LegacyTextHandler.sendFormattedMessage(sender, "<green>Звук воспроизводится у " + player.getName());
        } catch (Exception e) {
            plugin.logger.severe("Error playing sound for " + player.getName() + ": " + e.getMessage());
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Произошла ошибка при воспроизведении звука.");
        }
    }

    private void handlePlay(CommandSender sender, String[] args) {
        if (args.length < 4) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Использование: /customsounds play <filename> <player> <volume>");
            return;
        }

        String filename = args[1];
        Path soundFile = plugin.getDataFolder().toPath().resolve(filename);

        if (!Files.exists(soundFile)) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Файл не найден или недействителен: " + filename);
            return;
        }

        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Такого игрока нет.");
            return;
        }

        int volume;
        try {
            volume = Integer.parseInt(args[3]);
            if (volume <= 0) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Громкость должна быть положительной.");
                return;
            }
        } catch (NumberFormatException e) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Неверный формат громкости.");
            return;
        }

        try {
            if (voicechatManager.getPlayer(player) == null) {
                voicechatManager.createAudioPlayer(player);
            }

            String filePath = soundFile.toAbsolutePath().toString();
            voicechatManager.loadTrack(filePath, player, volume);

            LegacyTextHandler.sendFormattedMessage(sender, "<green>Звук воспроизводится у " + player.getName());
        } catch (Exception e) {
            plugin.logger.severe("Error playing sound for " + player.getName() + ": " + e.getMessage());
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Произошла ошибка при воспроизведении звука.");
        }
    }

    private void handlePlay3D(CommandSender sender, @Nullable Location executionLocation, String[] args) {
        if (args.length < 7) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Использование: /customsounds play3d <filename> <x> <y> <z> <distance> <volume>");
            return;
        }

        String filename = args[1];
        Path soundFile = plugin.getDataFolder().toPath().resolve(filename);

        if (!Files.exists(soundFile)) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Файл не найден или недействителен: " + filename);
            return;
        }

        double x, y, z;
        try {
            x = Math.floor(parseCoordinate(args[2], executionLocation != null ? executionLocation.getX() : 0));
            y = Math.floor(parseCoordinate(args[3], executionLocation != null ? executionLocation.getY() : 0));
            z = Math.floor(parseCoordinate(args[4], executionLocation != null ? executionLocation.getZ() : 0));
        } catch (NumberFormatException e) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Неверный формат координат.");
            return;
        }

        if (executionLocation == null && (args[2].startsWith("~") || args[3].startsWith("~") || args[4].startsWith("~"))) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Относительные координаты (~) нельзя использовать из консоли. Укажите абсолютные.");
            return;
        }

        World world;
        if (args.length >= 8) {
            world = Bukkit.getWorld(args[7]);
            if (world == null) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Мир не найден: " + args[7]);
                return;
            }
        } else {
            world = executionLocation != null
                    ? executionLocation.getWorld()
                    : Bukkit.getWorlds().getFirst();
        }

        Location location = new Location(world, x, y, z);

        float distance;
        try {
            distance = Float.parseFloat(args[5]);
            if (distance <= 0) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Дистанция должна быть положительной.");
                return;
            }
        } catch (NumberFormatException e) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Неверный формат дистанции.");
            return;
        }

        int volume;
        try {
            volume = Integer.parseInt(args[6]);
            if (volume <= 0) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Громкость должна быть положительной.");
                return;
            }
        } catch (NumberFormatException e) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Неверный формат громкости.");
            return;
        }

        try {
            if (voicechatManager.getLocational(location) == null) {
                voicechatManager.createAudioPlayer(location, distance);
            }

            String filePath = soundFile.toAbsolutePath().toString();
            voicechatManager.loadTrack(filePath, location, volume);

            LegacyTextHandler.sendFormattedMessage(sender, "<green>Звук воспроизводится в точке " + LegacyTextHandler.formatLocation(location));
        } catch (Exception e) {
            plugin.logger.severe("Error playing 3D sound at " + location + ": " + e.getMessage());
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Произошла ошибка при воспроизведении звука.");
        }
    }

    private void handleStopSound(CommandSender sender, @Nullable Location executionLocation, String[] args) {
        if (args.length < 2) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Использование: /customsounds stopsound <name|all|<x> <y> <z>>");
            return;
        }

        String arg1 = args[1].toLowerCase();
        if (arg1.equals("all")) {
            voicechatManager.stopAll();
            LegacyTextHandler.sendFormattedMessage(sender, "<green>Все звуки остановлены.");
            return;
        }

        if (args.length >= 4) {
            // Coordinates
            if (executionLocation == null && (args[1].startsWith("~") || args[2].startsWith("~") || args[3].startsWith("~"))) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Относительные координаты (~) нельзя использовать из консоли. Укажите абсолютные.");
                return;
            }

            try {
                double x = Math.floor(parseCoordinate(args[1], executionLocation != null ? executionLocation.getX() : 0));
                double y = Math.floor(parseCoordinate(args[2], executionLocation != null ? executionLocation.getY() : 0));
                double z = Math.floor(parseCoordinate(args[3], executionLocation != null ? executionLocation.getZ() : 0));

                World world = executionLocation != null
                        ? executionLocation.getWorld()
                        : Bukkit.getWorlds().get(0);

                Location location = new Location(world, x, y, z);
                voicechatManager.stopByLocation(location);
                LegacyTextHandler.sendFormattedMessage(sender, "<green>Звук в " + LegacyTextHandler.formatLocation(location) + " остановлен.");
            } catch (NumberFormatException e) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Неверный формат координат.");
            }
        } else {
            // Name
            String name = args[1];
            voicechatManager.stopByName(name);
            LegacyTextHandler.sendFormattedMessage(sender, "<green>Звук '" + name + "' остановлен.");
        }
    }

    private double parseCoordinate(String arg, double defaultValue) throws NumberFormatException {
        if (arg.startsWith("~")) {
            if (arg.length() == 1) {
                return defaultValue;
            } else {
                double offset = Double.parseDouble(arg.substring(1));
                return defaultValue + offset;
            }
        } else {
            return Double.parseDouble(arg);
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack source, String[] args) {
        if (args.length == 1) {
            return List.of("playentity","play3d", "play", "stopsound");
        }

        if (args.length >= 2) {
            String subcommand = args[0].toLowerCase();

            return switch (subcommand) {
                case "playentity" -> suggestPlayEntity(args);
                case "play3d" -> suggestPlay3D(args);
                case "play" -> suggestPlay(args);
                case "stopsound" -> suggestStopSound(args);
                default -> List.of();
            };
        }

        return List.of();
    }

    private Collection<String> suggestPlayEntity(String[] args) {
        return switch (args.length) {
            case 2 -> {
                try {
                    yield Files.list(plugin.getDataFolder().toPath())
                            .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".mp3"))
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toList());
                } catch (IOException e) {
                    plugin.logger.warning("Не удалось получить список файлов: " + e.getMessage());
                    yield List.of();
                }
            }
            case 3 -> Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
            case 4 -> List.of("16", "32", "64", "128");
            case 5 -> List.of("50", "100", "150", "200");
            default -> List.of();
        };
    }

    private Collection<String> suggestPlay(String[] args) {
        return switch (args.length) {
            case 2 -> {
                try {
                    yield Files.list(plugin.getDataFolder().toPath())
                            .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".mp3"))
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toList());
                } catch (IOException e) {
                    plugin.logger.warning("Не удалось получить список файлов: " + e.getMessage());
                    yield List.of();
                }
            }
            case 3 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            case 4 -> List.of("50", "100", "150", "200");
            default -> List.of();
        };
    }

    private Collection<String> suggestPlay3D(String[] args) {
        return switch (args.length) {
            case 2 -> {
                try {
                    yield Files.list(plugin.getDataFolder().toPath())
                            .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".mp3"))
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toList());
                } catch (IOException e) {
                    plugin.logger.warning("Не удалось получить список файлов: " + e.getMessage());
                    yield List.of();
                }
            }
            case 3, 4, 5 -> List.of("~", "~0", "~1", "~10");
            case 6 -> List.of("16", "32", "64", "128");
            case 7 -> List.of("50", "100", "150", "200");
            case 8 -> Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .collect(Collectors.toList());
            default -> List.of();
        };
    }

    private Collection<String> suggestStopSound(String[] args) {
        if (args.length == 2) {
            return List.of("all", "<x>");  // Улучшено
        } else if (args.length == 3) {
            return List.of("<y>");
        } else if (args.length == 4) {
            return List.of("<z>");
        }
        return List.of();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission(permission());
    }

    @Override
    public @Nullable String permission() {
        return "customsounds.use";
    }
}