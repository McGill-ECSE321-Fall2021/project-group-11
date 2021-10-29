package ca.mcgill.ecse321.townlibrary.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.OfflineMember;

public class OfflineMemberDTOTest {

    @Test
    public void testFromModel() {
        final OfflineMember u = new OfflineMember();
        u.setId(100);
        u.setName("Joe Schmoe");
        u.setAddress("123 FooBar Street");

        OfflineMemberDTO dto;

        dto = OfflineMemberDTO.fromModel(u);
        Assertions.assertEquals(u.getId(), dto.id);
        Assertions.assertEquals(u.getName(), dto.name);
        Assertions.assertEquals(u.getAddress(), dto.address);
        Assertions.assertNull(dto.libraryId);

        u.setLibrary(new Library());

        dto = OfflineMemberDTO.fromModel(u);
        Assertions.assertEquals(u.getId(), dto.id);
        Assertions.assertEquals(u.getName(), dto.name);
        Assertions.assertEquals(u.getAddress(), dto.address);
        Assertions.assertEquals(0, dto.libraryId);
    }
}