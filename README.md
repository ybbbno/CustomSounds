# CustomSounds

A Paper plugin that plays custom audio files (MP3 and other formats supported by LavaPlayer) through **Simple Voice Chat**.

Sounds can be played:
- To a specific player (static / non-positional)
- At a 3D location in the world
- Attached to an entity

## Requirements

- Paper 1.21+
- [Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat) (required)

## Commands

Permission: `customsounds.use` (default: OP)

### `/customsounds play <filename> <player> <volume>`

Plays a sound only for the specified player (static audio channel).

| Argument   | Description                          |
|------------|--------------------------------------|
| `filename` | Sound file name from the data folder |
| `player`   | Target player name                   |
| `volume`   | Volume (e.g. `50`, `100`, `150`)     |

**Example:**
```
/customsounds play music.mp3 Notch 100
```

### `/customsounds play3d <filename> <x> <y> <z> <distance> <volume> [world]`

Plays a positional 3D sound at the given coordinates.

| Argument   | Description                                      |
|------------|--------------------------------------------------|
| `filename` | Sound file name from the data folder             |
| `x y z`    | Coordinates (supports `~` relative coordinates)  |
| `distance` | Hearing distance                                 |
| `volume`   | Volume                                           |
| `world`    | Optional world name                              |

**Examples:**
```
/customsounds play3d explosion.mp3 ~ ~ ~ 32 100
/customsounds play3d music.mp3 100 64 -200 64 150 world
```

### `/customsounds playentity <filename> <player> <distance> <volume>`

Plays a sound attached to a player (entity audio channel). The sound follows the player.

| Argument   | Description                          |
|------------|--------------------------------------|
| `filename` | Sound file name from the data folder |
| `player`   | Target player                        |
| `distance` | Hearing distance                     |
| `volume`   | Volume                               |

**Example:**
```
/customsounds playentity footsteps.mp3 Steve 16 80
```

### `/customsounds stopsound <name|all|<x> <y> <z>>`

Stops currently playing sounds.

| Argument     | Description                              |
|--------------|------------------------------------------|
| `all`        | Stop every sound                         |
| `<name>`     | Stop sound by internal name              |
| `<x> <y> <z>`| Stop locational sound at coordinates     |

**Examples:**
```
/customsounds stopsound all
/customsounds stopsound static-Notch
/customsounds stopsound ~ ~ ~
```

## Permissions

```yaml
customsounds.use:
  description: Allows use of /customsounds commands
  default: op
```

## API for other plugins

```java
CustomSoundsAPI api = CustomSoundsAPI.api();
if (api != null) {
    VoicechatManager manager = api.manager();

    // Create players
    manager.createAudioPlayer(player);                          // static
    manager.createAudioPlayer(location, distance);              // locational
    manager.createAudioPlayer(entity, distance);                // entity

    // Load & play track
    manager.loadTrack(filePath, player, volume);
    manager.loadTrack(filePath, location, volume);
    manager.loadTrack(filePath, entity, volume);

    // Stop / destroy
    manager.stopByPlayer(player);
    manager.stopByLocation(location);
    manager.stopByEntity(entity);
    manager.stopAll();
}
```

You can also listen to VoiceChat lifecycle events by implementing `VoicechatLifecycleListener` and registering it with `manager.addListener(...)`.

## Notes

- Only local files from the plugin data folder are supported (LavaPlayer local source).
- Relative coordinates (`~`) work only when the command is executed by a player or command block.
- Audio channels are automatically managed. Re-using the same target (player / location / entity) will reuse the existing channel when possible.

