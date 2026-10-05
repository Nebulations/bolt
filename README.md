# Bolt - Java-based interpreted programming language
Bolt is a simple interpreted language that uses an english-like syntax.

# Docs

### Comments
Comments can be written using the `#` symbol. You can only put comments in a new line, not after code.
```
# This is a comment.
<code> # This comment is not valid.
```

### Variables
Variables can be defined using the `define` keyword.
```
define strings "Hello World"
define numbers 1234.1234
define bools true
```

### Basic standard functions
Use the `print` keyword to output text to the standard output.
```
print "Hello World!"

# Output
Hello, World!
```

### Functions
Functions can be declared using the `function` keyword. Function have a name, and a certain amount of arguments.
Functions can also return values using the `return` keyword. By default, all functions return `null`.
```
function greet name
    # You can surround variable names with curly brackets to make them appear in the string.
    print "Hello, {name}!"

call greet "Nathan"

# Output
Hello, Nathan!
```

You can capture the return value of a function using the keywords `define` and `as`
```
function greet name
    return "Hello, {name}!"

define greeting as greet "Nathan"

print "{greeting}"

# Output
Hello, Nathan!
```