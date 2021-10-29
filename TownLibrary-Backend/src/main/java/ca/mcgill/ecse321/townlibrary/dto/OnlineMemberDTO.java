package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;
import ca.mcgill.ecse321.townlibrary.model.OnlineMember;

public final class OnlineMemberDTO {

    public int id;

    public String name;
    public String address;
    public boolean inTown;

    public String email;
    public String username;
    // password intentionally omitted

    public Integer libraryId;

    public static OnlineMemberDTO fromModel(OnlineMember u) {
        final OnlineMemberDTO dto = new OnlineMemberDTO();
        dto.id = u.getId();
        dto.name = u.getName();
        dto.address = u.getAddress();
        dto.inTown = u.isInTown();
        dto.email = u.getEmail();
        dto.username = u.getUsername();
        dto.libraryId = Optional.ofNullable(u.getLibrary())
                .map(x -> x.getId())
                .orElse(null);

        return dto;
    }
}