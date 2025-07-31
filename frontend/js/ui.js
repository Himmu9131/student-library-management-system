
function setupRoleBasedUI(role) {
  if (!role) {
    // No role: hide all protected navs and redirect to login
    document.querySelectorAll(".nav-link.protected").forEach((el) => {
      el.style.display = "none";
    });
  }

  // Common DOM elements
  const studentsNav = document.getElementById("nav-students");
  const transactionsNav = document.getElementById("nav-transactions");
  const booksNav = document.getElementById("nav-books");
  const authorsNav = document.getElementById("nav-authors");
  const listCol = document.getElementById("list-column");

  if (role === "STUDENT") {
    if (studentsNav) studentsNav.style.display = "none";
    if (transactionsNav) transactionsNav.style.display = "none";
    if (booksNav) booksNav.textContent = "Books";
    if (authorsNav) authorsNav.textContent = "Authors";

    // Hide all forms under list-container
    document.querySelectorAll(".list-container").forEach((el) => {
      el.style.display = "none";
    });

    // Expand content area if list-column exists
    if (listCol) {
      listCol.classList.remove("col-md-6");
      listCol.classList.add("col-md-12");
    }

  } else if (role === "ADMIN") {
    if (studentsNav) studentsNav.style.display = "block";
    if (transactionsNav) transactionsNav.style.display = "block";

    // Show list-container elements
    document.querySelectorAll(".list-container").forEach((el) => {
      el.style.display = "block";
    });

    // Reset layout if needed
    if (listCol) {
      listCol.classList.remove("col-md-12");
      listCol.classList.add("col-md-6");
    }

}
  }




function restrictAccessIfNotLoggedIn() {
  const token = localStorage.getItem("token");
  const protectedLinks = document.querySelectorAll(".nav-link.protected");

  protectedLinks.forEach((link) => {
    if (!token) {
      link.onclick = function (e) {
        e.preventDefault();
        showLoginToast();
      };
    } else {
      link.onclick = null; // Allow normal navigation
    }
  });
}


// function showForm(type) {
//   document.getElementById("add-form").style.display = "none";
//   document.getElementById("update-form").style.display = "none";
//   document.getElementById("delete-form").style.display = "none";

//   if (type === "add") {
//     document.getElementById("add-form").style.display = "block";
//   } else if (type === "update") {
//     document.getElementById("update-form").style.display = "block";
//   } else if (type === "delete") {
//     document.getElementById("delete-form").style.display = "block";
//   }
// }

function showLoginToast() {
  const toastEl = document.getElementById("loginToast");
  const toast = new bootstrap.Toast(toastEl,{
    autohide: true,
    delay: 800, // Show for 3 seconds
  });
  toast.show();
}

  function showForgotPasswordForm() {

      console.log("Forgot password clicked");

  const login = document.getElementById("login-form");
  const forgot = document.getElementById("forgot-password-form");

  if (login && forgot) {
    login.style.display = "none";
    forgot.style.display = "block";
  }
}

function showLoginForm() {

  console.log("Show login form clicked");

  const login = document.getElementById("login-form");
  const forgot = document.getElementById("forgot-password-form");

  if (login && forgot) {
    login.style.display = "block";
    forgot.style.display = "none";
  }
}

 function setupNavbarAccessControl() {
  const token = localStorage.getItem("token");

  // If no token, restrict access to nav links
  if (!token) {
  
    restrictAccessIfNotLoggedIn(); // Don't immediately call loadPage here

  }
  
}


 function setActiveNavLink(pageUrl) {
  const navLinks = document.querySelectorAll(".nav-link");

  navLinks.forEach(link => {
    link.classList.remove("active"); // 🧼 Always clear old active first

    const targetPage = link.getAttribute("data-page");
    if (targetPage === pageUrl) {
      link.classList.add("active");  // ✅ Then add to the matching one
    }
  });
}

export{setupRoleBasedUI,restrictAccessIfNotLoggedIn,setupNavbarAccessControl,showLoginToast,setActiveNavLink};