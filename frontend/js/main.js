import { loadPage } from "./router.js";
import {logout,protectNavLinks} from "./auth.js";
import { setupRoleBasedUI ,setActiveNavLink} from "./ui.js";


function applyInitialRouting() {
      const token = localStorage.getItem("token");
      if(!token) {
   // Call this to restrict access
        // restrictAccessIfNotLoggedIn();
        loadPage("login.html"); // Redirect to login page
       return;
        
      } try {
    // Decode JWT payload
    const payload = JSON.parse(atob(token.split('.')[1]));

    // Store email and role
    localStorage.setItem("sub", payload.sub);
    localStorage.setItem("role", payload.role);
      
       
         if (payload.role === "STUDENT") 
           { loadPage("student-dashboard.html");
            
              protectNavLinks(); // ✅ Protect nav links based on login status
        setupRoleBasedUI(); // ✅ Call this to set up UI based on role
        setTimeout(() => {
  setActiveNavLink("student-dashboard.html");
}, 0);
           }
      else if (payload.role === "ADMIN"){
         loadPage("dashboard.html");
          
         protectNavLinks(); // ✅ Protect nav links based on login status
        setupRoleBasedUI(); // ✅ Call this to set up UI based on role
setTimeout(() => {
  setActiveNavLink("dashboard.html");
}, 0);      }else{
      
      loadPage("login.html"); // fallback
    }

  } catch (e) {
    console.error("Invalid token format:", e);
    localStorage.clear(); // remove token, role, sub
    loadPage("login.html");
  }
}
    

document.addEventListener("DOMContentLoaded", function () {
  fetch("navbar.html")
    .then((res) => res.text())
    .then((data) => {
      document.getElementById("navbar-container").innerHTML = data;
       

      //  Attach nav link handlers
      document.getElementById("nav-dashboard").addEventListener("click", () => {
        const role = localStorage.getItem("role");
        if (role === "STUDENT") {
          loadPage("student-dashboard.html");
          setActiveNavLink("student-dashboard.html");    

        } else if(role === "ADMIN") {
          loadPage("dashboard.html");
          setActiveNavLink("dashboard.html");    

        }
      });
       

      

    
     
      
      document
        .getElementById("nav-students")
        .addEventListener("click", () => loadPage("student.html"));
      document
        .getElementById("nav-books")
        .addEventListener("click", () => loadPage("book.html"));
      document
        .getElementById("nav-transactions")
        .addEventListener("click", () => loadPage("transaction.html"));
      document
        .getElementById("nav-authors")
        .addEventListener("click", () => loadPage("author.html"));

        document
        .getElementById("nav-upPass")
        .addEventListener("click", () => loadPage("updatePassword.html"));

      document.getElementById("logout-btn").addEventListener("click", logout);


       applyInitialRouting(); // ✅ Load correct page
    })
    .catch((err) => {
      console.error("❌ Navbar load failed", err);
      applyInitialRouting(); // Ensure fallback even if navbar fails
    });    
    }
  );

