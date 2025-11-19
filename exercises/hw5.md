# Homework 5
*Audrey Whitney-Miller*

## 1. Explain the meaning of each of the following C declarations.
**a. double *a[n];**
a is an array of n pointers to double.
**b. double (*b) [n];**
b is a pointer to an array of n doubles
**c. double (*c[n])();**
c is an array of n pointers to functions that return double.
**d. double (*d())[n];**
d is a function that returns a pointer to an array of n doubles.

## 2. In C, when exactly do arrays decay to pointers.
Arrays decay to pointers anytime they appear in an expression, except when used with sizeof, _Alignof, or as an initializer for another array.

## 3. Give a short description, under 10 words each, of the following, as they are understood in the context of the C language: (a) memory leak, (b) dangling pointer, (c) double free, (d) buffer overflow, (e) stack overflow, (f) wild pointer.
**A**: Allocated memory never freed.
**B**: Pointer referenceing freed memory.
**c**: Writing past allocated bounds.
**d**: Writing past allocated bounds.
**e**: Stack exceeds its size limit.
**f**: Uninitialized or invalid pointer value.

## 4. Explain why C++ move constructors and move assignment operators only make sense on r-values and not l-values. You can use a rough code fragment in your explanation.
Move constructors and move assignment operators only apply to r-values because moving transfers a resource out of the source object and leaves it in a valid-but-empty state. That’s safe for r-values (like temporaries) because they’re about to be destroyed, but it isn’t safe for l-values, which are named objects that must remain usable after the expression. If moves applied to l-values automatically, then ordinary assignments like x = y; could silently empty y, breaking program logic. Restricting moves to r-values ensures the compiler only performs destructive transfers when the source object won’t be needed anymore.

```cpp
std::string a = "hello";
std::string b = std::move(a);   // OK: a becomes empty

std::string c = "test";
std::string d = std::move(c);   // c is now empty, only allowed because we explicitly forced a move

```
## 5. Why does C++ even have moves, anyway?
C++ has moves to avoid unnecessary copying and improve performance with temporary objects. When a function returns a large object, copying it is slow, but moving lets the program reuse the internal storage instead of duplicating it. This keeps value semantics while reducing overhead.

## 6. What is the rule-of-5 in C++?
If any of the below functions is defined for a class, then it is better to define all of them.

## 7. What are the three ownership rules of Rust?
**1**: Each value has one owner at a time.
**2**: A value is dropped when its owner goes out of scope.
**3**: Ownership can be moved to a new owner.

## 8. What are the three borrowing rules of Rust?
**1**: Only one mutable reference is allowed at a time.
**2**: Any number of immutable references is allowed, as long as there is no mutable one.
**3**: References must not outlive the data they reference.
