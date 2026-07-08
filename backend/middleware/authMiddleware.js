const jwt = require("jsonwebtoken");

// Protección para rutas que requieran token

function verifyToken(req, res, next) {
  const authHeader = req.headers["authorization"];

  if (!authHeader) {
    return res.status(403).json({ error: "Token requerido" });
  }

  const token = authHeader.split(" ")[1];

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded;
    next();
  } catch (err) {
    return res.status(401).json({ error: "Token inválido" });
  }
}

module.exports = verifyToken;

/*
  Ejemplo de implementación en auth.js

const verifyToken = require("../middleware/authMiddleware");

router.get("/profile", verifyToken, (req, res) => {
  res.json({
    message: "Acceso permitido",
    user: req.user
  });
});

*/