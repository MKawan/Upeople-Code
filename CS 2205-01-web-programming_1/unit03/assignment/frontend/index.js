// Function to display preview using GET parameters
function showPreview() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('name')) {
        document.getElementById('prev-name').textContent = urlParams.get('name') || '—';
        document.getElementById('prev-email').textContent = urlParams.get('email') || '—';
        document.getElementById('prev-phone').textContent = urlParams.get('phone') || '—';
        document.getElementById('prev-contact').textContent = urlParams.get('contact') || '—';
        document.getElementById('prev-type').textContent = urlParams.get('inquiry') || '—';
        document.getElementById('prev-message').textContent = urlParams.get('message') || '—';
        document.getElementById('preview').style.display = 'block';
    }
}

// Preview button: update URL and preview without reload
function previewData() {
    if (!validateForm()) return;

    const form = document.getElementById('contactForm');
    const params = new URLSearchParams(new FormData(form));

    window.history.pushState({}, '', '?' + params.toString());
    showPreview();
}

// Client-side validation
function validateForm() {
    let isValid = true;
    const errors = {
        name: document.getElementById('nameError'),
        email: document.getElementById('emailError'),
        phone: document.getElementById('phoneError'),
        contact: document.getElementById('contactError'),
        inquiry: document.getElementById('inquiryError'),
        message: document.getElementById('messageError')
    };

    Object.values(errors).forEach(el => el.textContent = '');

    if (!document.getElementById('name').value.trim()) {
        errors.name.textContent = 'Name is required.';
        isValid = false;
    }

    const email = document.getElementById('email').value;
    if (!email) {
        errors.email.textContent = 'Email is required.';
        isValid = false;
    } else if (!/\S+@\S+\.\S+/.test(email)) {
        errors.email.textContent = 'Please enter a valid email address.';
        isValid = false;
    }

    const phone = document.getElementById('phone').value;
    if (phone && !/^\d{10}$/.test(phone)) {
        errors.phone.textContent = 'Phone must be exactly 10 digits (no dashes/spaces).';
        isValid = false;
    }

    if (!document.querySelector('input[name="contact"]:checked')) {
        errors.contact.textContent = 'Please select a preferred contact method.';
        isValid = false;
    }

    if (!document.getElementById('inquiry').value) {
        errors.inquiry.textContent = 'Please select an inquiry type.';
        isValid = false;
    }

    if (!document.getElementById('message').value.trim()) {
        errors.message.textContent = 'Message is required.';
        isValid = false;
    }

    return isValid;
}

// Submit handler – send to server via fetch with hardcoded URL
document.getElementById('contactForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    if (!validateForm()) {
        alert('Please correct the errors before submitting.');
        return;
    }

    const SERVER_URL = 'http://localhost:3000/contact';  // ← Hardcoded server URL here

    const form = document.getElementById('contactForm');
    const formData = new FormData(form);

    try {
        const response = await fetch(SERVER_URL, {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            alert('Thank you! Your message has been sent successfully.');
            form.reset();
            document.getElementById('preview').style.display = 'none';
            window.history.pushState({}, '', window.location.pathname);
        } else {
            alert('Error: ' + (result.error || 'Something went wrong.'));
        }
    } catch (error) {
        console.error('Submission error:', error);
        alert('Failed to connect to server. Please try again later.');
    }
});

// Show preview on page load if GET params exist
window.onload = showPreview;