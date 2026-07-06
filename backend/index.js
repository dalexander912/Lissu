require("dotenv").config();
const express = require("express");
const cors = require("cors");

const authRoutes = require("./routes/auth");

const app = express();


app.use(cors({
  origin: "*"
}));
app.use(express.json());

// Rutas
app.use("/auth", authRoutes);

app.listen(process.env.PORT, () => {
  console.log(`API corriendo en http://localhost:${process.env.PORT}`);
});