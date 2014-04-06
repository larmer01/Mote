package edu.missouristate.mote.propertygrid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;

/**
 * Provide a method for loading an object into a collection of PropertyGridRow
 * objects.
 */
public final class ObjectLoader {

    // *************************************************************************
    // CONSTRUCTOR
    // *************************************************************************

    /**
     * Initialize a new instance of an ObjectLoader.
     */
    private ObjectLoader() {
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************

    /**
     * Return a mapping of {base name: category name} for all getters with
     * category annotations.
     *
     * @param object object to examine
     * @return mapping of base name to category names
     */
    private static Map<String, String> getCategories(final Object object) {

        final HashMap<String, String> result = new HashMap<>();
        final Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            final String key = method.getName().substring(3);
            // Only the annotations on the get methods are used
            if (isGetter(method)) {
                Annotation ann = method.getAnnotation(CategoryAnnotation.class);
                if (ann instanceof CategoryAnnotation) {
                    result.put(key, ((CategoryAnnotation) ann).value());
                }
            }
        }
        return result;
    }

    /**
     * Return a list of base method names (name excluding the get/set prefix) in
     * an order specified by the index annotation of the get methods.
     *
     * @param object object to examine
     * @return list of base method names in order
     */
    private static ArrayList<String> getMethodOrder(final Object object) {
        // Use a tree to order since we do not know how many methods we will be
        // retrieving via reflection or what order they will be retrieved in
        final TreeMap<Integer, String> ordered = new TreeMap<>();
        final Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            final String key = method.getName().substring(3);
            // Only the annotations on the get methods are used
            if (isGetter(method)) {
                Annotation ann = method.getAnnotation(IndexAnnotation.class);
                if (ann instanceof IndexAnnotation) {
                    int index = ((IndexAnnotation) ann).value();
                    ordered.put(index, key);
                }
            }
        }
        // Convert the tree's values in a list and return
        final ArrayList<String> result = new ArrayList<>();
        result.addAll(ordered.values());
        return result;
    }

    /**
     * Return a mapping of {base name: [get method, set method], ...} where each
     * value is a two-element array with the getter at index 0 and the setter at
     * index 1 (or null if there is no setter).
     *
     * @param object object to examine
     * @return mapping of base name to getter/setter methods
     */
    private static HashMap<String, Method[]> getMethodPairs(
            final Object object) {
        final HashMap<String, Method[]> result = new HashMap<>();
        final Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            final String key = method.getName().substring(3);
            if (!result.containsKey(key)) {
                result.put(key, new Method[]{null, null});
            }
            if (isGetter(method)) {
                result.get(key)[0] = method;
            } else if (isSetter(method)) {
                result.get(key)[1] = method;
            }
        }
        return result;
    }

    /**
     * Return true if the specified method is a getter; false, otherwise.
     *
     * @param method method to check
     * @return True if the method is a getter; false, otherwise.
     */
    private static boolean isGetter(final Method method) {
        if (!method.getName().startsWith("get")) {
            return false;
        } else if (method.getParameterTypes().length != 0) {
            return false;
        } else if (void.class.equals(method.getReturnType())) {
            return false;
        }
        return true;
    }

    /**
     * Return true if the specified method is a setter; false, otherwise.
     *
     * @param method method to check
     * @return True if the method is a setter; false, otherwise.
     */
    private static boolean isSetter(final Method method) {
        if (!method.getName().startsWith("set")) {
            return false;
        } else if (method.getParameterTypes().length != 1) {
            return false;
        }
        return true;
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    /**
     * Return a list of PropertyGridRow objects for the specified object.
     *
     * @param object object to examine
     * @return List of PropertyGridRow objects
     */
    public static ArrayList<PropertyTableRow> getRows(final Object object) {

        final ArrayList<PropertyTableRow> result = new ArrayList<>();
        final Map<String, String> categories = getCategories(object);
        final ArrayList<String> methodOrder = getMethodOrder(object);
        final HashMap<String, Method[]> pairs = getMethodPairs(object);
        String currentCategory = "";
        for (String key : methodOrder) {
            PropertyTableRow row = new PropertyTableRow();
            // Retrieve the category
            if (categories.containsKey(key)) {
                row.setCategory(categories.get(key));
            }
            // Add a new category row
            if (!currentCategory.equals(row.getCategory())) {
                row.setName(row.getCategory());
                row.setReadonly(true);
                result.add(row);
                currentCategory = row.getCategory();
                row = new PropertyTableRow();
            }
            // Getter and setter
            row.setGetter(pairs.get(key)[0]);
            row.setSetter(pairs.get(key)[1]);
            final Method getter = pairs.get(key)[0];
            // Category
            row.setCategory(currentCategory);
            // Name
            Annotation ann = getter.getAnnotation(NameAnnotation.class);
            if (ann instanceof NameAnnotation) {
                row.setName(((NameAnnotation) ann).value());
            }
            // Description
            ann = getter.getAnnotation(DescriptionAnnotation.class);
            if (ann instanceof DescriptionAnnotation) {
                row.setDescription(((DescriptionAnnotation) ann).value());
            }
            // Read-only
            row.setReadonly(pairs.get(key)[1] == null);
            result.add(row);
        }
        return result;
    }
}
