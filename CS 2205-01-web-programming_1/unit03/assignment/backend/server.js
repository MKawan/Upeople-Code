const express = require('express');
const sqlite3 = require('sqlite3').verbose();
const cors = require('cors');
const multer = require('multer');  // ← obrigatório aqui

const app = express();
const port = 3000;

// CORS
app.use(cors({
  origin: '*',
  methods: ['GET', 'POST', 'OPTIONS'],
  allowedHeaders: ['Content-Type']
}));

// Configuração do multer para lidar com multipart/form-data (sem arquivos)
const upload = multer();  // sem storage = só parseia campos de texto

// Conexão com o banco (mantém igual)
const db = new sqlite3.Database('./database.db', (err) => {
  if (err) console.error('Error connecting to SQLite:', err.message);
  else console.log('Connected to SQLite database successfully.');
});

// Cria tabela (mantém igual)
db.serialize(() => {
  db.run(`
    CREATE TABLE IF NOT EXISTS contacts (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      name TEXT NOT NULL,
      email TEXT NOT NULL,
      phone TEXT,
      preferred_contact TEXT,
      inquiry_type TEXT,
      message TEXT NOT NULL,
      created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    )
  `);
});

// ────────────────────────────────────────────────
//                  API ROUTES
// ────────────────────────────────────────────────

// GET /contacts → Retrieve all submitted contact messages
app.get('/contacts', (req, res) => {
    db.all('SELECT * FROM contacts ORDER BY id DESC', [], (err, rows) => {
        if (err) {
            console.error('Error fetching contacts:', err.message);
            return res.status(500).json({ error: 'Failed to fetch contacts' });
        }
        res.json(rows);
    });
});

// Rota POST /contact – agora com multer
app.post('/contact', upload.none(), (req, res) => {
  console.log('POST /contact recebido!');
  console.log('req.body completo:', req.body);  // ← deve mostrar {name: '...', email: '...', ...}

  // Se req.body estiver vazio → problema de parsing
  if (!req.body || Object.keys(req.body).length === 0) {
    return res.status(400).json({
      success: false,
      error: 'No form data received - check Content-Type or multer config'
    });
  }

  const { name, email, phone, contact, inquiry, message } = req.body;

  if (!name || !email || !message) {
    return res.status(400).json({
      success: false,
      error: 'Name, email, and message are required'
    });
  }

  if (phone && !/^\d{10}$/.test(phone)) {
    return res.status(400).json({
      success: false,
      error: 'Phone must be exactly 10 digits'
    });
  }

  const sql = `
    INSERT INTO contacts 
    (name, email, phone, preferred_contact, inquiry_type, message) 
    VALUES (?, ?, ?, ?, ?, ?)
  `;

  const values = [
    name.trim(),
    email.trim(),
    phone ? phone.trim() : null,
    contact || null,
    inquiry || null,
    message.trim()
  ];

  db.run(sql, values, function (err) {
    if (err) {
      console.error('Database error:', err.message);
      return res.status(500).json({ success: false, error: 'Failed to save' });
    }

    res.status(201).json({
      success: true,
      message: 'Message saved!',
      id: this.lastID
    });
  });
});

// Inicia servidor
app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});