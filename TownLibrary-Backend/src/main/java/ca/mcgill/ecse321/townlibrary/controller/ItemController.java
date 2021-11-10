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
    private LibraryService libraryService;
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * Get all items of a specific type
	 * @param id
	 * @return list of items of a specific type
	 */
	@GetMapping(value = { "/archives", "/archives/"})
    public List<ArchiveDTO> getAllArchives() {
        return itemService.getAllArchives()
            .stream().map(ArchiveDTO::fromModel)
            .collect(Collectors.toList());
    }
	@GetMapping(value = { "/newspapers", "/newspapers/"})
    public List<NewspaperDTO> getAllNewspapers() {
        return itemService.getAllNewspapers()
            .stream().map(NewspaperDTO::fromModel)
            .collect(Collectors.toList());
    }
	@GetMapping(value = { "/books", "/books/"})
    public List<BookDTO> getAllBooks() {
        return itemService.getAllBooks()
            .stream().map(BookDTO::fromModel)
            .collect(Collectors.toList());
    }
	@GetMapping(value = { "/movies", "/movies/"})
    public List<MovieDTO> getAllMovies() {
        return itemService.getAllMovies()
            .stream().map(MovieDTO::fromModel)
            .collect(Collectors.toList());
    }
	@GetMapping(value = { "/musicalbums", "/musicalbums/"})
    public List<MusicAlbumDTO> getAllMusicAlbums() {
        return itemService.getAllMusicAlbums()
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
		Archive i = (Archive) itemService.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("ARCHIVE-NOT-FOUND");
		} return ResponseEntity.ok(ArchiveDTO.fromModel(i));
	}
	@GetMapping(value = { "/newspapers/{id}", "/newspapers/{id}/"})
	public ResponseEntity<?> getNewspaper(@PathVariable("id") int id) {
		Newspaper i = (Newspaper) itemService.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("NEWSPAPER-NOT-FOUND");
		} return ResponseEntity.ok(NewspaperDTO.fromModel(i));
	}
	@GetMapping(value = { "/books/{id}", "/books/{id}/"})
	public ResponseEntity<?> getBook(@PathVariable("id") int id) {
		Book i = (Book) itemService.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("BOOK-NOT-FOUND");
		} return ResponseEntity.ok(BookDTO.fromModel(i));
	}
	@GetMapping(value = { "/movies/{id}", "/movies/{id}/"})
	public ResponseEntity<?> getMovie(@PathVariable("id") int id) {
		Movie i = (Movie) itemService.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("MOVIE-NOT-FOUND");
		} return ResponseEntity.ok(MovieDTO.fromModel(i));
	}
	@GetMapping(value = { "/musicalbums/{id}", "/musicalbums/{id}/"})
	public ResponseEntity<?> getMusicAlbum(@PathVariable("id") int id) {
		MusicAlbum i = (MusicAlbum) itemService.getItem(id);
		if (i == null) {
			return ResponseEntity.badRequest().body("MUSIC-ALBUM-NOT-FOUND");
		} return ResponseEntity.ok(MusicAlbumDTO.fromModel(i));
	}
	
	/**
	 * Create an item of a specific type
	 * @param id			The item id
	 * @param name			The item name
	 * @param libraryId		The library id
	 * @return				The created item
	 */
	@PostMapping(value = { "/archives/{id}", "/archives/{id}/"})
	public ResponseEntity<?> createArchive(
			@PathVariable("id") int id,
			@RequestParam String name,
			@RequestParam int libraryId) {
		
		try {
			final Library library = libraryService.getLibrary(libraryId);
			final Archive item = itemService.createArchive(id, name, library);
			return ResponseEntity.ok().body(ArchiveDTO.fromModel(item));
		} catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
	}
	
	@PostMapping(value = { "/newspapers/{id}", "/newspapers/{id}/"})
	public ResponseEntity<?> createNewspaper(
			@PathVariable("id") int id,
			@RequestParam String name,
			@RequestParam int libraryId) {
		
		try {
			final Library library = libraryService.getLibrary(libraryId);
			final Newspaper item = itemService.createNewspaper(id, name, library);
			return ResponseEntity.ok().body(NewspaperDTO.fromModel(item));
		} catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
	}
	
	@PostMapping(value = { "/movies/{id}", "/movies/{id}/"})
	public ResponseEntity<?> createMovie(
			@PathVariable("id") int id,
			@RequestParam String name,
			@RequestParam int libraryId) {
		
		try {
			final Library library = libraryService.getLibrary(libraryId);
			final Movie item = itemService.createMovie(id, name, library);
			return ResponseEntity.ok().body(MovieDTO.fromModel(item));
		} catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
	}
	
	@PostMapping(value = { "/musicalbums/{id}", "/musicalbums/{id}/"})
	public ResponseEntity<?> createMusicAlbum(
			@PathVariable("id") int id,
			@RequestParam String name,
			@RequestParam int libraryId) {
		
		try {
			final Library library = libraryService.getLibrary(libraryId);
			final MusicAlbum item = itemService.createMusicAlbum(id, name, library);
			return ResponseEntity.ok().body(MusicAlbumDTO.fromModel(item));
		} catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
	}
	
	@PostMapping(value = { "/books/{id}", "/books/{id}/"})
	public ResponseEntity<?> createBook(
			@PathVariable("id") int id,
			@RequestParam String name,
			@RequestParam int libraryId) {
		
		try {
			final Library library = libraryService.getLibrary(libraryId);
			final Book item = itemService.createBook(id, name, library);
			return ResponseEntity.ok().body(BookDTO.fromModel(item));
		} catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
	}
	
	
	/**
	 * Get a single item of a specific type with its name
	 * @param 	The item name
	 * @return 	Item of a specific type
	 */
	@GetMapping(value = { "/archives/byName/{name}", "/archives/byName/{name}/"})
	public ResponseEntity<?> getArchiveByName(@PathVariable("name") String name) {
		Archive i = (Archive) itemService.getArchiveByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("ARCHIVE-NOT-FOUND");
		} return ResponseEntity.ok(ArchiveDTO.fromModel(i));
	}
	@GetMapping(value = { "/newspapers/byName/{name}", "/newspapers/byName/{name}/"})
	public ResponseEntity<?> getNewspaperByName(@PathVariable("name") String name) {
		Newspaper i = (Newspaper) itemService.getNewspaperByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("NEWSPAPER-NOT-FOUND");
		} return ResponseEntity.ok(NewspaperDTO.fromModel(i));
	}
	@GetMapping(value = { "/books/byName/{name}", "/books/byName/{name}/"})
	public ResponseEntity<?> getBookByName(@PathVariable("name") String name) {
		Book i = (Book) itemService.getBookByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("BOOK-NOT-FOUND");
		} return ResponseEntity.ok(BookDTO.fromModel(i));
	}
	@GetMapping(value = { "/movies/byName/{name}", "/movies/byName/{name}/"})
	public ResponseEntity<?> getMovieByName(@PathVariable("name") String name) {
		Movie i = (Movie) itemService.getMovieByName(name);
		if (i == null) {
			return ResponseEntity.badRequest().body("MOVIE-NOT-FOUND");
		} return ResponseEntity.ok(MovieDTO.fromModel(i));
	}
	@GetMapping(value = { "/musicalbums/byName/{name}", "/musicalbums/byName/{name}/"})
	public ResponseEntity<?> getMusicAlbumByName(@PathVariable("name") String name) {
		MusicAlbum i = (MusicAlbum) itemService.getMusicAlbumByName(name);
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
		Book i = (Book) itemService.getBookByStatus(status);
		if (i == null) {
			return ResponseEntity.badRequest().body("BOOK-NOT-FOUND");
		} return ResponseEntity.ok(BookDTO.fromModel(i));
	}
	@GetMapping(value = { "/movies/byStatus/{status}", "/movies/byStatus/{status}/"})
	public ResponseEntity<?> getMovieByStatus(@PathVariable("status") Status status) {
		Movie i = (Movie) itemService.getMovieByStatus(status);
		if (i == null) {
			return ResponseEntity.badRequest().body("MOVIE-NOT-FOUND");
		} return ResponseEntity.ok(MovieDTO.fromModel(i));
	}
	@GetMapping(value = { "/musicalbums/byStatus/{status}", "/musicalbums/byStatus/{status}/"})
	public ResponseEntity<?> getMusicAlbumByStatus(@PathVariable("status") Status status) {
		MusicAlbum i = (MusicAlbum) itemService.getMusicAlbumByStatus(status);
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
			Book i = (Book) itemService.reserveItem(id);
			return ResponseEntity.ok(BookDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping(value = { "/movies/{id}/reserve", "/movies/{id}/reserve/"})
	public ResponseEntity<?> setMovieReserved(@PathVariable("id") int id) {
		try {
			Movie i = (Movie) itemService.reserveItem(id);
			return ResponseEntity.ok(MovieDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping(value = { "/musicalbums/{id}/reserve", "/musicalbums/{id}/reserve/"})
	public ResponseEntity<?> setMusicAlbumReserved(@PathVariable("id") int id) {
		try {
			MusicAlbum i = (MusicAlbum) itemService.reserveItem(id);
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
	public ResponseEntity<?> setBookCheckedout(
			@PathVariable("id") int id, 
			@RequestParam int userId) {
		try {
			Book i = (Book) itemService.checkoutItem(id, userId);
			return ResponseEntity.ok(BookDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping(value = { "/movies/{id}/checkout", "/movies/{id}/checkout/"})
	public ResponseEntity<?> setMovieCheckedout(
			@PathVariable("id") int id, 
			@RequestParam int userId) {
		try {
			Movie i = (Movie) itemService.checkoutItem(id, userId);
			return ResponseEntity.ok(MovieDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping(value = { "/musicalbums/{id}/checkout", "/musicalbums/{id}/checkout/"})
	public ResponseEntity<?> setMusicAlbumCheckedout(
			@PathVariable("id") int id, 
			@RequestParam int userId) {
		try {
			MusicAlbum i = (MusicAlbum) itemService.checkoutItem(id, userId);
			return ResponseEntity.ok(MusicAlbumDTO.fromModel(i));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
