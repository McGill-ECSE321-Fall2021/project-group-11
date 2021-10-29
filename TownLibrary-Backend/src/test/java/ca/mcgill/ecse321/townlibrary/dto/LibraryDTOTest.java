package ca.mcgill.ecse321.townlibrary.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.HeadLibrarian;

public class LibraryDTOTest {

    @Test
    public void testFromModel() {
        final Library lib = new Library();
        lib.setId(250);
        lib.setAddress("125 FooBar Street");

        LibraryDTO dto;

        dto = LibraryDTO.fromModel(lib);
        Assertions.assertEquals(lib.getId(), dto.id);
        Assertions.assertEquals(lib.getAddress(), dto.address);
        Assertions.assertNull(dto.headLibrarianId);

        lib.setHeadLibrarian(new HeadLibrarian());

        dto = LibraryDTO.fromModel(lib);
        Assertions.assertEquals(lib.getId(), dto.id);
        Assertions.assertEquals(lib.getAddress(), dto.address);
        Assertions.assertEquals(0, dto.headLibrarianId);
    }
}