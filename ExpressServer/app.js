const express = require('express');
const multer = require('multer');
const axios = require('axios');
const jwt = require('jsonwebtoken');
const fs = require('fs');
const path = require('path');
const app = express();
const PORT = 3000;

// Create storage with a dynamic path
function createStorage(path) {
    return multer.diskStorage({
        destination: function (req, file, cb) {
            cb(null, path);
        },
        filename: function (req, file, cb) {
            cb(null, file.originalname); // or use Date.now() + '-' + file.originalname for unique filenames
        }
    });
}

// Configure upload for each path
const uploadImages = multer({
    storage: createStorage('Files/Images')
});
const uploadProducts = multer({
    storage: createStorage('Files/Images/Products')
});

// Middleware for serving files from both directories
app.use('/images/products', express.static('Files/Images/Products'));
app.use('/images', express.static('Files/Images'));

//Security configuration
const keycloakUrl = process.env.KEYCLOAK_URL || 'http://localhost:8180'; // Use environment variable or default to http://localhost:8180
const jwksUrl = keycloakUrl + '/realms/master/protocol/openid-connect/certs';

async function isAuthorized(token) {
    if (!token) {
        return false;
    }

    const headers = {
        Authorization: `Bearer ${token}`,
    };

    try {
        const response = await axios.get(jwksUrl, {
            headers
        });

        // If the request to the userinfo endpoint is successful (status 200), return true
        return response.status === 200;
    } catch (error) {
        // If there's an error or the response status is not 200, return false
        console.error('Userinfo request failed:', error);
        return false;
    }
}

// GET requests to fetch images from directories
app.get('/images/products/:name', (req, res) => {
    res.sendFile(path.join(__dirname, 'Files/Images/Products', req.params.name));
});

app.get('/images/:name', (req, res) => {
    res.sendFile(path.join(__dirname, 'Files/Images', req.params.name));
});

// POST requests for uploading images to directories
app.post('/images/products', uploadProducts.single('image'), async(req, res) => {
    const token = req.headers.authorization?.split(' ')[1];

    if (!token) {
        return res.status(401).json({
            error: 'Unauthorized - Access Token missing'
        });
    }

    try {
        const isAuthorizedResult = await isAuthorized(token);

        if (isAuthorizedResult) {
            // The user is authorized; proceed with the file upload or other operations.
            res.json({
                file: req.file
            });
        } else {
            // The user is not authorized.
            res.status(401).json({
                error: 'Unauthorized - Invalid Access Token'
            });
        }
    } catch (error) {
        // Handle any errors that occur during authorization check.
        console.error('Authorization check failed:', error);
        res.status(500).json({
            error: 'Internal Server Error'
        });
    }
});

app.post('/images', uploadImages.single('image'), async(req, res) => {

    const token = req.headers.authorization?.split(' ')[1];

    if (!token) {
        return res.status(401).json({
            error: 'Unauthorized - Access Token missing'
        });
    }

    try {
        const isAuthorizedResult = await isAuthorized(token);

        if (isAuthorizedResult) {
            // The user is authorized; proceed with the file upload or other operations.
            res.json({
                file: req.file
            });
        } else {
            // The user is not authorized.
            res.status(401).json({
                error: 'Unauthorized - Invalid Access Token'
            });
        }
    } catch (error) {
        // Handle any errors that occur during authorization check.
        console.error('Authorization check failed:', error);
        res.status(500).json({
            error: 'Internal Server Error'
        });
    }

});

// DELETE requests to delete images from directories
app.delete('/images/products/:name', async (req, res) => {

    const token = req.headers.authorization?.split(' ')[1];

    if (!token) {
        return res.status(401).json({
            error: 'Unauthorized - Access Token missing'
        });
    }

    try {
        const isAuthorizedResult = await isAuthorized(token);

        if (isAuthorizedResult) {
            // The user is authorized; proceed with the file upload or other operations.
            const filePath = path.join(__dirname, 'Files/Images/Products', req.params.name);
            fs.unlink(filePath, (err) => {
                if (err) {
                    return res.status(404).json({
                        error: 'File not found'
                    });
                }
                res.json({
                    message: 'File deleted successfully'
                });
            });
        } else {
            // The user is not authorized.
            res.status(401).json({
                error: 'Unauthorized - Invalid Access Token'
            });
        }
    } catch (error) {
        // Handle any errors that occur during authorization check.
        console.error('Authorization check failed:', error);
        res.status(500).json({
            error: 'Internal Server Error'
        });
    }

});

app.delete('/images/:name', async (req, res) => {

    const token = req.headers.authorization?.split(' ')[1];

    if (!token) {
        return res.status(401).json({
            error: 'Unauthorized - Access Token missing'
        });
    }

    try {
        const isAuthorizedResult = await isAuthorized(token);

        if (isAuthorizedResult) {
            // The user is authorized; proceed with the file upload or other operations.
            const filePath = path.join(__dirname, 'Files/Images', req.params.name);
            fs.unlink(filePath, (err) => {
                if (err) {
                    return res.status(404).json({
                        error: 'File not found'
                    });
                }
                res.json({
                    message: 'File deleted successfully'
                });
            });
        } else {
            // The user is not authorized.
            res.status(401).json({
                error: 'Unauthorized - Invalid Access Token'
            });
        }
    } catch (error) {
        // Handle any errors that occur during authorization check.
        console.error('Authorization check failed:', error);
        res.status(500).json({
            error: 'Internal Server Error'
        });
    }

});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
