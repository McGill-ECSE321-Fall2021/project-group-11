package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;
import ca.mcgill.ecse321.townlibrary.model.Library;

public final class LibraryDTO {

    public int id;

    public String address;

    public Integer headLibrarianId;

    public static LibraryDTO fromModel(Library lib) {
        final LibraryDTO dto = new LibraryDTO();
        dto.id = lib.getId();
        dto.address = lib.getAddress();
        dto.headLibrarianId = Optional.ofNullable(lib.getHeadLibrarian())
                .map(x -> x.getId())
                .orElse(null);

        return dto;
    }
}