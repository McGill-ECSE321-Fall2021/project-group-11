export default function decodeError (api_error) {
  if (!api_error)
    return []

  if (!api_error.response || !api_error.response.data)
    // happens when there is an network error
    return ['Network error, please reload and try again later']

  let raw_msg = api_error.response.data
  if (typeof raw_msg !== 'string' && !(raw_msg instanceof String))
    // happens when there is a backend error that we did not / cannot catch.
    // typically happens when supplying wrong parameters
    //
    // (not much we can do other than say it's an unknown error)
    return ['Unknown error, please reload and try again later']

  // Extract it into a list of error messages. Note that error messages can be
  // are ',', '.', and '!' separated.
  return raw_msg.split(/,|!|\./)
      .map(msg => msg.trim())
      .filter(msg => 0 !== msg.length)
      .map(msg => {
        switch (msg) {
        default:
          return msg

        case 'NULL-LIBRARY':
          return 'Invalid library, has setup been performed yet?'
        case 'DUP-LIBRARY':
          return 'Duplicate library, try reloading the page'
        case 'DUP-HEAD-LIBRARIAN':
          return 'Duplicate head-librarian, try reloading the page'
        case 'BAD-ACCESS':
          return 'Illegal user access'

        case 'BAD-AUTH-ONLINE-MEMBER':
          return 'Incorrect username or password'
        case 'BAD-AUTH-LIBRARIAN':
          return 'Incorrect id or password'

        case 'EMPTY-NAME':
          return 'Name cannot be empty'
        case 'EMPTY-ADDRESS':
          return 'Address cannot be empty'
        case 'EMPTY-PASSWORD':
          return 'Password cannot be empty'
        case 'DUP-USERNAME':
          return 'Username is already taken'
        case 'DUP-EMAIL':
          return 'Email is already used'
        case 'EMPTY-EMAIL':
          return 'Email cannot be empty'
        case 'BADFMT-EMAIL':
          return 'Invalid Email'
        case 'UNDERSIZED-PASSWORD':
        case 'OVERSIZED-PASSWORD':
          return 'Password must be 4 to 32 characters long'
        case 'BADCHAR-PASSWORD':
          return 'Password can only contain alphanumeric characters'
        }
      })
}
