import { findAllBooks} from "./book.js";
import { totalBooks ,loadRecentActivity,booksBorrowed,totalBorrowers,overdueTransactions,popularBooks} from "./dashboard.js";
import {login} from "./auth.js";
import { findAllStudents } from "./student.js";
import {findAllTransactions} from "./transaction.js";
import { findAllAuthors } from "./author.js";
import { setupRoleBasedUI,restrictAccessIfNotLoggedIn, setActiveNavLink } from "./ui.js";
import { attachAuthListeners } from "./auth.js";

 function loadPage(page) {
  console.log("📥 loadPage called with:", page);
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  console.log("Token in loadPage+++_+_+_+++_:", token);

  fetch("html/" + page)
    .then((res) => res.text())
    .then((data) => {
      document.getElementById("main-content").innerHTML = data;
      const body = document.getElementById("main-body");
       
      attachAuthListeners();





       
      // Handle background
      if (page === "login.html") {
        body.classList.add("login-bg");
      } else {
        body.classList.remove("login-bg");
      }

      // Hide/show navbar links
      const navLinks = document.getElementsByClassName("nav-link");
      for (let i = 0; i < navLinks.length; i++) {
        navLinks[i].style.display = page === "login.html" ? "none" : "block";
      }

      // Role-based navbar visibility
      setupRoleBasedUI(role); // ✅ must pass role
        setTimeout(() => {
      setActiveNavLink(page);
        }, 0);      // Attach auth listeners only when login page is loaded
      if (page === "login.html") {
        import("./auth.js").then((module) => {
          if (module.attachAuthListeners) {
            module.attachAuthListeners();


          } else {
            console.warn("attachAuthListeners not found in auth.js");
          }
        });
      }

      // Handle login form events
      const loginForm = document.getElementById("loginForm");
      if (loginForm) {
        loginForm.addEventListener("submit", login);
        loginForm.reset();
      }

      // Tab support
      const tabTriggerEls = document.querySelectorAll('[data-bs-toggle="tab"]');
      tabTriggerEls.forEach((tabEl) => new bootstrap.Tab(tabEl));

     

      // Dynamic page-specific logic
      if (page === "student.html") {
        findAllStudents();
      } else if (page === "author.html") {
        findAllAuthors();
      } else if (page === "book.html") {
        findAllBooks();
      } else if (page === "transaction.html") {
        findAllTransactions();
      } else if (page === "dashboard.html" && role === "ADMIN") {
        totalBooks();
        booksBorrowed();
        totalBorrowers();
        
        popularBooks();
        loadRecentActivity();
      } else if (page === "student-dashboard.html" && role === "STUDENT") {
        loadStudentDashboard();
        
        overdueTransactions();
      }
      

      // Map page to nav link ID


    })
    .catch((err) => {
      console.error("❌ Error loading page:", err);
    });
}


async function loadStudentDashboard() {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  if (!token) return alert("Unauthorized access");

  // Extract student ID from token
  const payload = JSON.parse(atob(token.split(".")[1]));
  const studentId = payload.sub; // Assuming 'sub' is student ID
  
  try {
    const response = await fetch(`https://student-library-management-system-g2wr.onrender.com/dashboard/student`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log("PayLoad is--------", payload);
    if (!response.ok) throw new Error("Failed to fetch student dashboard");

    const data = await response.json();
    console.log("Student Dashboard Data:", data);

    // Inject into DOM
    const welcomeEl = document.getElementById("welcome");
    if (!welcomeEl) {
      console.warn("Dashboard elements not yet rendered.");
      return;
    }

    document.getElementById("borrowed-count").textContent = data.booksBorrowed;
    document.getElementById("fine-amount").textContent = `${data.totalFine}`;
    document.getElementById("overdue-count").textContent = data.overdueBooks;
    document.getElementById("welcome").textContent = data.welcomeMessage;

    const table = document.getElementById("my-books");
    table.innerHTML = ""; // Clear previous content

    data.currentBorrowedBooks.forEach((book) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${book.bookName}</td>
    <td>${new Date(book.issuedDate).toLocaleDateString()}</td>
    <td>${new Date(book.dueDate).toLocaleDateString()}</td>
    <td>${book.overdue ? "₹ 50" : "₹ 0"}</td>
    <td>${book.overdue ? "Overdue" : "On Time"}</td>
    <td><button class="btn btn-danger btn-sm">Return</button></td>
      `;
      table.appendChild(row);

      // Ensure UI is set up for student role
    });
    // Load the student dashboard page
    console.log("Student dashboard loaded successfully");
    setupRoleBasedUI(role); // Ensure role-based UI is set up for student
    restrictAccessIfNotLoggedIn(); // Ensure access control is applied

    // Load the dashboard page after fetching data
  } catch (error) {
    console.error("Error loading student dashboard:", error);
    alert("Failed to load student dashboard.");
  }
}

export { loadPage, loadStudentDashboard };