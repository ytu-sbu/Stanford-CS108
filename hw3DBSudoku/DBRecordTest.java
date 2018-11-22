import junit.framework.TestCase;

public class DBRecordTest extends TestCase {
    DBRecord r;
    String s;
    public void setUp() {
        s = "name:The Truth About Cats and Dogs, stars:Janeane Garofalo, stars:Uma Thurman";
        r = new DBRecord(s);
    }

    public void testPrint() {
        String p = r.toString();
        assertEquals(s, p);
    }

    public void testSelect() {
        assertFalse(r.isSelected());
        r.setSelected();
        assertTrue(r.isSelected());
        String star = "*" + s;
        assertEquals(star, r.toString());
        r.clearSelect();
        assertFalse(r.isSelected());
    }

    public void testSearch() {
        assertFalse(r.isHit("year", "the truth"));
        assertFalse(r.isHit("stars", "the truth"));
        assertTrue(r.isHit("name", "the truth about cats and dogs"));
        assertTrue(r.isHit("name", "the truth"));
        assertTrue(r.isHit("stars", "janeane"));
        assertTrue(r.isHit("stars", "thurman"));
    }
}
