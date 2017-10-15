import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SelectionTest {

    @Test
    public void shouldReturnOutputAsPerSpec() throws Exception {
        Selection selection = new Selection(new Cell(0, 0), 10);

        System.out.println(selection);

        assertThat(selection.formattedOutput(), is("A1"));
    }
}