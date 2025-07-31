//TRANSACTIONS

const findAllTransactions = async () => {
  try {
    const url = await fetch("http://localhost:8080/transaction/api/findAll", {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
    });
    if (!url.ok) {
      throw new Error(`HTTP error! status: ${url.status}`);
    }
    const data = await url.json();

     console.log(data);
    let trtable = document.getElementById("rows-trt");
    trtable.innerHTML = "";
    data.forEach((transaction) => {
      let row = document.createElement("tr");
      // id fine is_issue_operation trdate tr_status book_id card_id
      // Create and append table cells
      let idCell = document.createElement("td");
      idCell.textContent = transaction.id;
      row.appendChild(idCell);
       
      let bookCell = document.createElement("td");
      bookCell.textContent = transaction.bookName;
      row.appendChild(bookCell);

      let studentNameCell = document.createElement("td");
      studentNameCell.textContent = transaction.studentName;
      row.appendChild(studentNameCell);

      let cIdCell = document.createElement("td");
      cIdCell.textContent = transaction.cardId;
      row.appendChild(cIdCell);

      let fineCell = document.createElement("td");
      fineCell.textContent = "$" + transaction.fine;
      row.appendChild(fineCell);

      let issuedCell = document.createElement("td");
      issuedCell.textContent = transaction.issueOperation?"issued":"returned";
      row.appendChild(issuedCell);

      let tr_statusCell = document.createElement("td");
      tr_statusCell.textContent = transaction.transactionStatus;
      row.appendChild(tr_statusCell);

      let trDate = document.createElement("td");
      trDate.textContent = transaction.transactionDate.split("T")[0]; // Format date to YYYY-MM-DD
      row.appendChild(trDate);

      trtable.appendChild(row);
    });
  } catch (error) {
    console.log(error);
  }
};

const addTransaction = async () => {
  const fine = document.getElementById("fine").value;
  const issued = document.getElementById("issued").value;
  const bId = document.getElementById("bId").value;
  const cId = document.getElementById("cId").value;
  const status = document.getElementById("status").value;

  const data = {
    fine,
    issued,
    bId,
    cId,
    status,
  };

  try {
    const resp = await fetch("http://localhost:8080/transaction/api/save", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(data),
    });
    if (resp.ok) {
      document.getElementById("transaction-form").reset();
      alert("Transaction Added Successfully!!");
      findAllTransactions();
    } else {
      alert("Failed to DO transaction.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
};

function booksBorrowed() {
  fetch("http://localhost:8080/transaction/api/totalBorrowedBooks", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + localStorage.getItem("token"),
    },
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("Total Borrowed Books:", data);
      document.getElementById("borrowedBooks").innerText = data;
    })
    .catch((error) => console.error("Error fetching total borrowed books:", error));
}


export {
  findAllTransactions,
    addTransaction,booksBorrowed};