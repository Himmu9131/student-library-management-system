
//Books
const findAllBooks = async () => {
  try {
    const url = await fetch("https://student-library-management-system-g2wr.onrender.com/book/api/findAll", {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
    });
    if (!url.ok) {
      throw new Error(`HTTP error! status: ${url.status}`);
    }
    const data = await url.json();
    console.log(data);
    let btable = document.getElementById("rows-bt");
    btable.innerHTML = "";
    data.forEach((book) => {
      let row = document.createElement("tr");
      // Create and append table cells
      let idCell = document.createElement("td");
      idCell.textContent = book.id;
      row.appendChild(idCell);

      let nameCell = document.createElement("td");
      nameCell.textContent = book.name;
      row.appendChild(nameCell);

      let genreCell = document.createElement("td");
      genreCell.textContent = book.genre;
      row.appendChild(genreCell);

      let pagesCell = document.createElement("td");
      pagesCell.textContent = book.pages;
      row.appendChild(pagesCell);

      let publisherNameCell = document.createElement("td");
      publisherNameCell.textContent = book.publisherName;
      row.appendChild(publisherNameCell);

      let isAvailableCell = document.createElement("td");
      isAvailableCell.textContent=isAvailableCell?"available":"Unavailable";
      row.appendChild(isAvailableCell);
      

      btable.appendChild(row);
    });
  } catch (error) {
    console.log(error);
  }
};

const addBook = async () => {
  const name = document.getElementById("book-name-add").value;
  const genre = document.getElementById("book-genre-add").value;
  const pages = document.getElementById("book-pages-add").value;
  const publisherName = document.getElementById("book-publisherName-add").value;
  const authorId = document.getElementById("a_id-add").value;
  const copies = document.getElementById("copies-add").value;

  const data = {
    name,
    genre,
    pages,
    publisherName,
    copies,
    authorId,
  };

  try {
    const resp = await fetch("https://student-library-management-system-g2wr.onrender.com/book/api/save", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(data),
    });
    if (resp.ok) {
      document.getElementById("add-form").reset();
      alert("Book Added Successfully!!");
      findAllBooks();
    } else {
      alert("Failed to add Book");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
};

const deleteBook = async () => {
  const id = document.getElementById("book-id-delete").value.trim();
  try {
    const resp = await fetch(`https://student-library-management-system-g2wr.onrender.com/book/api/delete/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    if (resp.ok) {
      document.getElementById("book-id-delete").reset();
      alert("Book deleted Successfully!!");
    } else {
      alert("Failed to delete Book.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
  findAllBooks();
};

const updateBook = async () => {  
  const id = document.getElementById("book-id-update").value;
  const name = document.getElementById("book-name-update").value;
  const genre = document.getElementById("book-genre-update").value;
  const pages = document.getElementById("book-pages-update").value;
  const publisherName = document.getElementById("book-publisherName-update").value;
  const authorId = document.getElementById("book-author-update").value;
  const copies = document.getElementById("book-copies-update").value;

  const data = {
    name,
    genre,
    pages,
    publisherName,
    copies,
    authorId,
  };

  try {
    const resp = await fetch(`https://student-library-management-system-g2wr.onrender.com/book/api/update/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(data),
    });
    if (resp.ok) {
      document.getElementById("update-form").reset();
      alert("Book Updated Successfully!!");
    } else {
      alert("Failed to update Book.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
  findAllBooks();
}

//Return Book

const returnBook = async () => {
  const bookId = document.querySelector("#book-id-r").value.trim();
  const cardId = document.querySelector("#card-id-r").value.trim();

  console.log("Book ID:", bookId);
  console.log("Card ID:", cardId);

  const data = {
    bookId,
    cardId,
  };

  try {
    const resp = await fetch(
      `https://student-library-management-system-g2wr.onrender.com/transaction/api/return?bookId=${bookId}&cardId=${cardId}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify(data),
      }
    );
    if (resp.ok) {
      document.getElementById("return-form").reset();
      alert("Book returned Successfully..!!");
    } else {
      alert("Failed to return book.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
};

//Issue Book

const issueBook = async () => {
  const bookId = document.querySelector("#book-id-i").value.trim();
  const cardId = document.querySelector("#card-id-i").value.trim();

  const data = {
    bookId,
    cardId,
  };

  try {
    const resp = await fetch(
      `https://student-library-management-system-g2wr.onrender.com/transaction/api/issue?bookId=${bookId}&cardId=${cardId}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    if (resp.ok) {
      document.getElementById("issue-form").reset();
      alert("Book Issued Successfully..!!");
    } else {
      alert("Failed to issue book.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
};

export {
  findAllBooks,
  addBook,
  deleteBook,
  updateBook,
  returnBook,
  issueBook,

};