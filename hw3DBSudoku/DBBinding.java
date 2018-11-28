public class DBBinding {
    private final String key;
    private final String value;

    /** build a key-value pair, use two String as inputs
     * inputs could not be null
     * @param key describe the what kind of value it is
     * @param value is the content of the item
     */
    public DBBinding(String key, String value) {
        if (key == null || key.length() == 0 || value == null || value.length() == 0) {
            throw new java.lang.RuntimeException("any of the inputs should not be empty");
        }
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key of this Binding
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @return the value of this binding
     */
    public String getValue() {
        return this.value;
    }

    /** checks if this DBBinding is the same kind of the one to be searched
     * @param sKey the key be searched
     * @return true if the search key matches the key
     */
    public boolean matchKey(String sKey) {
        return key.equals(sKey);
    }

    /** checks if the value contains the searching value
     * @param sValue
     * @return true if the searching key matches the entire or part of the value
     */
    public boolean containsValue(String sValue) {
        return value.toLowerCase().contains(sValue);
    }

    /** returns the characters representation of the key-value pair
     * @return
     */
    @Override
    public String toString() {
        return (key + ':' + value);
    }
}
