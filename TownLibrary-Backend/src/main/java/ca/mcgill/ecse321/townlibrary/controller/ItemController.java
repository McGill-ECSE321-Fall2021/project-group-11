package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	
	
	@GetMapping(value = { "/archives/{id}", "/archives/{id}/"})
	public ResponseEntity<?> getArchive(@PathVariable("id") int id) {
		Archive i = (Archive) service.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("ARCHIVE-NOT-FOUND");
		} return ResponseEntity.ok(ArchiveDTO.fromModel(i));
	}
	@GetMapping(value = { "/newspapers/{id}", "/newspapers/{id}/"})
	public ResponseEntity<?> getNewspaper(@PathVariable("id") int id) {
		Newspaper i = (Newspaper) service.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("NEWSPAPER-NOT-FOUND");
		} return ResponseEntity.ok(NewspaperDTO.fromModel(i));
	}
	@GetMapping(value = { "/books/{id}", "/books/{id}/"})
	public ResponseEntity<?> getBook(@PathVariable("id") int id) {
		Book i = (Book) service.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("BOOK-NOT-FOUND");
		} return ResponseEntity.ok(BookDTO.fromModel(i));
	}
	@GetMapping(value = { "/movies/{id}", "/movies/{id}/"})
	public ResponseEntity<?> getMovie(@PathVariable("id") int id) {
		Movie i = (Movie) service.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("MOVIE-NOT-FOUND");
		} return ResponseEntity.ok(MovieDTO.fromModel(i));
	}
	@GetMapping(value = { "/musicalbums/{id}", "/musicalbums/{id}/"})
	public ResponseEntity<?> getMusicAlbum(@PathVariable("id") int id) {
		MusicAlbum i = (MusicAlbum) service.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("MUSIC-ALBUM-NOT-FOUND");
		} return ResponseEntity.ok(MusicAlbumDTO.fromModel(i));
	}

}
