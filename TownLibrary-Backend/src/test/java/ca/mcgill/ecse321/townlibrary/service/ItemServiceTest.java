package ca.mcgill.ecse321.townlibrary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

import java.util.*;
import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.repository.*;
import ca.mcgill.ecse321.townlibrary.model.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

	@Mock
	ItemRepository itemDao;
	@Mock
	ArchiveRepository archiveDao;
	@Mock
	NewspaperRepository newspaperDao;
	@Mock
	BookRepository bookDao;
	@Mock
	MovieRepository movieDao;
	@Mock
	MusicAlbumRepository musicAlbumDao;
	
	@Mock
	UserRoleService userService;

	@InjectMocks
	private ItemService itemService;
	
	private static final Transaction TRANSACTION_MOVIE = new Transaction();
	private static final Transaction TRANSACTION_MUSIC = new Transaction();
	private static final int USER_ID = 4444;
	
	private static final int BAD_ID = -1;
	private static final int NONEXISTING_ID = 0;
	private static final int BOOK_ID = 11;	// available
	private static final int MOVIE_ID = 22;	// unavailable checked out
	private static final int MUSICALBUM_ID = 33; // unavailable reserved
	private static final int NEWSPAPER_ID = 44;
	private static final int ARCHIVE_ID = 55;
	
	@BeforeEach
	public void setMockOutput() {
		lenient().when(itemDao.findItemById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(BOOK_ID)) {
				Book book = new Book();
				book.setId(BOOK_ID);
				book.setName("Dune");
				book.setStatus(Status.AVAILABLE);
				return book;
			}
			else if (invocation.getArgument(0).equals(MOVIE_ID)) {
				Movie movie = new Movie();
				movie.setId(MOVIE_ID);
				movie.setName("Interstellar");
				movie.setStatus(Status.CHECKED_OUT);
				TRANSACTION_MOVIE.setEndDate(new Timestamp(System.currentTimeMillis()));
				movie.setTransaction(TRANSACTION_MOVIE);
				return movie;
			}
			else if (invocation.getArgument(0).equals(MUSICALBUM_ID)) {
				MusicAlbum music = new MusicAlbum();
				music.setId(MUSICALBUM_ID);
				music.setName("Chopin Nocturnes");
				music.setStatus(Status.RESERVED);
				music.setTransaction(TRANSACTION_MUSIC);
				return music;
			}
			else if (invocation.getArgument(0).equals(NEWSPAPER_ID)) {
				Newspaper news = new Newspaper();
				news.setId(NEWSPAPER_ID);
				news.setName("Gazette");
				news.setStatus(Status.AVAILABLE);
				return news;
			}
			else if (invocation.getArgument(0).equals(ARCHIVE_ID)) {
				Archive archive = new Archive();
				archive.setId(ARCHIVE_ID);
				archive.setName("Records-2021");
				archive.setStatus(Status.AVAILABLE);
				return archive;
			}
			else {
				return null;
			}
		});
		lenient().when(itemDao.findItemByTransaction(any(Transaction.class))).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(TRANSACTION_MOVIE)) {
				Movie movie = new Movie();
				movie.setTransaction(TRANSACTION_MOVIE);
				return movie;
			}
			if (invocation.getArgument(0).equals(TRANSACTION_MUSIC)) {
				MusicAlbum music = new MusicAlbum();
				music.setTransaction(TRANSACTION_MUSIC);
				return music;
			} 
			else {
				return null;
			}
		});
		lenient().when(itemDao.existsById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(BOOK_ID) ||
				invocation.getArgument(0).equals(MOVIE_ID) ||
				invocation.getArgument(0).equals(MUSICALBUM_ID) ||
				invocation.getArgument(0).equals(ARCHIVE_ID) ||
				invocation.getArgument(0).equals(NEWSPAPER_ID)) {
				return true;
			} else {
				return false;
			}
		});
		lenient().when(bookDao.findByNameContaining(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals("Dune")) {
				Book book = new Book();
				book.setId(BOOK_ID);
				book.setName("Dune");
				book.setStatus(Status.AVAILABLE);

				List<Book> bList = new ArrayList<Book>();
				bList.add(book);
				return bList;
			} else {
				return new ArrayList<Book>();
			}
		});
		lenient().when(movieDao.findByNameContaining(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals("stellar")) {
				Movie movie = new Movie();
				movie.setId(BOOK_ID);
				movie.setName("Interstellar");
				movie.setStatus(Status.CHECKED_OUT);
				movie.setTransaction(TRANSACTION_MOVIE);
				
				List<Movie> mList = new ArrayList<Movie>();
				mList.add(movie);
				return mList;
			} else {
				return new ArrayList<Movie>();
			}
		});
		lenient().when(musicAlbumDao.findByNameContaining(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals("Chopin")) {
				MusicAlbum music = new MusicAlbum();
				music.setId(MUSICALBUM_ID);
				music.setName("Chopin Nocturnes");
				music.setStatus(Status.RESERVED);
				music.setTransaction(TRANSACTION_MUSIC);
				
				List<MusicAlbum> mList = new ArrayList<MusicAlbum>();
				mList.add(music);
				return mList;
			} else {
				return new ArrayList<MusicAlbum>();
			}
		});
		lenient().when(newspaperDao.findNewspaperByNameContaining(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals("Gaz")) {
				Newspaper news = new Newspaper();
				news.setId(NEWSPAPER_ID);
				news.setName("Gazette");
				news.setStatus(Status.AVAILABLE);
				
				List<Newspaper> nList = new ArrayList<Newspaper>();
				nList.add(news);
				return nList;
			} else {
				return new ArrayList<Newspaper>();
			}
		});
		lenient().when(archiveDao.findByNameContaining(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals("Records")) {
				Archive archive = new Archive();
				archive.setId(ARCHIVE_ID);
				archive.setName("Records-2021");
				archive.setStatus(Status.AVAILABLE);
				
				List<Archive> aList = new ArrayList<Archive>();
				aList.add(archive);
				return aList;
			} else {
				return new ArrayList<Archive>();
			}
		});
		lenient().when(userService.getUserRole(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(USER_ID)) {
				UserRole user = new OfflineMember();
				user.setId(USER_ID);
				user.setName("Jhon");
				return user;
			} else {
				return null;
			}
		});
	}
	
	@Test
	public void testCreateArchive() {
		final Library library = new Library();
		library.setId(321);
		final Archive archive = itemService.createArchive(ARCHIVE_ID, "Records2021" , library);
		assertEquals(ARCHIVE_ID, archive.getId());
		assertEquals("Records2021", archive.getName());
		assertEquals(Status.AVAILABLE, archive.getStatus());
		assertEquals(library.getId(), archive.getLibrary().getId());
		
		// Bad id
		try {
			itemService.createArchive(BAD_ID, "Records2021" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}
		
		// Null name
		try {
			itemService.createArchive(ARCHIVE_ID, null , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Archive name cannot be empty.", e.getMessage());
		}
		
		// Empty name
		try {
			itemService.createArchive(ARCHIVE_ID, "" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Archive name cannot be empty.", e.getMessage());
		}
		
		// Null library
		try {
			itemService.createArchive(ARCHIVE_ID, "Records2021" , null);
		} catch (IllegalArgumentException e) {
			assertEquals("Library cannot be null.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateNewspaper() {
		final Library library = new Library();
		library.setId(321);
		final Newspaper item = itemService.createNewspaper(NEWSPAPER_ID, "Gazette" , library);
		assertEquals(NEWSPAPER_ID, item.getId());
		assertEquals("Gazette", item.getName());
		assertEquals(Status.AVAILABLE, item.getStatus());
		assertEquals(library.getId(), item.getLibrary().getId());
		
		// Bad id
		try {
			itemService.createNewspaper(BAD_ID, "Gazette" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}
		
		// Null name
		try {
			itemService.createNewspaper(NEWSPAPER_ID, null , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Newspaper name cannot be empty.", e.getMessage());
		}
		
		// Empty name
		try {
			itemService.createNewspaper(NEWSPAPER_ID, "" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Newspaper name cannot be empty.", e.getMessage());
		}
		
		// Null library
		try {
			itemService.createNewspaper(NEWSPAPER_ID, "Gazette" , null);
		} catch (IllegalArgumentException e) {
			assertEquals("Library cannot be null.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateBook() {
		final Library library = new Library();
		library.setId(321);
		final Book item = itemService.createBook(BOOK_ID, "Dune" , library);
		assertEquals(BOOK_ID, item.getId());
		assertEquals("Dune", item.getName());
		assertEquals(Status.AVAILABLE, item.getStatus());
		assertEquals(library.getId(), item.getLibrary().getId());
		
		// Bad id
		try {
			itemService.createBook(BAD_ID, "Dune" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}
		
		// Null name
		try {
			itemService.createBook(BOOK_ID, null , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Book name cannot be empty.", e.getMessage());
		}
		
		// Empty name
		try {
			itemService.createBook(BOOK_ID, "" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Book name cannot be empty.", e.getMessage());
		}
		
		// Null library
		try {
			itemService.createBook(BOOK_ID, "Dune" , null);
		} catch (IllegalArgumentException e) {
			assertEquals("Library cannot be null.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateMovie() {
		final Library library = new Library();
		library.setId(321);
		final Movie item = itemService.createMovie(MOVIE_ID, "Interstellar" , library);
		assertEquals(MOVIE_ID, item.getId());
		assertEquals("Interstellar", item.getName());
		assertEquals(Status.AVAILABLE, item.getStatus());
		assertEquals(library.getId(), item.getLibrary().getId());
		
		// Bad id
		try {
			itemService.createMovie(BAD_ID, "Interstellar" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}
		
		// Null name
		try {
			itemService.createMovie(MOVIE_ID, null , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Movie name cannot be empty.", e.getMessage());
		}
		
		// Empty name
		try {
			itemService.createMovie(MOVIE_ID, "" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Movie name cannot be empty.", e.getMessage());
		}
		
		// Null library
		try {
			itemService.createMovie(MOVIE_ID, "Interstellar" , null);
		} catch (IllegalArgumentException e) {
			assertEquals("Library cannot be null.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateMusicAlbum() {
		final Library library = new Library();
		library.setId(321);
		final MusicAlbum item = itemService.createMusicAlbum(MUSICALBUM_ID, "Evolve" , library);
		assertEquals(MUSICALBUM_ID, item.getId());
		assertEquals("Evolve", item.getName());
		assertEquals(Status.AVAILABLE, item.getStatus());
		assertEquals(library.getId(), item.getLibrary().getId());
		
		// Bad id
		try {
			itemService.createMusicAlbum(BAD_ID, "Evolve" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}
		
		// Null name
		try {
			itemService.createMusicAlbum(MUSICALBUM_ID, null , library);
		} catch (IllegalArgumentException e) {
			assertEquals("MusicAlbum name cannot be empty.", e.getMessage());
		}
		
		// Empty name
		try {
			itemService.createMusicAlbum(MUSICALBUM_ID, "" , library);
		} catch (IllegalArgumentException e) {
			assertEquals("MusicAlbum name cannot be empty.", e.getMessage());
		}
		
		// Null library
		try {
			itemService.createMusicAlbum(MUSICALBUM_ID, "Evolve" , null);
		} catch (IllegalArgumentException e) {
			assertEquals("Library cannot be null.", e.getMessage());
		}
	}

	@Test
	public void testGetItem() {

		assertEquals(BOOK_ID, itemService.getItem(BOOK_ID).getId());
		assertNull(itemService.getItem(NONEXISTING_ID));

		try {
			itemService.getItem(BAD_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}
	}

	@Test
	public void testGetItemByTransaction() {
		
		assertEquals(TRANSACTION_MOVIE, itemService.getItemByTransaction(TRANSACTION_MOVIE).getTransaction());
		
		try {
			itemService.getItemByTransaction(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Transaction cannot be null!", e.getMessage());
		}
	}

	@Test
	public void testGetItemByName() {
		
		// Book
		assertEquals(1, itemService.getBookByName("Dune").size());
		assertEquals("Dune", itemService.getBookByName("Dune").get(0).getName());
		try {
			itemService.getBookByName(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
		try {
			itemService.getBookByName("");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
		
		// Movie
		assertEquals(1, itemService.getMovieByName("stellar").size());
		assertEquals("Interstellar", itemService.getMovieByName("stellar").get(0).getName());

		// MusicAlbum
		assertEquals(1, itemService.getMusicAlbumByName("Chopin").size());
		assertEquals("Chopin Nocturnes", itemService.getMusicAlbumByName("Chopin").get(0).getName());
		
		// Newspaper
		assertEquals(1, itemService.getNewspaperByName("Gaz").size());
		assertEquals("Gazette", itemService.getNewspaperByName("Gaz").get(0).getName());

		lenient().when(newspaperDao.findNewspaperByNameContaining("Gazette")).thenAnswer(invocation -> {
			Newspaper news = new Newspaper();
			news.setName("Gazette");
			return Collections.singletonList(news);
		});
		assertEquals(1, itemService.getNewspaperByName("Gazette").size());

		lenient().when(musicAlbumDao.findByNameContaining("MyTunes")).thenAnswer(invocation -> {
			MusicAlbum news = new MusicAlbum();
			news.setName("MyTunes");
			return Collections.singletonList(news);
		});
		assertEquals(1, itemService.getMusicAlbumByName("MyTunes").size());

		lenient().when(archiveDao.findByNameContaining("The Archive")).thenAnswer(invocation -> {
			Archive news = new Archive();
			news.setName("The Archive");
			return Collections.singletonList(news);
		});
		assertEquals(1, itemService.getArchiveByName("The Archive").size());
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	public void testIllegalBookName(String name) {
		try {
			itemService.getBookByName(name);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	public void testIllegalNewspaperName(String name) {
		try {
			itemService.getNewspaperByName(name);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	public void testIllegalMusicAlbumName(String name) {
		try {
			itemService.getMusicAlbumByName(name);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	public void testIllegalMovieName(String name) {
		try {
			itemService.getMovieByName(name);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	public void testIllegalArchiveName(String name) {
		try {
			itemService.getArchiveByName(name);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
		
		// Archive
		assertEquals(1, itemService.getArchiveByName("Records").size());
		assertEquals("Records-2021", itemService.getArchiveByName("Records").get(0).getName());
		try {
			itemService.getArchiveByName(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
		try {
			itemService.getArchiveByName("");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item name cannot be empty!", e.getMessage());
		}
	}

	@Test
	public void testReserve() {

		assertEquals(BOOK_ID, itemService.reserveItem(BOOK_ID).getId());
		assertEquals(Status.RESERVED, itemService.reserveItem(BOOK_ID).getStatus());

		try {
			itemService.reserveItem(BAD_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}

		try {
			itemService.reserveItem(NONEXISTING_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item does not exist.", e.getMessage());
		}

		try {
			itemService.reserveItem(NEWSPAPER_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot reserve archives or newspapers.", e.getMessage());
		}

		try {
			itemService.reserveItem(ARCHIVE_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot reserve archives or newspapers.", e.getMessage());
		}

		try {
			itemService.reserveItem(ARCHIVE_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot reserve archives or newspapers.", e.getMessage());
		}
		
		try {
			itemService.reserveItem(MOVIE_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("This item is unavailable.", e.getMessage());
		}
	}

	@Test
	public void testCheckout() {
		
		assertEquals(BOOK_ID, itemService.checkoutItem(BOOK_ID, USER_ID).getId());
		assertEquals(Status.CHECKED_OUT, itemService.checkoutItem(BOOK_ID, USER_ID).getStatus());
	
		try {
			itemService.checkoutItem(BAD_ID, USER_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}

		try {
			itemService.checkoutItem(NONEXISTING_ID, USER_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item does not exist.", e.getMessage());
		}

		try {
			itemService.checkoutItem(NEWSPAPER_ID, USER_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot checkout archives or newspapers.", e.getMessage());
		}
		
		try {
			itemService.checkoutItem(ARCHIVE_ID, USER_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot checkout archives or newspapers.", e.getMessage());
		}

		try {
			itemService.checkoutItem(ARCHIVE_ID, USER_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot checkout archives or newspapers.", e.getMessage());
		}

		try {
			itemService.checkoutItem(MOVIE_ID, USER_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("This item is unavailable.", e.getMessage());
		}
	}
	
	@Test 
	public void testReturn() {
		assertEquals(MOVIE_ID, itemService.returnItem(MOVIE_ID).getId());
		assertEquals(Status.AVAILABLE, itemService.returnItem(MOVIE_ID).getStatus());
		
		try {
			itemService.returnItem(BOOK_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("You may only return checked out items.", e.getMessage());
		}
	}
	
	@Test
	public void testRenew() {
		assertEquals(MOVIE_ID, itemService.renewItem(MOVIE_ID).getId());
		assertEquals(Status.CHECKED_OUT, itemService.renewItem(MOVIE_ID).getStatus());
		
		try {
			itemService.renewItem(BOOK_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("You may only renew an item that is already checked out.", e.getMessage());
		}
	}

	@Test
	public void testGetBookByIllegalStatus() {
		try {
			itemService.getBookByStatus(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Status cannot be null.", e.getMessage());
		}
	}

	@Test
	public void testGetMovieByIllegalStatus() {
		try {
			itemService.getMovieByStatus(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Status cannot be null.", e.getMessage());
		}
	}

	@Test
	public void testGetMusicAlbumByIllegalStatus() {
		try {
			itemService.getMusicAlbumByStatus(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Status cannot be null.", e.getMessage());
		}
	}

	@Test
	public void testReturnNonExistentItem() {
		try {
			// no one is using id of 999999
			itemService.returnItem(999999);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item does not exist.", e.getMessage());
		}
	}

	@Test
	public void testReturnNonCheckedOutItem() {
		try {
			itemService.returnItem(BOOK_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("You may only return checked out items.", e.getMessage());
		}
	}

	@Test
	public void testReturnItem() {
		final Item item = itemService.returnItem(MOVIE_ID);
		assertEquals(item.getId(), MOVIE_ID);
		assertEquals(item.getStatus(), Status.AVAILABLE);
	}

	@Test
	public void testRenewNonExistentItem() {
		try {
			// no one is using id of 999999
			itemService.renewItem(999999);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item does not exist.", e.getMessage());
		}
	}

	@Test
	public void testRenewNonCheckedOutItem() {
		try {
			itemService.renewItem(BOOK_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("You may only renew an item that is already checked out.", e.getMessage());
		}
	}

	@Test
	public void testRenewItem() {
		final Item item = itemService.renewItem(MOVIE_ID);
		assertEquals(item.getId(), MOVIE_ID);
		assertEquals(item.getStatus(), Status.CHECKED_OUT);
	}

}
