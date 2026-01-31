const API_BASE = "https://openlibrary.org";
const booksEl = document.getElementById("books");
const searchInput = document.getElementById("searchInput");
const searchBtn = document.getElementById("searchBtn");
const cartBtn = document.getElementById("cartBtn");
const cartCount = document.getElementById("cartCount");
const cartModal = document.getElementById("cartModal");
const closeCart = document.getElementById("closeCart");
const cartItemsEl = document.getElementById("cartItems");
const cartTotalEl = document.getElementById("cartTotal");
const checkoutBtn = document.getElementById("checkoutBtn");

let cart = JSON.parse(localStorage.getItem("bookstore_cart") || "[]");

function saveCart() {
  localStorage.setItem("bookstore_cart", JSON.stringify(cart));
  updateCartUI();
}

function updateCartUI() {
  cartCount.textContent = cart.reduce((s, i) => s + i.qty, 0);
  const total = cart.reduce((s, i) => s + i.qty * i.price, 0);
  cartTotalEl.textContent = `Total: $${total.toFixed(2)}`;
  renderCartItems();
}

function renderCartItems() {
  cartItemsEl.innerHTML = "";
  if (cart.length === 0) {
    cartItemsEl.innerHTML = "<p>Your cart is empty.</p>";
    return;
  }
  cart.forEach((item) => {
    const row = document.createElement("div");
    row.className = "cart-row";
    const img = document.createElement("img");
    img.src = item.cover || "https://via.placeholder.com/128x192?text=No+Cover";
    const meta = document.createElement("div");
    meta.style.flex = "1";
    meta.innerHTML = `<div><strong>${item.title}</strong></div><div style="color:#666">${item.author || "Unknown"}</div><div style="margin-top:6px">$${item.price.toFixed(2)} × ${item.qty}</div>`;
    const remove = document.createElement("button");
    remove.textContent = "Remove";
    remove.onclick = () => {
      cart = cart.filter((c) => c.key !== item.key);
      saveCart();
    };
    row.appendChild(img);
    row.appendChild(meta);
    row.appendChild(remove);
    cartItemsEl.appendChild(row);
  });
}

async function fetchBooks(q = "fiction") {
  try {
    const res = await fetch(
      `${API_BASE}/search.json?q=${encodeURIComponent(q)}&limit=24`,
    );
    const data = await res.json();
    const books = data.docs.map((d) => ({
      key: d.key || d.cover_edition_key || Math.random().toString(36).slice(2),
      title: d.title || "Untitled",
      author: (d.author_name && d.author_name[0]) || "Unknown",
      cover: d.cover_i
        ? `https://covers.openlibrary.org/b/id/${d.cover_i}-L.jpg`
        : null,
      year: d.first_publish_year || null,
      price: Math.random() * 15 + 5, // random price 5-20
    }));
    return books;
  } catch (err) {
    console.error("fetch err", err);
    return [];
  }
}

function renderBooks(books) {
  booksEl.innerHTML = "";
  const tpl = document.getElementById("book-template");
  books.forEach((book) => {
    const node = tpl.content.cloneNode(true);
    const img = node.querySelector(".cover");
    img.src = book.cover || "https://via.placeholder.com/200x300?text=No+Cover";
    img.alt = book.title;
    node.querySelector(".title").textContent = book.title;
    node.querySelector(".author").textContent = book.author;
    node.querySelector(".add").addEventListener("click", () => {
      addToCart(book);
    });
    node.querySelector(".details").addEventListener("click", () => {
      alert(
        `${book.title}\n\nAuthor: ${book.author}\nYear: ${book.year || "N/A"}\nPrice: $${book.price.toFixed(2)}`,
      );
    });
    booksEl.appendChild(node);
  });
}

function addToCart(book) {
  const existing = cart.find((i) => i.key === book.key);
  if (existing) {
    existing.qty += 1;
  } else {
    cart.push({ ...book, qty: 1 });
  }
  saveCart();
}

searchBtn.addEventListener("click", async () => {
  const q = searchInput.value.trim() || "fiction";
  searchBtn.disabled = true;
  searchBtn.textContent = "Searching...";
  const books = await fetchBooks(q);
  renderBooks(books);
  searchBtn.disabled = false;
  searchBtn.textContent = "Search";
});

cartBtn.addEventListener("click", () => {
  cartModal.classList.remove("hidden");
});
closeCart.addEventListener("click", () => {
  cartModal.classList.add("hidden");
});
checkoutBtn.addEventListener("click", () => {
  if (cart.length === 0) {
    alert("Cart is empty");
  } else {
    alert("Thanks for shopping — this is a demo.");
    cart = [];
    saveCart();
    cartModal.classList.add("hidden");
  }
});

// initial load
(async () => {
  updateCartUI();
  const books = await fetchBooks("subject:fiction");
  if (books.length === 0) {
    // fallback sample
    const sample = [
      {
        key: "s1",
        title: "The Hidden Orchard",
        author: "A. Writer",
        cover: null,
        price: 9.99,
      },
      {
        key: "s2",
        title: "Midnight Map",
        author: "B. Author",
        cover: null,
        price: 12.5,
      },
    ];
    renderBooks(sample);
  } else renderBooks(books);
})();
