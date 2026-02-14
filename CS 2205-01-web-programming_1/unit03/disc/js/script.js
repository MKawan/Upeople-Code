/*
  script.js — UI behavior for the mini login/register app
  - toggles which form is visible (Sign In / Create Account)
  - handles simulated form submissions (replace alerts with real requests)
*/
document.addEventListener('DOMContentLoaded', ()=>{
  // element references
  const loginForm = document.getElementById('loginForm');
  const registerForm = document.getElementById('registerForm');
  const showRegister = document.getElementById('showRegister');
  const showLogin = document.getElementById('showLogin');

  // When the user clicks "Create account" — show the register form
  showRegister.addEventListener('click', (e)=>{
    e.preventDefault();
    loginForm.classList.remove('active');
    registerForm.classList.add('active');
  });

  // When the user clicks "Sign in" in the register form — go back to login
  showLogin.addEventListener('click', (e)=>{
    e.preventDefault();
    registerForm.classList.remove('active');
    loginForm.classList.add('active');
  });

  // Simulated submissions — replace with fetch()/XHR to your backend
  loginForm.addEventListener('submit', (e)=>{
    e.preventDefault();
    // TODO: validate inputs and submit to backend
    alert('Simulation: attempting to sign in (frontend only)');
  });

  registerForm.addEventListener('submit', (e)=>{
    e.preventDefault();
    // TODO: validate inputs and submit to backend
    alert('Simulation: sending registration data (frontend only)');
  });
});
