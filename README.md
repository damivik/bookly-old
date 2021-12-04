FORMAT: 1A
HOST: http://localhost:8080/api/

# bookly

Bookly is a simple API that helps users retrieve information on books and manage personal bookshelves.

## Users [/users]

### Create a New User [POST]

You may create a new user using this action. It takes a JSON
object containing the users email and password.

+ Request (application/json)

        {
            "email": "johndoe@example.com",
            "username": "johndoe",
            "password": "password"
        }

+ Response 201 (application/json)

    + Headers

            Location: /users/2

## User [/users/{userId}]

+ Parameters
    + userId (number) - ID of user in the form of an integer

### Update User Details [PATCH]

You may update an existing user account using this action. It takes a JSON
object containing the field(s) to be updated and their values.

+ Request (application/json)

    + Headers

            Authorization: Basic ABC123
    
    + Body
    
            {
                "email" : "new_email@example.com",
                "username": "new_username
            }

+ Response 401

    + Headers

            WWW-Authenticate: Basic realm="protected"
            
+ Response 204

            
### Delete User [DELETE]

You may a delete an existing user account using this action.

+ Request

    + Headers

            Authorization: Basic ABC123

+ Response 401

    + Headers

            WWW-Authenticate: Basic realm="protected"


+ Response 204

## Books [/books{?q}]

### Search Books [GET]

+ Parameters
    + q (string, required) - Search string(Book title)

+ Response 200 (application/json)

        {
            "books" : 
                [
                    {
                        "authors" : "J. K. Rowling",
                        "id" : 35,
                        "title" : "Harry Potter and the Philosopher's Stone"
                    },
                    {
                        "authors" : "J. K. Rowling",
                        "id" : 36,
                        "title" : "Harry Potter and the Chamber of Secrets"
                    },
                    {
                        "authors" : "J. K. Rowling",
                        "id" : 37,
                        "title" : "Harry Potter and the Prisoner of Azkaban"
                    },
                    {
                        "authors" : "J. K. Rowling",
                        "id" : 38,
                        "title" : "Harry Potter and the Goblet of Fire"
                    },
                    {
                        "authors" : "J. K. Rowling",
                        "id" : 39,
                        "title" : "Harry Potter and the Order of the Phoenix"
                    },
                    {
                        "authors" : "J. K. Rowling",
                        "id" : 40,
                        "title" : "Harry Potter and the Half-Blood Prince"
                    },
                    {
                        "authors" : "J. K. Rowling",
                        "id" : 41,
                        "title" : "Harry Potter and the Deathly Hallows"
                    }
                ],
            "count" : 7
        }
        
## Book [/books/{bookId}]

### Retrieve book [GET]

+ Response 200 (application/json)

    + Body

            {
                "authors" : "J. K. Rowling",
                "id" : 35,
                "title" : "Harry Potter and the Philosopher's Stone"
            }


## Bookshelves [/bookshelves/{?userId}]

### Create Bookshelf [POST]

+ Request (application/json)

    + Headers

            Authorization: Basic ABC123
    
    + Body
    
            {
                "name": "Fiction"
            }

+ Response 201 (application/json)

    + Headers
    
            Location: /bookshelves/47
            
### List Bookshelves [GET]

+ Parameters
    + userId (Integer, required) - ID of user
        
+ Request (application/json)

    + Headers

            Authorization: Basic ABC123

+ Response 200 (application/josn)

    + Body
    
            {
                "bookshelves" :
                    [
                        {
                            "bookCount" : 10,
                            "id" : 12,
                            "name" : "Non-fiction"
                        },
                        {
                            "bookCount" : 10,
                            "id" : 23,
                            "name" : "Mystery/Thrillers"
                        },
                        {
                            "bookCount" : 10,
                            "id" : 34,
                            "name" : "Classics"
                        },
                        {
                            "bookCount" : 10,
                            "id" : 45,
                            "name" : "Fantasy"
                        }
                    ],
                "items" : 4
            }

## Bookshelf [/bookshelves/{bookshelfId}]

+ Parameters
    + bookshelfId (number) - ID of the bookshelf

### Update Bookshelf [PATCH]

+ Request (application/json)

    + Headers

            Authorization: Basic ABC123
    
    + Body
    
            {
                "name": "Fiction"
            }
    
+ Response 200 (application/json)

    + Body
    
            {
                "booksCount" : 10,
                "id" : 12,
                "name" : "Non Fiction",
                "userId" : 12
            }

### Retrieve Bookshelf [GET]

+ Request

    + Headers
    
            Authorization: Basic ABC123
        
+ Response 200 (application/json)

    + Body
    
            {
                "booksCount" : 10,
                "id" : 23,
                "name" : "Non Fiction",
                "userId" : 23
            }

## Bookshelf -> Books [/bookshelves/{bookshelfId}/books]

+ Parameters
    + bookshelfId (number) - ID of the bookshelf

### Retrieve a list books on a bookshelf [GET]

+ Request

    + Headers
    
            Authorization: Basic ABC123
            
+ Response 200 (application/json)

    + Body
    
            {
                "books" :
                    [
                        {
                            "authors" : "Gillian Flynn",
                            "id" : 13,
                            "title" : "Gone Girl"
                        },
                        {
                            "authors" : "Paula Hawkins",
                            "id" : 14,
                            "title" : "The Girl on the Train"
                        },
                        {
                            "authors" : "Dan Brown",
                            "id" : 15,
                            "title" : "The Da Vinci Code"
                        },
                        {
                            "authors" : " Stieg Larsson",
                            "id" : 16,
                            "title" : "The Girl with the Dragon Tattoo"
                        },
                        {
                            "authors" : " Alex Michaelides",
                            "id" : 17,
                            "title" : "The Silent Patient"
                        },
                        {
                            "authors" : "Agatha Christie",
                            "id" : 18,
                            "title" : "And Then There Were None"
                        },
                        {
                            "authors" : "A.J. Finn",
                            "id" : 19,
                            "title" : "The Woman in the Window"
                        },
                        {
                            "authors" : "Agatha Christie",
                            "id" : 20,
                            "title" : "Murder on the Orient Express"
                        },
                        {
                            "authors" : "Tana French",
                            "id" : 21,
                            "title" : "In the Woods"
                        },
                        {
                            "authors" : "Robert Galbraith",
                            "id" : 22,
                            "title" : "The Cuckoo's Calling"
                        }
                    ],
                "count" : 10
            }

### Adding a book to bookshelf [POST]

+ Request (application/json)

    + Headers

            Authorization: Basic ABC123
    
    + Body
    
            {
                "bookId": 1
            }

+ Response 201 (application/json)

    + Headers
    
            Location: /bookshelves/47/books/1

## Bookshelf -> Book [/bookshelves/{bookshelfId}/books/{bookId}]

+ Parameters
    + bookshelfId (number) - ID of the bookshelf
    + bookId (number) - ID of the book

### Removing a book from a bookshelf [DELETE]

+ Request (application/json)

    + Headers

            Authorization: Basic ABC123

+ Response 204
