var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',

  FRONTEND_HOST: '"127.0.0.1"',
  FRONTEND_PORT: '"8087"',
  API_HOST: '"127.0.0.1"',
  API_PORT: '"8080"'
})
