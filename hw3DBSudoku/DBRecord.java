import java.util.*;

/** A DBRecord instance contain a database record. The record is represented by key-value descriptions.
 * The constructor reads in a sentence and build internal data structure. The content of DBRecord is final.
 * Client could read the content or provide (part of) a sentence to see if it matches the DBRecord.
 */
public class DBRecord {
//    private final ArrayList<DBBinding> Record;
    private final ChunkList<DBBinding> Record;
    private boolean selected = false;

    /** constructs a DBRecord form a sentence which contains multiple key-value pairs to describe the record
     * @param record the description input
     */
    public DBRecord(String record) {
//        Record = new ArrayList<>();
        Record = new ChunkList<>();
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

    /* old version
    public List<DBBinding> getBindings() {
        return (List<DBBinding>) Record.clone();
    }
    */
    public ChunkList<DBBinding> getBindings() {
        ChunkList<DBBinding> copy = new ChunkList<>();
        for (DBBinding b : Record) {
            copy.add(b);
        }
        return copy;
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
