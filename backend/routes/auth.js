const express = require("express");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");

const router = express.Router();

// Simulación de base de datos
const users = [
  {
    id: 1,
    name: "Daniel",
    email: "daniel@uca.edu.sv",
    password: bcrypt.hashSync("123456", 8)
  }
];

// ------------------------------------------- //
// REGISTER
// POST: auth/register
// ------------------------------------------- //

router.post("/register", (req, res) => {
  const { name, email, password } = req.body;

  /*
  if (!name || !email || !password) {
    return res.status(400).json({ error: "Todos los campos son requeridos" });
  }
  */

  // Verificar si ya existe
  const existingUser = users.find(u => u.email === email);
  if (existingUser) {
    return res.status(400).json({ error: "El usuario ya existe" });
  }

  // Encriptar contraseña
  const hashedPassword = bcrypt.hashSync(password, 8);

  // Crear usuario
  const newUser = {
    id: users.length + 1,
    name,
    email,
    password: hashedPassword
  };

  users.push(newUser);

  // Crear token automáticamente
  const token = jwt.sign(
    { id: newUser.id, email: newUser.email },
    process.env.JWT_SECRET
    // { expiresIn: "24h" }
  );

  res.status(201).json({
    token,
    user: {id: newUser.id, name: newUser.name, email: newUser.email}
  });
});

// ------------------------------------------- //
// LOGIN
// POST: auth/login
// ------------------------------------------- //

router.post("/login", (req, res) => {
  const { email, password } = req.body;

  // Verificación de username
  const user = users.find(u => u.email === email);
  if (!user) {
    return res.status(401).json({ error: "Credenciales inválidas" });
  }

  // Verificación de email
  const passwordCorrect = bcrypt.compareSync(password, user.password);
  if (!passwordCorrect) {
    return res.status(401).json({ error: "Credenciales inválidas" });
  }

  // Asignación de token
  const token = jwt.sign(
    { id: user.id, email: user.email },
    process.env.JWT_SECRET
    // { expiresIn: "24h" }
  );

  // Respuesta
  res.json({
    token,
    user: {id: user.id, name: user.name, email: user.email}
  });
});

module.exports = router;