package ca.mcgill.ecse321.townlibrary.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.OnlineMember;

public class OnlineMemberDTOTest {

    @Test
    public void testFromModel() {
        final OnlineMember u = new OnlineMember();
        u.setId(100);
        u.setName("Joe Schmoe");
        u.setAddress("123 FooBar Street");
        u.setEmail("joe.schmoe@phaser.com");
        u.setUsername("foo_man");
        u.setPassword("Foobar123");

        OnlineMemberDTO dto;

        dto = OnlineMemberDTO.fromModel(u);
        Assertions.assertEquals(u.getId(), dto.id);
        Assertions.assertEquals(u.getName(), dto.name);
        Assertions.assertEquals(u.getAddress(), dto.address);
        Assertions.assertEquals(u.getEmail(), dto.email);
        Assertions.assertEquals(u.getUsername(), dto.username);
        Assertions.assertNull(dto.libraryId);

        u.setLibrary(new Library());

        dto = OnlineMemberDTO.fromModel(u);
        Assertions.assertEquals(u.getId(), dto.id);
        Assertions.assertEquals(u.getName(), dto.name);
        Assertions.assertEquals(u.getAddress(), dto.address);
        Assertions.assertEquals(u.getEmail(), dto.email);
        Assertions.assertEquals(u.getUsername(), dto.username);
        Assertions.assertEquals(0, dto.libraryId);
    }
}
