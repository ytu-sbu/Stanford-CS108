import junit.framework.TestCase;

public class DBTableTest extends TestCase{
    DBTable t;

    public void setUp() {
        String fileName = "movies.txt";
        t = new DBTable();
        t.read(fileName);
    }

    public void testReadAndPrint() {
        String expected = "name:Alien, stars: Yaphet Kotto, stars:Sigourney Weaver, stars: Harry Dean Stanton\nname:Repo Man, stars: Emilio Estevez, stars:Harry Dean Stanton\nname:The Truth About Cats and Dogs, stars: Janeane Garofalo, stars:Uma Thurman\nname:Sense and Sensibility, stars:Emma Thompson, stars:Hugh Grant\nname:Midnight Run, stars: Yaphet Kotto, stars: Charles Grodin, stars: Robert Deniro";
        assertEquals(expected, t.toString());
    }

    public void testSearchAnd() {
        t.searchInAnd("stars:kotto, stars:stanton");
        String expected = "*name:Alien, stars: Yaphet Kotto, stars:Sigourney Weaver, stars: Harry Dean Stanton\nname:Repo Man, stars: Emilio Estevez, stars:Harry Dean Stanton\nname:The Truth About Cats and Dogs, stars: Janeane Garofalo, stars:Uma Thurman\nname:Sense and Sensibility, stars:Emma Thompson, stars:Hugh Grant\nname:Midnight Run, stars: Yaphet Kotto, stars: Charles Grodin, stars: Robert Deniro";
        assertEquals(expected, t.toString());
    }

    public void testSearchOr() {
        t.searchInOr("stars:kotto, stars:stanton");
        String expected = "*name:Alien, stars: Yaphet Kotto, stars:Sigourney Weaver, stars: Harry Dean Stanton\n*name:Repo Man, stars: Emilio Estevez, stars:Harry Dean Stanton\nname:The Truth About Cats and Dogs, stars: Janeane Garofalo, stars:Uma Thurman\nname:Sense and Sensibility, stars:Emma Thompson, stars:Hugh Grant\n*name:Midnight Run, stars: Yaphet Kotto, stars: Charles Grodin, stars: Robert Deniro";
        assertEquals(expected, t.toString());
    }

    public void testDeleteAll() {
        t.deleteAll();
        assertEquals("", t.toString());
    }

    public void testClear() {
        t.searchInOr("stars:kotto, stars:stanton");
        t.clear();
        String expected = "name:Alien, stars: Yaphet Kotto, stars:Sigourney Weaver, stars: Harry Dean Stanton\nname:Repo Man, stars: Emilio Estevez, stars:Harry Dean Stanton\nname:The Truth About Cats and Dogs, stars: Janeane Garofalo, stars:Uma Thurman\nname:Sense and Sensibility, stars:Emma Thompson, stars:Hugh Grant\nname:Midnight Run, stars: Yaphet Kotto, stars: Charles Grodin, stars: Robert Deniro";
        assertEquals(expected, t.toString());
    }

    public void testDeleteSel() {
        t.searchInAnd("stars:kotto, stars:stanton");
        t.deleteSel();
        String expected = "name:Repo Man, stars: Emilio Estevez, stars:Harry Dean Stanton\nname:The Truth About Cats and Dogs, stars: Janeane Garofalo, stars:Uma Thurman\nname:Sense and Sensibility, stars:Emma Thompson, stars:Hugh Grant\nname:Midnight Run, stars: Yaphet Kotto, stars: Charles Grodin, stars: Robert Deniro";
        assertEquals(expected, t.toString());
    }

    public void testDelteUnsel() {
        t.searchInAnd("stars:kotto, stars:stanton");
        t.deleteUnsel();
        String expected = "*name:Alien, stars: Yaphet Kotto, stars:Sigourney Weaver, stars: Harry Dean Stanton";
        assertEquals(expected, t.toString());
    }
}
