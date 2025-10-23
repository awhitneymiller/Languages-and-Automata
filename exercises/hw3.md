# Homework 3
*Audrey Whitney-Miller*

## 1. Show how to constructively define the type of trees of elements of type t.
Tree : Type -> Type
leaf : for all {t}, t -> Tree t
branch : for all {t}, Tree t -> Tree t -> Tree t

## 2. Give a definition by cases for the exponential of natural numbers.
exp n 0        = 1
exp n (m + 1)  = (exp n m) * n

## 3.
### A. Which are inhabitants of Bool + Unit? 
Bool + Unit  ≈  enum { fromBool: Bool; fromUnit: Unit }


### B. Which are the inhabitants of Bool | Unit?
true, false, () - Union!

## 4. 
### A. Which are the inhabitants of Bool x Unit?
(true, ())
(false, ())
### B. Which are the inhabitants of Bool -> Unit?
λb ()

## 5. What are the major arguments put forward in the article "The String Type is Broken?"
Most current string types are flawed and can lead to incorrect program behavior, especially with internationalization.
Basic string operations like reversing, extracting substrings, and determining length often produce incorrect results due to inadequate handling of Unicode characters.Many string types treat text as a simple array of code points, leading to issues with characters like "ë" in "noël".
Languages using UTF-16 encoding, like Java and C#, struggle with characters outside the BMP (Basic Multilingual Plane), such as the cat emojis, due to the use of surrogate pairs.
Many programming languages have inadequate string handling capabilities, particularly for internationalization, leading to unexpected behavior with case conversion, normalization, and other text processing operations. Normalization is crucial for comparing strings with different composition forms, but not all languages provide built-in libraries for this, highlighting a significant gap in Unicode support.
Many string types are "broken" due to their inability to handle fundamental text processing tasks, but ann array of characters might be a more reliable alternative for Unicode text manipulation.


## 6. Can you give a type to (math things)? If so, what is it? If not, why not?
I don't think so. This question really confused me, but I think it's because its an infinite type.

## 7. Represent x ∉ A in function notation.
x ∉ A  =  not (A x)


## 8. What is a pure function? Why do we care about these things?
A pure function will always return the same output, when given the same input. It also has no side effects. It's self-contained and predictable. Pure functions are easier to test, composable, parallel and async execution-safe, reduce bugs caused by hidden state, and make code easier to reason about.

## 9. How does Haskell isolate pure and impure code?
Haskell is known as a pure language. The side effect portion of logic stays "contained" inside of IO values, and doesn't "Leak" into surrounding code. Code that has side effects is required to be in the IO monad. Once something is in the IO monad, it can never "escape". But, it's best to use the IO monad only where is necessary.


## 10. In TypeScript, which of | or & is closer to the idea of "subclassing" or "inheritance" from Python? Why? (An example will help!)
The & is an intersection type literal. An intersection type creates a new type by combining multiple existing types. The new type has all the features of the existing type. This seems similar to inheritance.
 | is a union type operator that defines a variable that can hold a value of multiple types, but it will only be of one of the types, while & builds a new type containing all of the features of the intersected types. Because of this, I think that & is closer to the idea of "subclassing" or "inheritance" from Python.