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
  let errs = []
  for (let msg of raw_msg.split(/,|!|\./)) {
    msg = msg.trim()
    if (0 === msg.length)
      continue

    switch (msg) {
    // list of more special/severe errors:
    // if we find these, we want to report that one alone and ignore all other
    // errors
    case 'NULL-LIBRARY':
      return ['Invalid library, has setup been performed yet?']
    case 'DUP-LIBRARY':
      return ['Duplicate library, try reloading the page']
    case 'DUP-HEAD-LIBRARIAN':
      return ['Duplicate head-librarian, try reloading the page']
    case 'BAD-ACCESS':
      return ['Illegal user access, try reloading the page']

    // other errors just add them to the list of errors and report them at the
    // same time
    default:
      errs.push(msg)
      break
    case 'BAD-AUTH-ONLINE-MEMBER':
      errs.push('Incorrect username or password')
      break
    case 'BAD-AUTH-LIBRARIAN':
      errs.push('Incorrect id or password')
      break
    case 'EMPTY-NAME':
      errs.push('Name cannot be empty')
      break
    case 'EMPTY-ADDRESS':
      errs.push('Address cannot be empty')
      break
    case 'EMPTY-PASSWORD':
      errs.push('Password cannot be empty')
      break
    case 'DUP-USERNAME':
      errs.push('Username is already taken')
      break
    case 'DUP-EMAIL':
      errs.push('Email is already used')
      break
    case 'EMPTY-EMAIL':
      errs.push('Email cannot be empty')
      break
    case 'BADFMT-EMAIL':
      errs.push('Invalid Email')
      break
    case 'UNDERSIZED-PASSWORD':
    case 'OVERSIZED-PASSWORD':
      errs.push('Password must be 4 to 32 characters long')
      break
    case 'BADCHAR-PASSWORD':
      errs.push('Password can only contain alphanumeric characters')
      break
    }
  }
  return errs
}
