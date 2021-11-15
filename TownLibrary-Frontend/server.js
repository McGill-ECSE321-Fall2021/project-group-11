// Based on https://medium.com/binarcode/deploying-vue-apps-to-heroku-the-right-way-26b11c1ae5cd

const express = require('express')
const serveStatic = require('serve-static')
const path = require('path')

app = express()
app.use(serveStatic(path.join(__dirname, 'dist')))
const port = process.env.PORT || 8080
app.listen(port)

