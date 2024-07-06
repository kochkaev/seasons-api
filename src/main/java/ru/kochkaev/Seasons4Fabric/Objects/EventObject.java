package ru.kochkaev.Seasons4Fabric.Objects;

import ru.kochkaev.Seasons4Fabric.Service.Event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * It's EventObject, object for create your own events.<br><br>
 * For create event you must extend EventObject.<br>
 * {@code public class YourClass extends EventObject { ... }}
 *
 * @version 1.0
 * @author Dmitrii Kochkaev
 * <p>
 *     <a href="https://t.me/kleverdi">Telegram channel</a>
 *     <a href="https://youtube.com/@kochkaev">YouTube channel</a>
 *     <a href="https://vk.com/kleverdi">VK</a>
 *     <a href="https://github.com/kochkaev">GitHub</a>
 *     <a href="https://gitverse.ru/kochkaev">GitVerse</a>
 * </p>
 */
public abstract class EventObject {

    /** It's list of methods for invoke on event. */
    private final List<Consumer<Object>> methods = new ArrayList<>();

    /** It's object, returned by method */
    private Object returned;
    private boolean isReturned = false;

    private boolean isCancelled= false;

    /** It's event id.
     * You must assign event id in your effect. You can use constructor for this: <br>
     * <code>
     *     YourClass() {
     *         super("EVENT_ID");
     *     }
     * </code>
     * @see #getEventID()
     */
    protected final String eventId;

    protected EventObject(String eventId)  {
        this.eventId = eventId;
    }

    /**
     * You can use this method to add method for invoke on event.
     * @param method method for invoke on event.
     */
    public void addMethod(Consumer<Object> method) {
        methods.add(method);
    }

    /**
     * This method will be called on event. <br>
     * You must realize this method in your event. Use {@code @Override} annotation for this method. <br>
     * Use {@link #invokeMethods(Object... args)} for invoke methods.<br>
     * {@code invokeMethods(args)}<br><br>
     * You can invoke this method from your mixin:<br>
     * {@code Event.getEventByID("EVENT_ID").onEvent(yourArgumentIfItsNeeded, yourSecondOptArg);}
     *
     * @param args additional arguments for invoke on event.
     */
    public abstract void onEvent(Object... args);

    /**
     * You can use this method to invoke methods. <br>
     * For add method for invoke on event you can use {@link #addMethod(Method method)}.
     * @param args additional arguments for invoke on event.
     */
    protected void invokeMethods(Object... args)  {
        for (Consumer<Object> method : methods) {
            try {
                method.accept(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * You can use this method to return any object from method.
     * @param returned returned by method Object.
     */
    public void returnValue(Object returned) {
        this.returned = returned;
        this.isReturned = true;
    }

    /**
     * You can use this method to cancel event.
     */
    public void cancelEvent() {
        this.isCancelled = true;
    }

    /** @return object, returned by method. */
    public Object getReturned() { return this.returned; }

    /** @return true if method returned any object. */
    public boolean isReturned()  { return this.isReturned; }

    /** @return true if event was canceled by method. */
    public boolean isCancelled()  { return this.isCancelled; }
    /** Returns {@link #isCancelled} and set it false (reset)
     *  @return true if event was canceled by method.
     */
    public boolean isCancelledAndReset() {
        boolean isCancelled = this.isCancelled;
        this.isCancelled = false;
        return isCancelled;
    }

    /**
     * You can use this method for get event id.
     * @return this event id.
     */
    public String getEventID() { return eventId; }
}