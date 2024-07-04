package ru.kochkaev.Seasons4Fabric.Objects;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Service.Weather;
import ru.kochkaev.Seasons4Fabric.WeatherDamageType;

import java.util.List;

public class EffectObject {

    /**
     *  EffectObject
     *  It's object for create effects
     */

    /** triggerMessage sends to chat on set available weather */
    public String triggerMessage;
    /** getMessage (optional) you can send to chat when player gets this effect */
    public String getMessage;
    /** removeMessage (optional) you can send to chat when player stopped fulfilling conditions in logic(ServerPlayerEntity player, int countOfInARowCalls) */
    public String removeMessage;
    /** isGood true if effect gives a buff | false if effect gives a debuff */
    public boolean isGood;
    /** weathers contains weathers, that available this effect   */
    public List<Weather> weathers;

    /**
     *  Effect meta registration
     *  Contains effect data (langKey, isGood, weathers)
     *  Usage:
     *
     *  YourClass() {
     *      this.langKey = "yourLangKey";
     *      this.isGood = true;
     *      this.weathers = Arrays.asList(Weather.getWeatherByID("FIRST_WEATHER_ID"), Weather.getWeatherByID("SECOND_WEATHER_ID"));
     *  }
     */

    public String getTriggerMessage() { return this.triggerMessage; }
    public boolean isGood() { return this.isGood; }
    public List<Weather> getWeathers() { return this.weathers; }

    /**
     *  Will be called every "conf.tick.secondsPerTick" (from config.txt) seconds
     *  Contains effect logic for one thing player
     *  Usage:
     *
     *  @Override
     *  public void logic(ServerPlayerEntity player, int countOfInARowCalls) {
     *      // Your effect logic
     *  }
     */
    public void logic(ServerPlayerEntity player, int countOfInARowCalls) {
        player.damage(WeatherDamageType.of(player.getServerWorld(), WeatherDamageType.CUSTOM_DAMAGE_TYPE), 1.0f);
    }

    public boolean isAllowed() {
        return weathers.contains(Weather.getCurrent());
    }

    /**
     *  Effect example:
     *
     *  import ru.kochkaev.Seasons4Fabric.Service.Weather;
     *  import ru.kochkaev.Seasons4Fabric.Ticker;
     *  public class ExampleEffect extends EffectObject{
     *      ExampleEffect() {
     *          this.triggerMessage = "Today, iron very hot!";
     *          this.getMessage = "Auch!";
     *          this.removeMessage = "You shouldn't hold iron in your hand.";
     *          this.isGood = false;
     *          this.weathers = Array.asList(Weather.getWeatherByID("HOT"), Weather.getWeatherByID("SCORCHING"));
     *      }
     *      @Override
     *      public void logic(ServerPlayerEntity player, int countOfInARowCalls) {
     *          if (player.getActiveItem() == Items.IRON_INGOT) {
     *              player.sendMessage(this.getMessage());
     *              if (countOfInARowCalls > 1) player.damage(WeatherDamageType.of(player.getServerWorld(), WeatherDamageType.CUSTOM_DAMAGE_TYPE), 1.0f);
     *              countOfInARowCalls++;
     *          } else if (countOfInARowCalls > 0) {
     *              player.sendMessage(this.removeMessage());
     *              countOfInARowCalls = 0;
     *          }
     *      }
     *  }
     */

}
