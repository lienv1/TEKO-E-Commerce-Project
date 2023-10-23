const express = require('express');
const multer  = require('multer');
const fs = require('fs');
const path = require('path');
const app = express();
const PORT = 3000;

// Configure storage
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    // check the url of the incoming request
    const uploadPath = req.baseUrl.includes('/products') ? 'Files/Images/Products' : 'Files/Images';
    cb(null, uploadPath);
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname);
  }
});

const upload = multer({ storage: storage });

// Middleware for serving files from both directories
app.use('/images/products', express.static('Files/Images/Products'));
app.use('/images', express.static('Files/Images'));

// GET requests to fetch images from directories
app.get('/images/products/:name', (req, res) => {
  res.sendFile(path.join(__dirname, 'Files/Images/Products', req.params.name));
});

app.get('/images/:name', (req, res) => {
  res.sendFile(path.join(__dirname, 'Files/Images', req.params.name));
});

// POST requests for uploading images to directories
app.post('/images/products', upload.single('image'), (req, res) => {
  res.json({ file: req.file });
});

app.post('/images', upload.single('image'), (req, res) => {
  res.json({ file: req.file });
});

// DELETE requests to delete images from directories
app.delete('/images/products/:name', (req, res) => {
  const filePath = path.join(__dirname, 'Files/Images/Products', req.params.name);
  fs.unlink(filePath, (err) => {
    if (err) {
      return res.status(404).json({ error: 'File not found' });
    }
    res.json({ message: 'File deleted successfully' });
  });
});

app.delete('/images/:name', (req, res) => {
  const filePath = path.join(__dirname, 'Files/Images', req.params.name);
  fs.unlink(filePath, (err) => {
    if (err) {
      return res.status(404).json({ error: 'File not found' });
    }
    res.json({ message: 'File deleted successfully' });
  });
});

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
