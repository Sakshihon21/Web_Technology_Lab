const express = require("express");
const mysql = require("mysql2");
const cors = require("cors");

const app = express();

app.use(cors());
app.use(express.json());

/* DATABASE CONNECTION */

const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "abhy17",
  database: "college_events"
});

db.connect((err) => {
  if (err) {
    console.log("Database connection error:", err);
  } else {
    console.log("Connected to MySQL Database");
  }
});

/* REGISTER API */

app.post("/register", (req, res) => {

  console.log("Data received:", req.body);

  const { name, email, event } = req.body;

  const sql =
    "INSERT INTO registrations (name, email, event_name) VALUES (?, ?, ?)";

  db.query(sql, [name, email, event], (err, result) => {

    if (err) {
      console.log("Insert error:", err);
      res.status(500).send("Database insert error");
    } else {
      console.log("Data inserted successfully");
      res.send("Registration successful");
    }

  });

});

app.listen(5000, () => {
  console.log("Server running on port 5000");
});