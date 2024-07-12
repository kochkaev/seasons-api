package ru.kochkaev.Seasons4Fabric.object;

import ru.kochkaev.Seasons4Fabric.IFunc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
    private final List<IFunc> methods = new ArrayList<>();

    /** It's object, returned by method */
    private Object returned;
    private boolean isReturned = false;

    private boolean isCancelled= false;

    /** It's event id.
     * You must assign event id in your event. You can use constructor for this: <br>
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
    public void addMethod(IFunc method) {
        methods.add(method);
    }

    /**
     * This method will be called on event. <br>
     * You must realize this method in your event. Use {@code @Override} annotation for this method. <br>
     * Use {@link #invokeMethods(List args)} for invoke methods.<br>
     * {@code invokeMethods(args)}<br><br>
     * You can invoke this method from your mixin:<br>
     * {@code Event.getEventByID("EVENT_ID").onEvent(yourArgumentIfItsNeeded, yourSecondOptArg);}
     *
     * @param args additional arguments for invoke on event.
     */
    public abstract void onEvent(List<Object> args);

    /**
     * You can use this method to invoke methods. <br>
     * For add method for invoke on event you can use {@link #addMethod(IFunc method)}.
     * @param args additional arguments for invoke on event.
     */
    protected void invokeMethods(List<Object> args)  {
        for (IFunc method : methods) {
            try {
                method.function(args);
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
    /** Returns {@link #isReturned} and set it false (reset)
     *  @return true if method was returned any Object.
     */
    public boolean isReturnedAndReset()  {
        boolean isReturned= this.isReturned;
        this.isReturned = false;
        return isReturned;
    }

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