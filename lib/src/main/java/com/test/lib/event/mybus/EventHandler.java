package com.test.lib.event.mybus;

import com.test.lib.log.HLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler {
    private static final Map<Class, List<EventItem>> eventMap = new HashMap<>();

    public static void register(Object object) {
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
    }

    public static void unregister(Object object) {

    }

    public static void sendEvent(Object object) {
        if (eventMap.containsKey(object.getClass())) {
            List<EventItem> eventItems = eventMap.get(object.getClass());
            for (EventItem item : eventItems) {
                item.invoke(object);
            }
        }
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
