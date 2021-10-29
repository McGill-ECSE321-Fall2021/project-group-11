package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;
import ca.mcgill.ecse321.townlibrary.model.HeadLibrarian;

public final class HeadLibrarianDTO {

    public int id;

    public String name;
    public String address;

    public Integer libraryId;

    public static HeadLibrarianDTO fromModel(HeadLibrarian u) {
        final HeadLibrarianDTO dto = new HeadLibrarianDTO();
        dto.id = u.getId();
        dto.name = u.getName();
        dto.address = u.getAddress();
        dto.libraryId = Optional.ofNullable(u.getLibrary())
                .map(x -> x.getId())
                .orElse(null);

        return dto;
    }
}