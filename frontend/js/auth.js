import { changePassword, extractRoleFromToken } from "./utils.js";
import { loadPage} from "./router.js";
import { setActiveNavLink, setupRoleBasedUI} from "./ui.js";
import {
  returnBook,
  issueBook,
  addBook,
  updateBook,
  deleteBook,
} from "./book.js";
import { addAuthor } from "./author.js";
import { addStudent, updateStudent, deleteStudent } from "./student.js";

function attachAuthListeners() {
  const forgotLink = document.getElementById("forgot-link");
  const backToLoginBtn = document.getElementById("back-to-login");

  if (forgotLink) {
    forgotLink.addEventListener("click", (e) => {
      e.preventDefault();
      showForgotPasswordForm();
    });
  }

  if (backToLoginBtn) {
    backToLoginBtn.addEventListener("click", () => {
      document.getElementById("forgot-form-section").style.display = "none";
      document.getElementById("login-form-section").style.display = "block";
    });
  }

  const deleteButton = document.getElementById("delete-book");
  if (deleteButton) {
    deleteButton.addEventListener("click", deleteBook);
  }

  const issueButton = document.getElementById("issue-book");
  if (issueButton) {
    issueButton.addEventListener("click", issueBook);
  }

  const returnButton = document.getElementById("return-book");
  if (returnButton) {
    returnButton.addEventListener("click", returnBook);
  }

  const addButton = document.getElementById("add-book");
  if (addButton) {
    addButton.addEventListener("click", addBook);
    console.log("Add book button listener attached");
    console.log("Add book button: clicked");
  }

  const updateButton = document.getElementById("update-book");
  if (updateButton) {
    updateButton.addEventListener("click", updateBook);
  }

  const addAuthorBtn = document.getElementById("addAuthor");
  if (addAuthorBtn) {
    addAuthorBtn.addEventListener("click", addAuthor);
  }

  const addStudentBtn = document.getElementById("add-student");
  if (addStudentBtn) {
    addStudentBtn.addEventListener("click", addStudent);
  }

  const updateStudentBtn = document.getElementById("update-student");
  if (updateStudentBtn) {
    updateStudentBtn.addEventListener("click", updateStudent);
  }

  const deleteStudentBtn = document.getElementById("delete-student");
  if (deleteStudentBtn) {
    deleteStudentBtn.addEventListener("click", deleteStudent);
  }

  const changePasswordBtn = document.getElementById("update-password");
  if (changePasswordBtn) {
    changePasswordBtn.addEventListener("click", changePassword);
  }


 const sendResetBtn = document.getElementById("reset-link-send");
  const emailInput = document.getElementById("forgot-email");

  if (emailInput && sendResetBtn) {
    sendResetBtn.addEventListener("click", sendResetLink);
  }



  //   const addAuthorBtn= document.getElementById("add-book");
  //   if (addAuthorBtn) {
  //     addAuthorBtn.addEventListener("click", addBook);
  //   }

  // const requestBookBtn = document.getElementById("request-book");
  // if (requestBookBtn) {
  //     requestBookBtn.addEventListener("click", (e) => {
  //     e.preventDefault();
  //     const bookId = document.getElementById("book-id").value.trim();

  //     const loginForm = document.getElementById("loginForm");
  //     if (loginForm) {
  //       loginForm.addEventListener("submit", login); //  submit not click
  //       loginForm.reset();
  //     }
  //     const sendLinkBtn = document.getElementById("reset-link-send");
  // if (sendLinkBtn) {
  //   sendLinkBtn.addEventListener("click", (e) => {
  //     e.preventDefault();

  //     sendResetLink(); //  triggers the function
  //   });
  // }
}

function showForgotPasswordForm() {
  const login = document.getElementById("login-form-section");
  const forgot = document.getElementById("forgot-form-section");

  if (login && forgot) {
    login.style.display = "none";
    forgot.style.display = "block";
    forgot.style.placeContent = "center";
    forgot.style.justifyContent = "center";
  }
}

async function login(event) {
  event.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  console.log(email+" "+password);

  try {

        const payload = { username: email, password };
console.log("Final payload sent to backend:", JSON.stringify(payload));

    const response = await fetch("https://student-library-management-system-g2wr.onrender.com/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },

      body: JSON.stringify({ username: email, password }),
    });
    console.log("Raw response:", response);

    if (response.ok) {
      // Only parse if OK
      const result = await response.json();
      console.log("Login succesful:", result);
      // Save token to localStorage
      const token = result.token;
      localStorage.setItem("token", token);
      // Extract and save role from token
      const role = extractRoleFromToken();
      console.log("Role extracted:", role);
      localStorage.setItem("role", role);

      console.log("Login Successful, redirecting...");

      if (role === "STUDENT") {
        loadPage("student-dashboard.html");
        setupRoleBasedUI(role);
        attachAuthListeners();
        setActiveNavLink("loadStudentDashboard.html");
      } else if (role === "ADMIN") {
        loadPage("dashboard.html");
        setupRoleBasedUI(role);
        attachAuthListeners();
        setActiveNavLink("loadStudentDashboard.html");

      }
    } else {
      const errorText = await response.text(); // Safely read error as text
      alert("Login failed: " + errorText);
      console.error("Login failed with status:", response.status, errorText);
    }
  } catch (error) {
    console.error("Fetch error:", error);
    alert("Something went wrong. Try again later.");
  }
}

function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("role");
  loadPage("login.html");
  setupRoleBasedUI(null); // Reset UI after logout
  const nav = document.getElementById("nav-link");
  if (nav) {
    nav.style.display = "none"; // Hide navbar on logout
  }
}

async function sendResetLink() {
   const button=document.getElementById("reset-link-send")
   const emailInput = document.getElementById("forgot-email");
   const email = emailInput.value.toLowerCase().trim();
   button.disabled = true;
   button.innerText = "Sending...";

        if (!email) {
           alert("Please enter your email.");
           return;
        }

  fetch("https://student-library-management-system-g2wr.onrender.com/auth/forgot-password", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email }),
  })
    .then((res) => res.text())
    .then((data) => alert(data))
    .catch((err) => alert("Error: " + err.message));
}

function isUserLoggedIn() {
  return localStorage.getItem("token") !== null;
}

function protectNavLinks() {
  const token = localStorage.getItem("token");

  const protectedLinks = document.querySelectorAll(".nav-link.protected");

  if (!token) {
    protectedLinks.forEach((link) => {
      // Disable actual click with CSS
      link.style.pointerEvents = "auto"; // Ensure pointer works
      link.onclick = (e) => {
        e.preventDefault();
        showLoginToast();
      };
    });
  } else {
    protectedLinks.forEach((link) => {
      // Re-enable navigation
      link.style.dispalay = null; // restore default onclick
    });
  }
}

function forgotPass(e) {
  e.preventDefault();
  const email = prompt("Enter your registered email:");

  if (email) {
    fetch("https://student-library-management-system-g2wr.onrender.com/auth/forgot-password", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email: email }),
    })
      .then((res) => {
        if (res.ok) {
          alert("✅ Temporary password sent to your email.");
        } else {
          return res.text().then((text) => {
            throw new Error(text);
          });
        }
      })
      .catch((err) => {
        alert("❌ Error: " + err.message);
      });
  }
}

// function restrictAccessIfNotLoggedIn() {
//   const token = localStorage.getItem("token");
//   const protectedLinks = document.querySelectorAll(".nav-link.protected");

//   if (!token) {
//     setInterval(() => {
//       protectedLinks.forEach((link) => {
//         link.onclick = function (e) {
//           e.preventDefault();
//           showLoginToast();
//         };
//       });
//     }, 1); // Set timeout for 500ms or adjust as needed
//   } else {
//     protectedLinks.forEach((link) => {
//       link.onclick = null; // Restore default onclick behavior
//     });
//   }
// }

export {
  logout,
  protectNavLinks,
  login,
  attachAuthListeners,
  showForgotPasswordForm,
  forgotPass,
  sendResetLink,
  isUserLoggedIn,
};
