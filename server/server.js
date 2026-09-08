const express = require('express');
const cors = require('cors');
const Database = require('better-sqlite3');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;
const db = new Database(path.join(__dirname, 'family-calendar.db'));

db.pragma('journal_mode = WAL');

db.exec(`
  CREATE TABLE IF NOT EXISTS events (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    date TEXT NOT NULL,
    time TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
  );
`);

app.use(cors());
app.use(express.json());

app.get('/api/health', (req, res) => {
  res.json({ ok: true, service: 'family-calendar-server' });
});

app.get('/api/events', (req, res) => {
  const events = db.prepare('SELECT * FROM events ORDER BY date, time, id').all();
  res.json(events);
});

app.post('/api/events', (req, res) => {
  const { title, date, time } = req.body || {};

  if (!title || !date || !time) {
    return res.status(400).json({ error: 'title, date and time are required' });
  }

  const result = db
    .prepare('INSERT INTO events (title, date, time) VALUES (?, ?, ?)')
    .run(String(title), String(date), String(time));

  const event = db.prepare('SELECT * FROM events WHERE id = ?').get(result.lastInsertRowid);
  res.status(201).json(event);
});

app.delete('/api/events/:id', (req, res) => {
  const result = db.prepare('DELETE FROM events WHERE id = ?').run(req.params.id);

  if (result.changes === 0) {
    return res.status(404).json({ error: 'event not found' });
  }

  res.json({ ok: true });
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Family Calendar server running on port ${PORT}`);
});
