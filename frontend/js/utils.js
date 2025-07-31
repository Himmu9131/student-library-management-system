import { loadPage } from "./router.js";


function extractRoleFromToken() {
  const token = localStorage.getItem("token");
  if (!token) return null;

  try {
    const payloadBase64 = token.split(".")[1];
    const payloadJson = atob(payloadBase64);
    const payload = JSON.parse(payloadJson);

    const role = payload.role || payload["ROLE"] || null;
    if (role) {
      localStorage.setItem("role", role); // ✅ Save role
    }

    return role;
  } catch (e) {
    console.error("❌ Failed to decode token:", e);
    return null;
  }
}

async function changePassword() {

const updateForm=document.getElementById("changePasswordForm");
const newPassword=document.getElementById("newPassword").value.trim();
const confirmPassword=document.getElementById("confirmPassword").value.trim();


     if (newPassword !== confirmPassword) {
    alert("New passwords do not match!");
    return;
  }

  try {
    const response =  await fetch("http://localhost:8080/auth/update-password", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`
      },
      body: JSON.stringify({
        email:localStorage.getItem("sub"),
        newPass: newPassword,
        confirmPass: confirmPassword
      })
    });
        
    if (response.ok) {
      alert("Password changed successfully!");
      updateForm.reset();
      const role=localStorage.getItem("role");
      if(role==="STUDENT")
      loadPage("student-dashboard.html");
      else if(role==="ADMIN"){
      loadPage("dashboard.html");
      }

    } else {
        
      alert("Error changing password");
      updateForm.reset();
    }
  } catch (err) {
    console.error("Error:", err);
  }
}



export { extractRoleFromToken,changePassword};