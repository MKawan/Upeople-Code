document.addEventListener('DOMContentLoaded', ()=>{
  const loginForm = document.getElementById('loginForm');
  const registerForm = document.getElementById('registerForm');
  const showRegister = document.getElementById('showRegister');
  const showLogin = document.getElementById('showLogin');

  showRegister.addEventListener('click', (e)=>{
    e.preventDefault();
    loginForm.classList.remove('active');
    registerForm.classList.add('active');
  });

  showLogin.addEventListener('click', (e)=>{
    e.preventDefault();
    registerForm.classList.remove('active');
    loginForm.classList.add('active');
  });

  // Simulated submissions — integrate with backend here
  loginForm.addEventListener('submit', (e)=>{
    e.preventDefault();
    alert('Simulation: attempting to sign in (frontend only)');
  });

  registerForm.addEventListener('submit', (e)=>{
    e.preventDefault();
    alert('Simulation: sending registration data (frontend only)');
  });
});
