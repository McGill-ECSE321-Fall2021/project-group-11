package ca.mcgill.ecse321.townlibrary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

import java.util.*;

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
import org.mockito.stubbing.Answer;

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

	@InjectMocks
	private ItemService itemService;

	private static final Transaction TRANSACTION = new Transaction();

	private static final int BAD_ID = -1;
	private static final int NONEXISTING_ID = 0;
	private static final int BOOK_ID = 11;	// available
	private static final int MOVIE_ID = 22;	// unavailable
	private static final int NEWSPAPER_ID = 33;
	private static final int ARCHIVE_ID = 44;

	@BeforeEach
	public void setMockOutput() {
		lenient().when(itemDao.findItemById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(BOOK_ID)) {
				Book book = new Book();
				book.setId(BOOK_ID);
				book.setName("Dune");
				book.setTransaction(TRANSACTION);
				book.setStatus(Status.AVAILABLE);
				return book;
			}
			else if (invocation.getArgument(0).equals(MOVIE_ID)) {
				Movie movie = new Movie();
				movie.setId(MOVIE_ID);
				movie.setName("Interstellar");
				movie.setStatus(Status.CHECKED_OUT);
				return movie;
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
				archive.setName("The Archive");
				archive.setStatus(Status.AVAILABLE);
				return archive;
			}
			else {
				return null;
			}
		});
		lenient().when(itemDao.findItemByTransaction(any(Transaction.class))).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(TRANSACTION)) {
				Book book = new Book();
				book.setTransaction(TRANSACTION);
				return book;
			} else {
				return null;
			}
		});
		lenient().when(itemDao.existsById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(BOOK_ID) ||
				invocation.getArgument(0).equals(MOVIE_ID) ||
				invocation.getArgument(0).equals(NEWSPAPER_ID) ||
				invocation.getArgument(0).equals(ARCHIVE_ID)) {
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
				book.setTransaction(TRANSACTION);
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

				List<Movie> mList = new ArrayList<Movie>();
				mList.add(movie);
				return mList;
			} else {
				return new ArrayList<Movie>();
			}
		});
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

		assertEquals(TRANSACTION, itemService.getItemByTransaction(TRANSACTION).getTransaction());

		try {
			itemService.getItemByTransaction(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Transaction cannot be null!", e.getMessage());
		}
	}

	@Test
	public void testGetItemByName() {
		assertEquals(1, itemService.getBookByName("Dune").size());
		assertEquals(1, itemService.getMovieByName("stellar").size());
		assertEquals("Interstellar", itemService.getMovieByName("stellar").get(0).getName());

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
			itemService.reserveItem(MOVIE_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("This item is unavailable.", e.getMessage());
		}
	}

	@Test
	public void testCheckout() {

		assertEquals(BOOK_ID, itemService.checkoutItem(BOOK_ID).getId());
		assertEquals(Status.CHECKED_OUT, itemService.checkoutItem(BOOK_ID).getStatus());

		try {
			itemService.checkoutItem(BAD_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported Id.", e.getMessage());
		}

		try {
			itemService.checkoutItem(NONEXISTING_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Item does not exist.", e.getMessage());
		}

		try {
			itemService.checkoutItem(NEWSPAPER_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot checkout archives or newspapers.", e.getMessage());
		}

		try {
			itemService.checkoutItem(ARCHIVE_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot checkout archives or newspapers.", e.getMessage());
		}

		try {
			itemService.checkoutItem(MOVIE_ID);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("This item is unavailable.", e.getMessage());
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

}
