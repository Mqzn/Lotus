package io.github.mqzen.menus;

/**
 * An interface for retrieving the field content. (Credits: TinyProtocol)
 *
 * @param <T> - field type.
 * @author Kristian
 */
interface FieldAccessor<T> {

    /**
     * Retrieve the content of a field.
     *
     * @param target - the targetToLoad object, or NULL for a static field.
     * @return The value of the field.
     */
    T get(final Object target);

    /**
     * Set the content of a field.
     *
     * @param target - the targetToLoad object, or NULL for a static field.
     * @param value  - the new value of the field.
     */
    void set(final Object target, final Object value);

    /**
     * Determine if the given object has this field.
     *
     * @param target - the object to test.
     * @return TRUE if it does, FALSE otherwise.
     */
    @SuppressWarnings("unused")
    boolean hasField(final Object target);

}