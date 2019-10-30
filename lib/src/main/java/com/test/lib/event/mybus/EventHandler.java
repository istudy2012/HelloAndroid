package com.test.lib.event.mybus;

import android.util.Log;

import com.test.lib.log.HLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventHandler {
    private static final Map<Class, List<EventItem>> eventMap = new HashMap<>();
    private static final Set<Object> objectSet = new HashSet<>();

    public static void register(Object object) {
        if (object == null) {
            return;
        }

        if (objectSet.contains(object)) {
            return;
        }

        List<EventItem> methodList = getEventItems(object);
        for (EventItem methodItem : methodList) {
            if (eventMap.containsKey(methodItem.paramCls)) {
                eventMap.get(methodItem.paramCls).add(methodItem);
            } else {
                List<EventItem> newItems = new ArrayList<>();
                newItems.add(methodItem);
                eventMap.put(methodItem.paramCls, newItems);
            }
        }
        objectSet.add(object);
    }

    public static void unregister(Object object) {
        if (object == null) {
            return;
        }

        if (!objectSet.contains(object)) {
            return;
        }

        List<Class> eventClassList = getEventClassList(object);
        for (Class eventClass : eventClassList) {
            if (eventMap.containsKey(eventClass)) {
                List<EventItem> eventItems = eventMap.get(eventClass);
                if (eventItems != null && !eventItems.isEmpty()) {
                    Iterator<EventItem> iterable = eventItems.iterator();
                    while (iterable.hasNext()) {
                        EventItem item = iterable.next();
                        if (item.object == object) {
                            iterable.remove();
                        }
                    }
                }
            }
        }
        objectSet.remove(object);
    }

    public static void sendEvent(Object object) {
        Log.d("test", "sendEvent start: ");
        long startTime = System.currentTimeMillis();

        if (eventMap.containsKey(object.getClass())) {
            List<EventItem> eventItems = eventMap.get(object.getClass());
            for (EventItem item : eventItems) {
                item.invoke(object);
            }
        }

        Log.d("test", "sendEvent end: " + (System.currentTimeMillis() - startTime));
    }

    private static List<EventItem> getEventItems(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        List<EventItem> eventMethods = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Event.class)) {
                Class[] params = method.getParameterTypes();
                if (params.length == 1) {
                    EventItem item = new EventItem(object, method, params[0]);
                    eventMethods.add(item);
                }
            }
        }

        return eventMethods;
    }

    private static List<Class> getEventClassList(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        List<Class> eventClassList = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Event.class)) {
                Class[] params = method.getParameterTypes();
                if (params.length == 1) {
                    eventClassList.add(params[0]);
                }
            }
        }

        return eventClassList;
    }

    public static class EventItem {
        public Object object;
        public Method method;
        public Class paramCls;

        public EventItem(Object object, Method method, Class paramCls) {
            this.object = object;
            this.method = method;
            this.paramCls = paramCls;
        }

        public void invoke(Object event) {
            try {
                method.invoke(object, event);
            } catch (IllegalAccessException e) {
                HLog.d("event", "IllegalAccessException: " + e.getMessage());
            } catch (InvocationTargetException e) {
                HLog.d("event", "InvocationTargetException: " + e.getMessage());
            }
        }
    }

}
