import java.util.*;
import java.io.File;

/**
 * A simple DataBase class, a collection of DBRecords. Supporsts read, print, select in AND mode, select in OR mode
 * delete all, delete selected, delete unselected, clear operations.
 */
public class DBTable {
    private ArrayList<DBRecord> table;
    private int recordNum;
    private int selectNum;

    public DBTable() {
        table = new ArrayList<>();
        recordNum = 0;
        selectNum = 0;
    }

    /**
     * reads a file specified by the fileName and adds the records in that file to the table
     * @param fileName specify the file to be added to table
     */
    public void read(String fileName) {
        try (Scanner in = new Scanner(new File(fileName))) {
            while (in.hasNextLine()) {
                table.add(new DBRecord(in.nextLine()));
                recordNum += 1;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * print the DBTable
     */
    public void print() {
        System.out.println(this.toString());
    }

    /**
     * @return how many records in table in total
     */
    public int getRecordNum() {
        return recordNum;
    }

    /**
     * @return the number of how many records are selected
     */
    public int getSelectNum() {
        return selectNum;
    }

    /**
     * Override the character representation of DBTable
     * @return the String description of DBTable
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (DBRecord r : table) {
            buffer.append(r.toString());
            buffer.append('\n');
        }
        if (buffer.length() != 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * reads in the criteria and puts it into structure of DBRecord, then return it
     * @param sCriteria
     * @return
     */
    private DBRecord getCriteria(String sCriteria) {
        return new DBRecord(sCriteria);
    }

    /**
     * search the table in And Mode
     * @param criteria for search use
     */
    public void searchInAnd(String criteria) {
        List<DBRecord.DBBinding> bindings = getCriteria(criteria).getBindings();
        for (DBRecord r : table) {
            r.setSelected();
        }
        selectNum = recordNum;
        for (DBRecord.DBBinding b : bindings) {
            for (DBRecord r : table) {
                // check this record only if it was selected in last check
                if (r.isSelected()) {
                    // if this record is not hit by the current criteria, unselect it
                    if (!r.isHit(b.getKey(), b.getValue())) {
                        r.clearSelect();
                        selectNum -= 1;
                    }
                }
            }
        }
    }

    /**
     * clear select all the records in the table
     */
    public void clear() {
        for (DBRecord r : table) {
            r.clearSelect();
            selectNum = 0;
        }
    }

    /**
     * search the table in Or Mode
     * @param criteria
     */
    public void searchInOr(String criteria) {
        clear();
        List<DBRecord.DBBinding> bindings = getCriteria(criteria).getBindings();
        for (DBRecord.DBBinding b : bindings) {
            for (DBRecord r : table) {
                if (!r.isSelected() && r.isHit(b.getKey(), b.getValue())) {
                    r.setSelected();
                    selectNum += 1;
                }
            }
        }
    }

    /**
     * delete all the records in the table
     */
    public void deleteAll() {
        table = new ArrayList<>();
        recordNum = 0;
    }

    /**
     * delete the selected records
     */
    public void deleteSel() {
        delete(true);
    }

    /**
     * determine if a DBRecord will be deleted or not
     * @param selMode true for select, false for unselect
     * @param r the DBRecord to be judged
     * @return true if will be retained, otherwise false
     */
    private boolean retainIt(boolean selMode, DBRecord r) {
        if (selMode) {
            return !r.isSelected();
        }
        else {
            return r.isSelected();
        }
    }

    /**
     * helper function, if the selMode is set to true, then delete the records selected,
     * otherwise delete the unselected records
     * @param selMode
     */
    private void delete(boolean selMode) {
        ArrayList<DBRecord> container = new ArrayList<>();
        int newRecordNum = 0;
        for (DBRecord r : table) {
            if (retainIt(selMode, r)) {
                container.add(r);
                newRecordNum += 1;
            }
        }
        table = container;
        recordNum = newRecordNum;
        if (selMode) {
            selectNum = 0;
        }
        else {
            selectNum = newRecordNum;
        }
    }

    /**
     * delete the unselected records
     */
    public void deleteUnsel() {
        delete(false);
    }
}
