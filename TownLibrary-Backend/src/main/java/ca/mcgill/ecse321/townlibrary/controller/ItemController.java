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
	
	/**
	 * Get all items of a specific type
	 * @param id
	 * @return list of items of a specific type
	 */
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
	
	/**
	 * Get a single item of a specific type with its id
	 * @param 	The item id
	 * @return 	Item of a specific type
	 */
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
	
	/**
	 * Get a single item of a specific type with its name
	 * @param 	The item name
	 * @return 	Item of a specific type
	 */
	@GetMapping(value = { "/archives/byName/{name}", "/archives/byName/{name}/"})
	public ResponseEntity<?> getArchiveByName(@PathVariable("name") String name) {
		Archive i = (Archive) service.getArchiveByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("ARCHIVE-NOT-FOUND");
		} return ResponseEntity.ok(ArchiveDTO.fromModel(i));
	}
	@GetMapping(value = { "/newspapers/byName/{name}", "/newspapers/byName/{name}/"})
	public ResponseEntity<?> getNewspaperByName(@PathVariable("name") String name) {
		Newspaper i = (Newspaper) service.getNewspaperByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("NEWSPAPER-NOT-FOUND");
		} return ResponseEntity.ok(NewspaperDTO.fromModel(i));
	}
	@GetMapping(value = { "/books/byName/{name}", "/books/byName/{name}/"})
	public ResponseEntity<?> getBookByName(@PathVariable("name") String name) {
		Book i = (Book) service.getBookByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("BOOK-NOT-FOUND");
		} return ResponseEntity.ok(BookDTO.fromModel(i));
	}
	@GetMapping(value = { "/movies/byName/{name}", "/movies/byName/{name}/"})
	public ResponseEntity<?> getMovieByName(@PathVariable("name") String name) {
		Movie i = (Movie) service.getMovieByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("MOVIE-NOT-FOUND");
		} return ResponseEntity.ok(MovieDTO.fromModel(i));
	}
	@GetMapping(value = { "/musicalbums/byName/{name}", "/musicalbums/byName/{name}/"})
	public ResponseEntity<?> getMusicAlbumByName(@PathVariable("name") String name) {
		MusicAlbum i = (MusicAlbum) service.getMusicAlbumByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("MUSIC-ALBUM-NOT-FOUND");
		} return ResponseEntity.ok(MusicAlbumDTO.fromModel(i));
	}
	
	/**
	 * Get a single item of a specific type with its status
	 * Not needed for Archives & Newspapers since they always have the same status
	 * @param 	The item status
	 * @return 	Item of a specific type
	 */
	@GetMapping(value = { "/books/byStatus/{status}", "/books/byStatus/{status}/"})
	public ResponseEntity<?> getBookByStatus(@PathVariable("status") Status status) {
		Book i = (Book) service.getBookByStatus(status);
		if (i == null) {
			return ResponseEntity.badRequest().body("BOOK-NOT-FOUND");
		} return ResponseEntity.ok(BookDTO.fromModel(i));
	}
	@GetMapping(value = { "/movies/byStatus/{status}", "/movies/byStatus/{status}/"})
	public ResponseEntity<?> getMovieByStatus(@PathVariable("status") Status status) {
		Movie i = (Movie) service.getMovieByStatus(status);
		if (i == null) {
			return ResponseEntity.badRequest().body("MOVIE-NOT-FOUND");
		} return ResponseEntity.ok(MovieDTO.fromModel(i));
	}
	@GetMapping(value = { "/musicalbums/byStatus/{status}", "/musicalbums/byStatus/{status}/"})
	public ResponseEntity<?> getMusicAlbumByStatus(@PathVariable("status") Status status) {
		MusicAlbum i = (MusicAlbum) service.getMusicAlbumByStatus(status);
		if (i == null) {
			return ResponseEntity.badRequest().body("MUSIC-ALBUM-NOT-FOUND");
		} return ResponseEntity.ok(MusicAlbumDTO.fromModel(i));
	}
	
	/**
	 * Reserve an item
	 * @param 	Item id
	 * @return	Reserved item
	 */
	@PutMapping(value = { "/books/{id}/reserve", "/books/{id}/reserve/"})
	public ResponseEntity<?> setBookReserved(@PathVariable("id") int id) {
		try {
			Book i = (Book) service.reserveItem(id);
			return ResponseEntity.ok(BookDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping(value = { "/movies/{id}/reserve", "/movies/{id}/reserve/"})
	public ResponseEntity<?> setMovieReserved(@PathVariable("id") int id) {
		try {
			Movie i = (Movie) service.reserveItem(id);
			return ResponseEntity.ok(MovieDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping(value = { "/musicalbums/{id}/reserve", "/musicalbums/{id}/reserve/"})
	public ResponseEntity<?> setMusicAlbumReserved(@PathVariable("id") int id) {
		try {
			MusicAlbum i = (MusicAlbum) service.reserveItem(id);
			return ResponseEntity.ok(MusicAlbumDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * Check out an item
	 * @param 	Item id
	 * @return	Checked out item
	 */
	@PutMapping(value = { "/books/{id}/checkout", "/books/{id}/checkout/"})
	public ResponseEntity<?> setBookCheckedout(@PathVariable("id") int id, @PathVariable("user") UserRole user) {
		try {
			Book i = (Book) service.checkoutItem(id, user);
			return ResponseEntity.ok(BookDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping(value = { "/movies/{id}/checkout", "/movies/{id}/checkout/"})
	public ResponseEntity<?> setMovieCheckedout(@PathVariable("id") int id, @PathVariable("user") UserRole user) {
		try {
			Movie i = (Movie) service.checkoutItem(id, user);
			return ResponseEntity.ok(MovieDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping(value = { "/musicalbums/{id}/checkout", "/musicalbums/{id}/checkout/"})
	public ResponseEntity<?> setMusicAlbumCheckedout(@PathVariable("id") int id, @PathVariable("user") UserRole user) {
		try {
			MusicAlbum i = (MusicAlbum) service.checkoutItem(id, user);
			return ResponseEntity.ok(MusicAlbumDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
