package org.chaptertwo.exampletwo.bookstore;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.springframework.web.context.annotation.RequestScope;

@Path("books")
@RequestScope
public class BookResource {

	@Inject
	private Bookshelf bookshelf;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response books() {
		return Response.ok(bookshelf.findAll()).build();
	}

	@GET
	@Path("/{isbn}")
	public Response get(@PathParam("isbn") String isbn) {
		Book book = bookshelf.findByISBN(isbn);
		return Response.ok(book).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Book book) {
		if (bookshelf.exists(book.getIsbn())) {
			return Response.status(Response.Status.CONFLICT).build();
		}
		bookshelf.create(book);
		URI location = UriBuilder.fromResource(BookResource.class).path("/{isbn}")
				.resolveTemplate("isbn", book.getIsbn()).build();
		return Response.created(location).build();
	}

	@PUT
	@Path("/{isbn}")
	public Response update(@PathParam("isbn") String isbn, Book book) {
		bookshelf.update(isbn, book);
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{isbn}")
	public Response delete(@PathParam("isbn") String isbn) {
	bookshelf.delete(isbn);
	return Response.ok().build();
	}
}

