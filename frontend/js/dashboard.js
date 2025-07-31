  

function totalBooks() {
  fetch("http://localhost:8080/book/api/totalBooks", {
    method: "GET", // Moved here
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + localStorage.getItem("token"),
    },
  })
    .then((response) => response.json())
    .then((data) => {
      document.getElementById("totalBooks").innerText = data;
    })
    .catch((error) => console.error("Error fetching total books:", error));
}

function booksBorrowed() {
  fetch("http://localhost:8080/book/api/borrowedBooks", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + localStorage.getItem("token"),
    },
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("Borrowed books:", data);
      document.getElementById("booksBorrowed").innerText = data;
    })
    .catch((error) => console.error("Error fetching total books:", error));
}

function totalBorrowers() {
  fetch("http://localhost:8080/transaction/api/totalBorrowers", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + localStorage.getItem("token"),
    },
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("Total Borrowers:", data);
      document.getElementById("totalBorrowers").innerText = data;
    })
    .catch((error) => console.error("Error fetching total books:", error));
}

function overdueTransactions() {
  fetch("http://localhost:8080/transaction/api/overdueTransactions", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + localStorage.getItem("token"),
    },
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("OverDue Transactions:", data);
      document.getElementById("overdue-count").textContent = data!=null?data:0;
    })
    .catch((error) => console.error("Error fetching total books:", error));
}

const popularBooks = async () => {
  try {
    const url = await fetch(
      "http://localhost:8080/transaction/api/popular-books",
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      }
    );
    if (!url.ok) {
      throw new Error(`HTTP error! status: ${url.status}`);
    }
    const data = await url.json();
    console.log("Popular Books:", data);

    let popBook_list = document.getElementById("popularBooks");
    if (popBook_list) popBook_list.innerHTML = ""; // clear previous list items

    data.forEach((book_names) => {
      const nameList = document.createElement("li");
      nameList.textContent = book_names;
      popBook_list.appendChild(nameList);
    });
  } catch (error) {
    console.log(error);
  }
};

async function loadRecentActivity() {
  try {
    const response = await fetch("http://localhost:8080/transaction/api/recent-activity", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    if (!response.ok) throw new Error("Failed to fetch recent activity");

    const data = await response.json(); // List of RecentActivityDto
    console.log("Recent Activity:", data);

    const activityList = document.getElementById("recentActivity");
    if (!activityList) {
      console.warn("Element with ID 'recentActivity' not found in DOM.");
      return;
    }

    activityList.innerHTML = "";

   console.log(data);

    data.forEach((item) => {
      const li = document.createElement("li");
      const action = item.issueOperation?"issued":"returned";
      li.textContent = `${item.studentName} has ${action} "${item.bookName}"`;
      activityList.appendChild(li);
    });

  } catch (err) {
    console.error("Error loading recent activity:", err);
  }
}



export {
  totalBooks,   
    booksBorrowed,
    totalBorrowers,
    overdueTransactions,
    popularBooks,
    loadRecentActivity,
};