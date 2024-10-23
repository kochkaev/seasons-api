# Seasons API

**Seasons API** was developed for creating your own **seasons, weathers and challenges**!<br>
This mod was developed by analogy with the Spigot plugin "Seasons" specifically for the private Minecraft server "Zixa City" by its administrator (kochkaev, aka kleverdi). The idea of this mod was taken from Harieo.

## What is this?
- **Season** - a certain period of a conditional year that has a certain weather for the current conditional time.
- **Weather** - daytime conditions for the game that have their own Challenges.
- **Challenge** - a set of some sort of rules for the player, appropriate to the current season and weather.

## Command Syntax:
- `/seasons get season` - get current season.
- `/seasons get weather` - get the current weather.
- `/seasons get lang` - get the currently set language.
- `/seasons get challenges` - get the list of active Challenges.
- `/seasons set season <season>` - set season.
- `/seasons set weather <weather>` - set weather.
- `/seasons set lang <language>` - set language.
- `/seasons set challenge <forceAllow | forceDisable> <challenge>` - force start or end a challenge.
- `/seasons reload` - reload configs.

***

## For developers
You can create your own set of seasons, weathers and challenges in your mod. More information about **implementing** the Seasons API in your mod can be found in **[wiki](https://github.com/kochkaev/seasons-api/wiki)**.

***

## Additional info
### Links
- [Sources](https://github.com/kochkaev/seasons-api)
- [Issues](https://github.com/kochkaev/seasons-api/issues)
- [Modrinth](https://modrinth.com/mod/seasons-api)
### Dependencies
- [Fabric API](https://modrinth.com/mod/fabric-api) (Required)
- [Text Placeholder API](https://modrinth.com/mod/placeholder-api) (Optional)
- [Mod Menu](https://modrinth.com/mod/modmenu) (Optional)
- [Cloth Config API](https://modrinth.com/mod/cloth-config) (Optional)
### Idea by Harieo
- Harieo on GitHub: https://github.com/Harieo/
- Original plugin on GitHub: https://github.com/Harieo/Seasons/
- Original plugin on SpigotMC: https://www.spigotmc.org/resources/seasons.39298/
### Created by kochkaev
- GitHub: https://github.com/kochkaev/
- VK: https://vk.com/kleverdi/
- YouTube: https://youtube.com/@kochkaev/
- Contact email: kleverdi@vk.com

***

## Known issues
- Сan't miss a thunderstorm by sleeping in a bed;
- Incompatible with Fabric Seasons;
