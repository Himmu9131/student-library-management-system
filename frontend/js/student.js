//  Student api's..

const findAllStudents = async () => {
  try {
    const response = await fetch("http://localhost:8080/student/api/findAll", {
      method: "GET",
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();

    let sttable = document.getElementById("rows-st");
    sttable.innerHTML = "";
    data.forEach((student) => {
      let row = document.createElement("tr");

      // Create and append table cells
      let idCell = document.createElement("td");
      idCell.textContent = student.id;
      row.appendChild(idCell);

      let nameCell = document.createElement("td");
      nameCell.textContent = student.name;
      row.appendChild(nameCell);

       let cardIdCell = document.createElement("td");
      cardIdCell.textContent = student.cardId;
      row.appendChild(cardIdCell);

      let ageCell = document.createElement("td");
      ageCell.textContent = student.age;
      row.appendChild(ageCell);

      let mailCell = document.createElement("td");
      mailCell.textContent = student.email;
      row.appendChild(mailCell);

      let mobCell = document.createElement("td");
      mobCell.textContent = student.mobile;
      row.appendChild(mobCell);

      let addressCell = document.createElement("td");
      addressCell.textContent = student.address;
      row.appendChild(addressCell);

     

      sttable.appendChild(row);
    });
  } catch (error) {
    console.log(error);
  }
};

const addStudent = async () => {
  const name = document.getElementById("add-name").value;
  const age = document.getElementById("add-age").value;
  const email = document.getElementById("add-email").value;
  const mobile = document.getElementById("add-mobile").value;
  const address = document.getElementById("add-address").value;

  const data = {
    name,
    age,
    email,
    mobile,
    address,
  };

  try {
    const resp = await fetch("http://localhost:8080/student/api/save", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(data),
    });
    if (resp.ok) {
      document.getElementById("add-form").reset();
      alert("Student Added Successfully!!");
      findAllStudents();
    } else {
      alert("Failed to add student.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
};

const deleteStudent = async () => {
  const id = document.querySelector("#delete-form #delete-id").value.trim();
  try {
    const resp = await fetch(`http://localhost:8080/student/api/delete/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    if (resp.ok) {
      document.getElementById("delete-form").reset();
      alert("Student deleted Successfully!!");
    } else {
      alert("Failed to delete student.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
  findAllStudents();
};

const updateStudent = async () => {
  const id = document.querySelector("#update-form #update-id").value.trim();
  const name = document.querySelector("#update-form #update-name").value;
  const age = document.querySelector("#update-form #update-age").value;
  const email = document.querySelector("#update-form #update-email").value;
  const mobile = document.querySelector("#update-form #update-mobile").value;
  const address = document.querySelector("#update-form #update-address").value;

  const data = {
    id,
    name,
    age,
    email,
    mobile,
    address,
  };

  try {
    const resp = await fetch(`http://localhost:8080/student/api/update/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(data),
    });
    if (resp.ok) {
      document.getElementById("update-form").reset();
      alert("Student Updated Successfully!!");
    } else {
      alert("Failed to update student.");
    }
  } catch (error) {
    console.log("error:", error);
    alert("Something went wrong!!");
  }
  findAllStudents();
};


export {findAllStudents,addStudent,deleteStudent,updateStudent};