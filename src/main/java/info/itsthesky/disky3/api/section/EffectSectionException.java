package info.itsthesky.disky3.api.section;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.skript.log.*;
import ch.njol.util.Kleenean;
import ch.njol.util.StringUtils;
import info.itsthesky.disky3.api.ReflectionUtils;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A class to allow you to create effects that you can run its section.
 *
 * @author Tuke_Nuke on 29/03/2017
 */
public abstract class EffectSectionException extends Condition {
    protected static HashMap<Class<? extends EffectSectionException>, EffectSectionException> map = new HashMap<>();
    protected SectionNode section = null;
    private TriggerSection trigger = null;
    private boolean hasIfOrElseIf = false;
    private boolean executeNext = true;
    private Node node;

    public EffectSectionException() {
        /*if (this instanceof LazyEffectSection) //This one doesn't need to load the section separated.
            return;*/
        Node n = SkriptLogger.getNode(); //Skript sets the node before parsing this 'effect'
        node = n;
        if (!(n instanceof SectionNode)) //Check in case it wasn't loaded as inline condition
            return;
        //True if it was used as condition
        hasIfOrElseIf = StringUtils.startsWithIgnoreCase(n.getKey(), "if ") || StringUtils.startsWithIgnoreCase(n.getKey(), "else if ");
        //The comment value of a note is protected, so it is needed but not really necessary tho.
        //It doesn't make difference, it's just to make a exactly copy.
        String comment = ReflectionUtils.getField(Node.class, n, "comment");
        if (comment == null)
            comment = "";
        //Creating a copy of current node.
        section = new SectionNode(n.getKey(), comment, n.getParent(), n.getLine());
        //It will copy the "ArrayList<Node> nodes" field too as it is protected.
        ReflectionUtils.setField(SectionNode.class, section, "nodes", ReflectionUtils.getField(SectionNode.class, n, "nodes"));
        //Then it will clear the nodes from the current node, so Skript won't parse it (you need to parse then later).
        ReflectionUtils.setField(SectionNode.class, n, "nodes", new ArrayList<Node>());
    }

    @SuppressWarnings("unchecked")
    public static boolean isCurrentSection(Class<? extends EffectSectionException>... classes) {
        return getCurrentSection(classes) != null;
    }

    public static void register(
            Class<? extends EffectSectionException> clazz,
            String pattern
    ) {
        Skript.registerCondition(clazz, pattern);
    }

    public Node getNode() {
        return node;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EffectSectionException> T getCurrentSection(Class<? extends EffectSectionException>... classes) {
        for (Class<? extends EffectSectionException> clz : classes) {
            T result = (T) map.get(clz);
            if (result != null)
                return result;
        }
        return null;
    }

    /**
     * A hacky method to fix wrong syntax inside of sections not being included in errors.
     * Why? Because before parsing the effect itself, Skript starts a ParseLogHandler, then,
     * in case the syntax returns true in {@link #init(Expression[], int, Kleenean, SkriptParser.ParseResult)},
     * The LogHander will ignore all errors that was sent in this method.
     * So to fix that, it stops the lasts ParseLogHandlers to not conflict with.
     *
     * @param logger - RetainingLogHandler used to parse the section.
     */
    public static void stopLog(RetainingLogHandler logger) {
        //Stop the current one
        logger.stop();
        //Using reflection to access the iterator of handlers
        HandlerList handler = SkriptAdapter.getInstance().getHandlers();
        if (handler == null)
            return;
        Iterator<LogHandler> it = handler.iterator();
        //A list containing the last handlers that will be stopped
        List<LogHandler> toStop = new ArrayList<>();
        while (it.hasNext()) {
            LogHandler l = it.next();
            if (l instanceof ParseLogHandler)
                toStop.add(l);
            else //We can only stop the lasts handler, this prevent in case the last is not what we want.
                break;
        }
        toStop.forEach(LogHandler::stop); //Stopping them
        SkriptLogger.logAll(logger.getLog()); //Sending the errors to Skript logger.
    }

    /**
     * It is to replicate [@link ch.njol.skript.lang.Effect#execute(Event)]
     *
     * @param e - The Event
     */
    protected abstract void execute(Event e);

    @Override
    public boolean check(Event e) {
        execute(e);
        if (executeNext && trigger != null)
            setNext(trigger.getNext());
        //It needs to return false to not enter inside the section
        //And return true in case it is inline condition, so the code
        //can continue.
        return !hasSection();
    }

    /**
     * It will load the section of this if any. It must be used before {@link #runSection(Event)}.
     *
     * @param setNext - Set the next trigger of this loading section, to actual next of this effect.
     */
    public void loadSection(boolean setNext, BiConsumer<Exception, Event> exceptionConsumer) {
        if (section != null) {
            RetainingLogHandler errors = SkriptLogger.startRetainingLog();
            EffectSectionException previous = map.put(getClass(), this);
            try {
                trigger = new TriggerSection(section) {

                    @Override
                    public String toString(Event event, boolean b) {
                        return EffectSectionException.this.toString(event, b);
                    }

                    @Override
                    public TriggerItem walk(Event event) {
                        try {
                            return walk(event, true);
                        } catch (Exception ex) {
                            exceptionConsumer.accept(ex, event);
                        }
                        return null;
                    }
                };
                if (setNext) {
                    trigger.setNext(getNext());
                    setNext(null);
                }
            } finally {
                stopLog(errors);
            }
            map.put(getClass(), previous);
            //Just to not keep a instance of SectionNode.
            section = null;
        }
    }

    /**
     * It will load the section of this if any and then it will parse as in specific event.
     * Basically it will call {@link ScriptLoader#setCurrentEvent(String, Class[])}, parse the current section,
     * and then set the current event back to the previous one.
     * Useful to load a code from event X and parse as Y, allowing to use syntaxes that work on it.
     *
     * @param name    - The name of event (It can be anything)
     * @param setNext - Set the next trigger of this loading section, to actual next of this effect.
     * @param events  - The classes that extends {@link Event}.
     */
    @SafeVarargs
    public final void loadSection(String name, boolean setNext, BiConsumer<Exception, Event> exceptionConsumer, Class<? extends Event>... events) {
        if (section != null && name != null && events != null && events.length > 0) {
            String previousName = ScriptLoader.getCurrentEventName();
            Class<? extends Event>[] previousEvents = ScriptLoader.getCurrentEvents();
            Kleenean previousDelay = SkriptAdapter.getInstance().getHasDelayedBefore();
            ScriptLoader.setCurrentEvent(name, events);
            loadSection(setNext, exceptionConsumer);
            ScriptLoader.setCurrentEvent(previousName, previousEvents);
            SkriptAdapter.getInstance().setHasDelayedBefore(previousDelay);
        }
    }

    /**
     * Check if this has any section (basically check if it is inline condition or Condtional)
     *
     * @return True if it has
     */
    public boolean hasSection() {
        return section != null || trigger != null;
    }

    /**
     * Run the section.
     * <b>Note</b>: You must {@link #loadSection(boolean)} first and you should run it with same
     * event from {@link #execute(Event)}
     *
     * @param e - The event
     */
    protected void runSection(Event e) {
        executeNext = false;
        TriggerItem.walk(trigger, e);
    }

    /**
     * It will check in case the effect wasn't used with 'if/else if' before
     * <code>
     * do something:
     * send "Everything fine"
     * if do something:
     * send "Not ok, it will send a default message and return false"
     * </code>
     * It needs to be used in {@link #init(Expression[], int, Kleenean, SkriptParser.ParseResult)}
     * method, like:
     * <code>
     * public boolean init(...) {
     * if (checkIfCondition()) { //It will send a error if true
     * return false; //Then return false to not continue the code
     * }
     * //continue here
     * }
     * </code>
     *
     * @return True if the EffectSection was used as condition in if/else if
     */
    public boolean checkIfCondition() {
        if (hasIfOrElseIf)
            Skript.error("You can't use the effect in if/else if section.");
        return hasIfOrElseIf;
    }

    /**
     * The section node of {@link EffectSectionException}.
     * It can return null if it was used after {@link #loadSection(boolean)} or
     * if this doesn't have any section.
     *
     * @return The SectionNode
     */
    public SectionNode getSectionNode() {
        return section;
    }
}
