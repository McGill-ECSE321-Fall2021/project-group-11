package ca.mcgill.ecse321.townlibrary.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Librarian;

public class LibrarianDTOTest {

    @Test
    public void testFromModel() {
        final Librarian u = new Librarian();
        u.setId(100);
        u.setName("Joe Schmoe");
        u.setAddress("123 FooBar Street");
        u.setPassword("Foobar123");

        LibrarianDTO dto;

        dto = LibrarianDTO.fromModel(u);
        Assertions.assertEquals(u.getId(), dto.id);
        Assertions.assertEquals(u.getName(), dto.name);
        Assertions.assertEquals(u.getAddress(), dto.address);
        Assertions.assertNull(dto.libraryId);

        u.setLibrary(new Library());

        dto = LibrarianDTO.fromModel(u);
        Assertions.assertEquals(u.getId(), dto.id);
        Assertions.assertEquals(u.getName(), dto.name);
        Assertions.assertEquals(u.getAddress(), dto.address);
        Assertions.assertEquals(0, dto.libraryId);
    }
}