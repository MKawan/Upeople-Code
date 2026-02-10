// Theme toggle functionality
function toggleTheme() {
  const html = document.documentElement;
  const body = document.body;
  const themeToggle = document.getElementById('themeToggle');
  const themeIcon = themeToggle.querySelector('.theme-icon');
  
  // Toggle dark mode class
  body.classList.toggle('dark-mode');
  
  // Update icon
  if (body.classList.contains('dark-mode')) {
    themeIcon.textContent = '☀️';
    localStorage.setItem('theme', 'dark');
  } else {
    themeIcon.textContent = '🌙';
    localStorage.setItem('theme', 'light');
  }
}

// Initialize theme on page load
window.addEventListener('load', function() {
  const savedTheme = localStorage.getItem('theme');
  const body = document.body;
  const themeToggle = document.getElementById('themeToggle');
  const themeIcon = themeToggle.querySelector('.theme-icon');
  
  if (savedTheme === 'dark') {
    body.classList.add('dark-mode');
    themeIcon.textContent = '☀️';
  } else {
    themeIcon.textContent = '🌙';
  }
});

// Data structure with all continents, countries, and cities
const continentData = {
  europe: {
    name: 'Europe',
    countries: {
      france: {
        name: 'France',
        cities: ['Paris', 'Nice', 'Marseille']
      },
      italy: {
        name: 'Italy',
        cities: ['Rome', 'Florence', 'Venice']
      },
      greece: {
        name: 'Greece',
        cities: ['Athens', 'Santorini', 'Thessaloniki']
      }
    }
  },
  asia: {
    name: 'Asia',
    countries: {
      japan: {
        name: 'Japan',
        cities: ['Tokyo', 'Kyoto', 'Osaka']
      },
      china: {
        name: 'China',
        cities: ['Beijing', 'Shanghai', 'Xi\'an']
      },
      india: {
        name: 'India',
        cities: ['New Delhi', 'Mumbai', 'Jaipur']
      }
    }
  },
  america: {
    name: 'America',
    countries: {
      usa: {
        name: 'USA',
        cities: ['New York City', 'Los Angeles', 'Chicago']
      },
      brazil: {
        name: 'Brazil',
        cities: ['Rio de Janeiro', 'São Paulo', 'Salvador']
      },
      canada: {
        name: 'Canada',
        cities: ['Toronto', 'Vancouver', 'Montreal']
      }
    }
  },
  africa: {
    name: 'Africa',
    countries: {
      'south-africa': {
        name: 'South Africa',
        cities: ['Cape Town', 'Johannesburg', 'Durban']
      },
      egypt: {
        name: 'Egypt',
        cities: ['Cairo', 'Luxor', 'Sharm El Sheikh']
      },
      morocco: {
        name: 'Morocco',
        cities: ['Marrakech', 'Casablanca', 'Fez']
      }
    }
  }
};

// Global variables for image viewer
let currentImageIndex = 1;
let currentCity = '';
let currentCountry = '';
let currentContinent = '';

// Function to generate continent sections dynamically
function generateContinentSection(continentKey) {
  const continent = continentData[continentKey];
  
  let html = `
    <div id="${continentKey}" class="destination-section">
  `;

  // Add countries and cities
  for (const [countryKey, country] of Object.entries(continent.countries)) {
    const countryId = `cities-${countryKey}`;
    html += `
      <h3 data-continent="${continentKey}" data-country="${countryKey}">${country.name}</h3>
      <ul id="${countryId}">
    `;

    country.cities.forEach(city => {
      html += `<li>${city}</li>`;
    });

    html += `</ul>`;
  }

  // Add postal container and back link
  html += `
  </div>
      <div id="postal-container-${continentKey}" class="postal-container"></div>
      <a href="#" class="back-link" onclick="closeContinent(); return false;">↑ Back to Top</a>
  `;

  const titleElement = document.getElementById('title-external');
  titleElement.innerHTML = `<h2 class="continent-title">${continent.name}</h2>`;

  return html;
}

// Function to load and display a continent
function loadContinent(continentKey, event) {
  event.preventDefault();
  
  const container = document.getElementById('contentContainer');
  
  // Clear previous content
  container.innerHTML = '';
  
  // Generate and insert the continent section
  const html = generateContinentSection(continentKey);
  container.innerHTML = html;
  
  // Scroll to the continent section
  container.scrollIntoView({ behavior: 'smooth', block: 'start' });
  
  // Attach event listeners to country titles
  attachCountryClickListeners();
}

// Function to close continent and return to home
function closeContinent() {
  const container = document.getElementById('contentContainer');
  container.innerHTML = '';
  
  // Scroll back to top
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

// Function to attach click listeners to country titles
function attachCountryClickListeners() {
  document.querySelectorAll('h3[data-continent]').forEach(countryTitle => {
    countryTitle.addEventListener('click', function () {
      const continent = this.getAttribute('data-continent');
      const countryKey = this.getAttribute('data-country');
      
      const containerId = `postal-container-${continent}`;
      const container = document.getElementById(containerId);
      
      if (!container) return;
      
      // Clear previous content
      container.innerHTML = '';

      // Get cities for this country
      const countryId = `cities-${countryKey}`;
      const cityList = document.getElementById(countryId);
      
      if (!cityList) return;

      const cities = Array.from(cityList.querySelectorAll('li'))
                         .map(li => li.textContent.trim());

      // Create table
      let tableHTML = `
        <table class="postal-table">
          <thead>
            <tr>
              <th>City</th>
              <th>Postal Card</th>
            </tr>
          </thead>
          <tbody>
      `;

      // Add rows with city name and image
      cities.forEach(city => {
        const imageName = city.toLowerCase().replace(/\s+/g, '-');
        const imagePath = `images/postal-cards/${continent}/${countryKey}/${imageName}/image1.png`;

        tableHTML += `
          <tr>
            <td>${city}</td>
            <td>
              <img src="${imagePath}" 
                   alt="Postal card of ${city}" 
                   class="postal-card-img"
                   data-city="${city}"
                   data-country="${countryKey}"
                   data-continent="${continent}"
                   onclick="openImageViewer(this)"
                   onerror="this.src='https://via.placeholder.com/280x180?text=No+Image';">
            </td>
          </tr>
        `;
      });

      tableHTML += `
          </tbody>
        </table>
      `;

      // Insert the table into the page
      container.innerHTML = tableHTML;
    });
  });
}

// Function to open the image viewer
function openImageViewer(imgElement) {
    currentCity = imgElement.getAttribute('data-city');
    currentCountry = imgElement.getAttribute('data-country');
    currentContinent = imgElement.getAttribute('data-continent');
    currentImageIndex = 1;

    // Update viewer UI
    const viewerTitle = document.getElementById('viewerTitle');
    viewerTitle.textContent = `${currentCity} - Image Gallery`;

    // Display the image viewer modal
    const imageViewer = document.getElementById('imageViewer');
    imageViewer.classList.add('active');

    // Load the first image
    loadImage(currentImageIndex);
}

// Function to load and display a specific image
function loadImage(imageNumber) {
    const imagePath = `images/postal-cards/${currentContinent}/${currentCountry}/${currentCity.toLowerCase().replace(/\s+/g, '-')}/image${imageNumber}.png`;
    const viewerImage = document.getElementById('viewerImage');
    const imageCounter = document.getElementById('imageCounter');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    viewerImage.src = imagePath;
    imageCounter.textContent = imageNumber;

    // Enable/Disable navigation buttons
    prevBtn.disabled = imageNumber === 1;
    nextBtn.disabled = imageNumber === 10;

    // Handle image load error with placeholder
    viewerImage.onerror = function () {
        this.src = `https://via.placeholder.com/900x600?text=Image+${imageNumber}+Not+Found`;
    };
}

// Function to navigate to previous image
function previousImage() {
    if (currentImageIndex > 1) {
        currentImageIndex--;
        loadImage(currentImageIndex);
    }
}

// Function to navigate to next image
function nextImage() {
    if (currentImageIndex < 10) {
        currentImageIndex++;
        loadImage(currentImageIndex);
    }
}

// Function to close the image viewer
function closeImageViewer() {
    const imageViewer = document.getElementById('imageViewer');
    imageViewer.classList.remove('active');
    currentImageIndex = 1;
}

// Close viewer when clicking outside the image container
const imageViewer = document.getElementById('imageViewer');

if (imageViewer) {
  imageViewer.addEventListener('click', function (event) {
    if (event.target === this) {
      closeImageViewer();
    }
  });
}

// Keyboard navigation support
document.addEventListener('keydown', function (event) {
  const imageViewer = document.getElementById('imageViewer');
  if (imageViewer && imageViewer.classList.contains('active')) {
    if (event.key === 'ArrowLeft') {
      previousImage();
    } else if (event.key === 'ArrowRight') {
      nextImage();
    } else if (event.key === 'Escape') {
      closeImageViewer();
    }
  }
});