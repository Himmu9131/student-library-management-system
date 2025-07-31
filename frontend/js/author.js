//Author
const findAllAuthors = async () => {
  try {
    const url = await fetch("http://localhost:8080/author/api/findAll", {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
    });
    if (!url.ok) {
      throw new Error(`HTTP error! status: ${url.status}`);
    }
    const data = await url.json();

    let atable = document.getElementById("rows-at");
    atable.innerHTML = "";
    data.forEach((author) => {
      let row = document.createElement("tr");

      // Create and append table cells
      let idCell = document.createElement("td");
      idCell.textContent = author.id;
      row.appendChild(idCell);

      let nameCell = document.createElement("td");
      nameCell.textContent = author.name;
      row.appendChild(nameCell);

      let mailCell = document.createElement("td");
      mailCell.textContent = author.email;
      row.appendChild(mailCell);

      let ratingCell = document.createElement("td");
      ratingCell.textContent = author.rating;
      row.appendChild(ratingCell);

      atable.appendChild(row);
    });
  } catch (error) {
    console.log(error);
  }
};

const addAuthor = async () => {
  const name = document.getElementById("author-name").value;
  const country = document.getElementById("author-country").value;
  const email = document.getElementById("author-email").value;
  const rating = document.getElementById("author-rating").value;

  const data = {
    name,
    country,
    email,
    rating,
  };

  try {
    const resp = await fetch("http://localhost:8080/author/api/save", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(data),
    });
    if (resp.ok) {
      document.getElementById("author-form").reset();
      alert("Author Added Successfully!!");
      findAllAuthors();
    } else {
      alert("Failed to add author.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
};

export {
  findAllAuthors,
    addAuthor
    // You can add more functions related to authors here
};