import java.util.*;

/** A DBRecord instance contain a database record. The record is represented by key-value descriptions.
 * The constructor reads in a sentence and build internal data structure. The content of DBRecord is final.
 * Client could read the content or provide (part of) a sentence to see if it matches the DBRecord.
 */
public class DBRecord {
    /** defines the inner key-value data structure to represents the DBRecord
     *
     */
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

    private final ArrayList<DBBinding> Record;
    private boolean selected = false;

    /** constructs a DBRecord form a sentence which contains multiple key-value pairs to describe the record
     * @param record the description input
     */
    public DBRecord(String record) {
        Record = new ArrayList<>();
        char[] txt = record.toCharArray();
        StringBuilder buffer = new StringBuilder();
        String key = null, value;
        for (int i = 0; i < txt.length; i++) {
            if (txt[i] == ':') {
                key = buffer.toString();
                buffer = new StringBuilder();
            }
            else if (txt[i] == ',') {
                value = buffer.toString();
                DBBinding pair = new DBBinding(key, value);
                Record.add(pair);
                buffer = new StringBuilder();
                i += 1;
            }
            else {
                buffer.append(txt[i]);
            }
        }
        // add the last pair
        value = buffer.toString();
        DBBinding pair = new DBBinding(key, value);
        Record.add(pair);
    }

    /**
     * @return true if this DBRecord is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * set the DBRecord in the status of selected
     */
    public void setSelected() {
        selected = true;
    }

    /**
     * set the DBRecord in the status of un-selected
     */
    public void clearSelect() {
        selected = false;
    }

    /** check if the searching criteria matches any of the DBBinding storing in this BDRecord
     * @param sKey the searching key
     * @param sValue the searching value
     * @return true if the record matches
     */
    public boolean isHit(String sKey, String sValue) {
        for (DBBinding b : Record) {
            if (!b.matchKey(sKey)) {
                continue;
            }
            else if (b.containsValue(sValue)){
                return true;
            }
        }
        return false;
    }

    public List<DBBinding> getBindings() {
        return (List<DBBinding>) Record.clone();
    }

    /** returns the character representation of the DBRecord
     */
    public String toString() {
        StringBuilder outBuffer = new StringBuilder();
        if (selected) {
            outBuffer.append('*');
        }
        for (DBBinding pair : Record) {
            outBuffer.append(pair);
            outBuffer.append(", ");
        }
        outBuffer.deleteCharAt(outBuffer.length() - 1);
        outBuffer.deleteCharAt(outBuffer.length() - 1);
//        outBuffer.append('\n');
        return outBuffer.toString();
    }
}
