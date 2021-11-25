package ca.mcgill.ecse321.townlibrary.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.repository.*;
import ca.mcgill.ecse321.townlibrary.model.*;

@Service
public class ItemService {
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ArchiveRepository archiveRepository;
	@Autowired
	NewspaperRepository newspaperRepository;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	MusicAlbumRepository musicAlbumRepository;
	
	@Autowired
	TransactionService transactionService;
	//UserRoleService userService;
	
	/**
	 * Creates an archive
	 * @param id		The archive id
	 * @param name		The archive name
	 * @param library	The library the archive belongs to
	 * @return			The created archive
	 */
	@Transactional
	public Archive createArchive(int id, String name, Library library) {
		String error = "";
		if (id < 0) {
			error = error + "Unsupported Id.";
	    }
		if (name == null || name.isEmpty()) {
			error = error + "Archive name cannot be empty.";
		}
		if (library == null) {
			error = error + "Library cannot be null.";
		}
		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }
	    
	    Archive archive = new Archive();
	    archive.setId(id);
	    archive.setName(name);
	    archive.setStatus(Status.AVAILABLE);	//available by default
	    archive.setLibrary(library);
	    archiveRepository.save(archive);
	    itemRepository.save(archive);
	    return archive;
	}
	
	/**
	 * Creates a newspaper
	 * @param id		The newspaper id
	 * @param name		The newspaper name
	 * @param library	The library the newspaper belongs to
	 * @return			The created newspaper
	 */
	@Transactional
	public Newspaper createNewspaper(int id, String name, Library library) {
		String error = "";
		if (id < 0) {
			error = error + "Unsupported Id.";
	    }
		if (name == null || name.isEmpty()) {
			error = error + "Newspaper name cannot be empty.";
		}
		if (library == null) {
			error = error + "Library cannot be null.";
		}
		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }
	    
	    Newspaper item = new Newspaper();
	    item.setId(id);
	    item.setName(name);
	    item.setStatus(Status.AVAILABLE);	//available by default
	    item.setLibrary(library);
	    newspaperRepository.save(item);
	    itemRepository.save(item);
	    return item;
	}
	
	/**
	 * Creates a book
	 * @param id		The book id
	 * @param name		The book name
	 * @param library	The library the book belongs to
	 * @return			The created book
	 */
	@Transactional
	public Book createBook(int id, String name, Library library) {
		String error = "";
		if (id < 0) {
			error = error + "Unsupported Id.";
	    }
		if (name == null || name.isEmpty()) {
			error = error + "Book name cannot be empty.";
		}
		if (library == null) {
			error = error + "Library cannot be null.";
		}
		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }
	    
	    Book item = new Book();
	    item.setId(id);
	    item.setName(name);
	    item.setStatus(Status.AVAILABLE);	//available by default
	    item.setLibrary(library);
	    bookRepository.save(item);
	    itemRepository.save(item);
	    return item;
	}
	
	/**
	 * Creates a movie
	 * @param id		The movie id
	 * @param name		The movie name
	 * @param library	The library the movie belongs to
	 * @return			The created movie
	 */
	@Transactional
	public Movie createMovie(int id, String name, Library library) {
		String error = "";
		if (id < 0) {
			error = error + "Unsupported Id.";
	    }
		if (name == null || name.isEmpty()) {
			error = error + "Movie name cannot be empty.";
		}
		if (library == null) {
			error = error + "Library cannot be null.";
		}
		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }
	    
	    Movie item = new Movie();
	    item.setId(id);
	    item.setName(name);
	    item.setStatus(Status.AVAILABLE);	//available by default
	    item.setLibrary(library);
	    movieRepository.save(item);
	    itemRepository.save(item);
	    return item;
	}
	
	/**
	 * Creates a music album
	 * @param id		The music album id
	 * @param name		The music album name
	 * @param library	The library the music album belongs to
	 * @return			The created music album
	 */
	@Transactional
	public MusicAlbum createMusicAlbum(int id, String name, Library library) {
		String error = "";
		if (id < 0) {
			error = error + "Unsupported Id.";
	    }
		if (name == null || name.isEmpty()) {
			error = error + "MusicAlbum name cannot be empty.";
		}
		if (library == null) {
			error = error + "Library cannot be null.";
		}
		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }
	    
	    MusicAlbum item = new MusicAlbum();
	    item.setId(id);
	    item.setName(name);
	    item.setStatus(Status.AVAILABLE);	//available by default
	    item.setLibrary(library);
	    musicAlbumRepository.save(item);
	    itemRepository.save(item);
	    return item;
	}
	
	/**
     * Retrieves an item by its id.
     * @param id    The item id
     * @return      The item or null if no such id exists
     */
	@Transactional
	public Item getItem(int id) {
		if (id < 0) {
	        throw new IllegalArgumentException("Unsupported Id.");
	    }
		return itemRepository.findItemById(id);
	}
	
	/**
     * Retrieves an item by its associated transaction.
     * @param transaction	A transaction
     * @return      		The associated item or null if no such transaction exists
     */
	@Transactional
	public Item getItemByTransaction(Transaction transaction) {
		if (transaction == null) {
	        throw new IllegalArgumentException("Transaction cannot be null!");
	    }
		return itemRepository.findItemByTransaction(transaction);
	}
	
	/**
     * Retrieves all items in the system.
     * @return all items
     */
	@Transactional
	public List<Item> getAllItems() {
		return toList(itemRepository.findAll());
	}
	
	/**
     * Retrieves all items of a specific type in the system.
     * @return all items of specified type
     */
	@Transactional
	public List<Archive> getAllArchives() {
		return toList(archiveRepository.findAll());
	}
	@Transactional
	public List<Newspaper> getAllNewspapers() {
		return toList(newspaperRepository.findAll());
	}
	@Transactional
	public List<Book> getAllBooks() {
		return toList(bookRepository.findAll());
	}
	@Transactional
	public List<Movie> getAllMovies() {
		return toList(movieRepository.findAll());
	}
	@Transactional
	public List<MusicAlbum> getAllMusicAlbums() {
		return toList(musicAlbumRepository.findAll());
	}
	
	private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
	
	/**
     * Retrieves a list of a specific type of item by a fragment of their name.
     * @param name    The fragment of the item's name
     * @return        All items with the specified name fragment or null if no such item exists
     */
	@Transactional
	public List<Archive> getArchiveByName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Item name cannot be empty!");
		}
		return archiveRepository.findByNameContaining(name);
	}
	@Transactional
	public List<Newspaper> getNewspaperByName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Item name cannot be empty!");
		}
		return newspaperRepository.findNewspaperByNameContaining(name);
	}
	@Transactional
	public List<Book> getBookByName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Item name cannot be empty!");
		}
		return bookRepository.findByNameContaining(name);
	}
	@Transactional
	public List<Movie> getMovieByName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Item name cannot be empty!");
		}
		return movieRepository.findByNameContaining(name);
	}
	@Transactional
	public List<MusicAlbum> getMusicAlbumByName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Item name cannot be empty!");
		}
		return musicAlbumRepository.findByNameContaining(name);
	}
	
	/**
     * Retrieves a list of a specific type of item by their status.
     * @param name    The item's status
     * @return        All items with the specified status or null if no such item exists
     */
//	@Transactional
//	public List<Archive> getArchiveByStatus(Status status) {
//		return archiveRepository.findByStatus(status);
//	}
//	@Transactional
//	public List<Newspaper> getNewspaperByStatus(Status status) {
//		return newspaperRepository.findNewspaperByStatus(status);
//	}
	@Transactional
	public List<Book> getBookByStatus(Status status) {
		if (status == null) {
			throw new IllegalArgumentException("Status cannot be null.");
		}
		return bookRepository.findByStatus(status);
	}
	@Transactional
	public List<Movie> getMovieByStatus(Status status) {
		if (status == null) {
			throw new IllegalArgumentException("Status cannot be null.");
		}
		return movieRepository.findByStatus(status);
	}
	@Transactional
	public List<MusicAlbum> getMusicAlbumByStatus(Status status) {
		if (status == null) {
			throw new IllegalArgumentException("Status cannot be null.");
		}
		return musicAlbumRepository.findByStatus(status);
	}
	
	/**
	 * Reserves an appropriate item
	 * @param id	The item's id
	 * @return		The reserved item
	 */
	@Transactional
	public Item reserveItem(int id) {
		String error = "";
		Item item = itemRepository.findItemById(id);
		
		if (id < 0) {
	        error = error + "Unsupported Id.";
	    }
		else if (!itemRepository.existsById(id)) {
			error = error + "Item does not exist.";
		}
		else if (item instanceof Archive || item instanceof Newspaper) {
			error = error + "Cannot reserve archives or newspapers.";
		} 
		else if (item.getStatus() != Status.AVAILABLE){
			error = error + "This item is unavailable.";
		}
		
		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }
	    
		item.setStatus(Status.RESERVED);
		return item;
		
	}
	
	/**
	 * Checks out an appropriate item
	 * @param id	The item's id
	 * @return		The checked out item
	 */
	@Transactional
	public Item checkoutItem(int id, int transactionId) {
		String error = "";
		Item item = itemRepository.findItemById(id);
		
		if (id < 0) {
	        error = error + "Unsupported Id.";
	    }
		else if (!itemRepository.existsById(id)) {
			error = error + "Item does not exist.";
		}
		else if (item instanceof Archive || item instanceof Newspaper) {
			error = error + "Cannot checkout archives or newspapers.";
		} 
		else if (item.getStatus() != Status.AVAILABLE){
			error = error + "This item is unavailable.";
		}
		
		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }
	    
//	    // Create a transaction associated with checked out item
//		Transaction transaction = new Transaction();
//		Timestamp startTime = new Timestamp(System.currentTimeMillis());
//		// Checked out for 2 weeks at a time by default
//		Timestamp endTime = new Timestamp(startTime.getTime() + 1000 * 86400 * 14);
//		transaction.setStartDate(startTime);
//		transaction.setEndDate(endTime);
//		transaction.setId(id);
//		transaction.setUserRole(userService.getUserRole(userId));   
		
		item.setTransaction(transactionService.getTransaction(transactionId));
		item.setStatus(Status.CHECKED_OUT);
		
		return item;
		
	}

	/**
	 * Returns an appropriate item
	 * 
	 * @param id	The item's id
	 * 
	 * @return		The returned item
	 * 
	 * @throws IllegalArgumentException if the item does not exist or is not already checked out
	 */
	@Transactional
	public Item returnItem(int id) {
		Item item = itemRepository.findItemById(id);
		if (item == null) {
			throw new IllegalArgumentException("Item does not exist.");
		}
		if (item.getStatus() != Status.CHECKED_OUT) {
			throw new IllegalArgumentException("You may only return checked out items.");
		}
		else {
			item.setTransaction(null);	// remove associated transaction
			item.setStatus(Status.AVAILABLE);
			return item;
		}
	}

	/**
	 * Renews an appropriate item
	 * 
	 * @param id	The item's id
	 * 
	 * @return		The renewed item
	 * 
	 * @throws IllegalArgumentException if the item does not exist or is not already checked out
	 */
	@Transactional
	public Item renewItem(int id) {
		Item item = itemRepository.findItemById(id);
		if (item == null) {
			throw new IllegalArgumentException("Item does not exist.");
		}
		if (item.getStatus() != Status.CHECKED_OUT) {
			throw new IllegalArgumentException("You may only renew an item that is already checked out.");
		}
		else {
			// extend transaction end date
			long newEndTime = item.getTransaction().getEndDate().getTime() + 1000 * 86400 * 14;
			Timestamp newEndDate = new Timestamp(newEndTime);
			item.getTransaction().setEndDate(newEndDate);
			
			item.setStatus(Status.CHECKED_OUT);
			return item;
		}
	}

}
