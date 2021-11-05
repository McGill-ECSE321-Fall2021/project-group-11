package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins = "*")
@RestController
public class ItemController {
	
	@Autowired
	private ItemService service;
	
	@GetMapping(value = { "/archives", "/archives/"})
    public List<ArchiveDTO> getAllArchives() {
        return service.getAllArchives()
            .stream().map(ArchiveDTO::fromModel)
            .collect(Collectors.toList());
    }
	@GetMapping(value = { "/newspapers", "/newspapers/"})
    public List<NewspaperDTO> getAllNewspapers() {
        return service.getAllNewspapers()
            .stream().map(NewspaperDTO::fromModel)
            .collect(Collectors.toList());
    }
	@GetMapping(value = { "/books", "/books/"})
    public List<BookDTO> getAllBooks() {
        return service.getAllBooks()
            .stream().map(BookDTO::fromModel)
            .collect(Collectors.toList());
    }
	@GetMapping(value = { "/movies", "/movies/"})
    public List<MovieDTO> getAllMovies() {
        return service.getAllMovies()
            .stream().map(MovieDTO::fromModel)
            .collect(Collectors.toList());
    }
	@GetMapping(value = { "/musicalbums", "/musicalbums/"})
    public List<MusicAlbumDTO> getAllMusicAlbums() {
        return service.getAllMusicAlbums()
            .stream().map(MusicAlbumDTO::fromModel)
            .collect(Collectors.toList());
    }
	
//	@GetMapping(value = { "/archives/{id}", "/archives/{id}/"})
//	public List<ArchiveDTO> getArchive(@PathVariable("id") int id) {
//		return ;
//	}

}
